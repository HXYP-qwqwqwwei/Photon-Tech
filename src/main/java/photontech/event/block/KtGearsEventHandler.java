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
        }
    }

    // TODO 等效惯量的设定
    @SubscribeEvent
    public static void onGearSynchronize(KtEvent.KtGearSynchronizeEvent event) {
        IWorld level = event.getWorld();
        BlockPos selfPos = event.getPos();
        KtGearTile selfGear = event.getGearKt();
        Direction.Axis axis = selfGear.getAxis();
        BlockPos[] searchPoses = selfGear.getSearchPositions();
        for (BlockPos searchPos : searchPoses) {
            TileEntity te = level.getBlockEntity(searchPos);
            if (te instanceof KtGearTile) {
                KtGearTile neighborGear = (KtGearTile) te;
                int distSqr = distSqrInt(selfPos, searchPos);
                if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;
                if (!canConnect(selfGear, neighborGear, distSqr)) continue;

                KtMachineTile selfMainKt = selfGear.getMainKtTile();
                KtMachineTile neighborMainKt = neighborGear.getMainKtTile();
                int fixedFrequency = selfMainKt.ktReferenceState.frequency + log2Int(1.0 * selfGear.getRadius() / neighborGear.getRadius());

                double fixedPhase = -selfMainKt.getAngle() * (1.0 * selfGear.getRadius() / neighborGear.getRadius());
                fixedPhase += getNeighborPhase(selfGear, neighborGear);

                if (selfMainKt.ktReferenceState.refKtPos == neighborMainKt.ktReferenceState.refKtPos) {
                    int currentFrequency = neighborMainKt.ktReferenceState.frequency;
                    if (currentFrequency != fixedFrequency) {
                        level.destroyBlock(selfPos, true);
                        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfMainKt));
                        break;
                    }
                    neighborMainKt.ktReferenceState.phase = fixedPhase;
                    continue;
                }
                neighborMainKt.ktReferenceState.refKtPos = selfMainKt.ktReferenceState.refKtPos;
                LogManager.getLogger().info(selfMainKt.getAngle());
                neighborMainKt.ktReferenceState.phase = fixedPhase;
                neighborMainKt.ktReferenceState.frequency = fixedFrequency;
                neighborMainKt.ktReferenceState.reversed = !selfMainKt.ktReferenceState.reversed;
                neighborMainKt.setDirty(true);
                selfMainKt.getRefKtTile().ktReferenceState.equivalentInertia += neighborMainKt.ktReferenceState.sumInertia;
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
