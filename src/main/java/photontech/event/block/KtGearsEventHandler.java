package photontech.event.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;
import photontech.event.pt.KtEvent;
import photontech.utils.helperfunctions.AxisHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KtGearsEventHandler {

    @SubscribeEvent
    public static void onAxialCombinedEvent(KtEvent.KtGearSynchronizeNotifyEvent event) {
        IWorld level = event.getWorld();
        if (level instanceof ServerWorld) {
            KtMachineTile selfKt = event.getSelfKt();
            selfKt.flags = ((ServerWorld) level).getGameTime();
            selfKt.getMainKtTile().expired = false;
        }
    }

    // TODO 等效惯量
    @SubscribeEvent
    public static void onGearSynchronize(KtEvent.KtGearSynchronizeEvent event) {
        IWorld level = event.getWorld();
        BlockPos selfPos = event.getPos();
        KtGearTile selfGear = event.getGearKt();
        KtMachineTile selfMainKt = selfGear.getMainKtTile();
        // 过期的事件
        if (selfMainKt.expired) {
            return;
        }
        Direction.Axis axis = selfGear.getAxis();
        BlockPos[] searchPoses = selfGear.getSearchPositions();

        // 从自身开始，递归地向邻居传递，邻居均以自身为参考
        for (BlockPos searchPos : searchPoses) {
            TileEntity te = level.getBlockEntity(searchPos);
            if (te instanceof KtGearTile) {
                KtGearTile neighborGear = (KtGearTile) te;
                int distSqr = distSqrInt(selfPos, searchPos);
                if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;
                if (!canConnect(selfGear, neighborGear, distSqr)) continue;

                KtMachineTile neighborMainKt = neighborGear.getMainKtTile();
                // 以自身为参考，计算邻居频率
                int fixedFrequency = selfMainKt.referenceState.frequency + log2Int(1.0 * selfGear.getRadius() / neighborGear.getRadius());
                if (fixedFrequency < 0) {   // 频率小于0的情况，重新以邻居为参考开始同步
                    selfMainKt.expired = true;  // 用于通知即将发布的事件
                    neighborMainKt.initRefState();
                    neighborMainKt.rotatingState.reset();
                    MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
                    break;
                }
                double fixedPhase = -selfMainKt.getAngle() * (1.0 * selfGear.getRadius() / neighborGear.getRadius());
                fixedPhase += getNeighborPhase(selfGear, neighborGear);

                if (selfMainKt.referenceState.refKtPos == neighborMainKt.referenceState.refKtPos) {
                    int currentFrequency = neighborMainKt.referenceState.frequency;
                    if (currentFrequency != fixedFrequency) {
                        level.destroyBlock(selfPos, true);
                        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfMainKt));
                        break;
                    }
                    neighborMainKt.referenceState.phase = fixedPhase;
                    continue;
                }
                neighborMainKt.referenceState.refKtPos = selfMainKt.referenceState.refKtPos;
                LogManager.getLogger().info(selfMainKt.getAngle());
                neighborMainKt.referenceState.phase = fixedPhase;
                neighborMainKt.referenceState.frequency = fixedFrequency;
                neighborMainKt.referenceState.reversed = !selfMainKt.referenceState.reversed;
                neighborMainKt.setDirty(true);
                selfMainKt.getRefKtTile().referenceState.equivalentInertia += neighborMainKt.referenceState.sumInertia;
                // 同步完之后，向总线报告一个通知事件
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
            }
        }
    }

    @SubscribeEvent
    public static void onGearInvalidate(KtEvent.KtInvalidateEvent event) {
        KtMachineTile kt = event.getSelfKt();
        IWorld level = event.getWorld();
        BlockPos selfPos = event.getPos();
        if (kt instanceof KtGearTile) {
            Direction.Axis axis = kt.getAxis();
            Direction[] verticalSides = AxisHelper.getVerticalDirections(axis);
            for (Direction side : verticalSides) {
                TileEntity te = level.getBlockEntity(selfPos.relative(side));
                if (te instanceof KtGearTile) {
                    KtGearTile neighborGear = (KtGearTile) te;
                    if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;
                    neighborGear.initRefState();
                    MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
                }
            }
        }
    }

    private static double getNeighborPhase(KtGearTile self, KtGearTile neighbor) {
        return -Math.PI / neighbor.getTeethNumber();
    }

    private static int log2Int(double i) {
        return (int) Math.round(Math.log(i) / Math.log(2));
    }

    private static int distSqrInt(BlockPos p1, BlockPos p2) {
        return squareInt(p1.getX() - p2.getX())
                + squareInt(p1.getY() - p2.getY())
                + squareInt(p1.getZ() - p2.getZ());
    }

    public static int squareInt(int i) {
        return i * i;
    }

    private static boolean canConnect(KtGearTile g1, KtGearTile g2, int distanceSqr) {
        switch (distanceSqr) {
            case 1: return g1.getRadius() + g2.getRadius() == 2;
            case 2: return g1.getRadius() + g2.getRadius() == 3;
        }
        return false;
    }

}
