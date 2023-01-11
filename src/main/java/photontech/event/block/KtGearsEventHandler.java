package photontech.event.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;
import photontech.event.pt.KtEvent;
import photontech.utils.helper.fuctions.MathFunctions;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KtGearsEventHandler {

    @SubscribeEvent
    public static void onAxialCombinedEvent(KtEvent.KtGearSynchronizeNotifyEvent event) {
        IWorld level = event.getWorld();
        KtMachineTile selfKt = event.getSelfKt();
        selfKt.gearNotifyTick = level.getLevelData().getGameTime();
        selfKt.getMainKtTile().expired = false;
    }

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
                int distSqr = MathFunctions.distSqrInt(selfPos, searchPos);
                if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;
                if (!canConnect(selfGear, neighborGear, distSqr)) continue;

                KtMachineTile neighborMainKt = neighborGear.getMainKtTile();
                // 以自身为参考，计算邻居频率
                int frequencyLevel = selfMainKt.referenceState.frequencyLevel + MathFunctions.log2Int(1.0 * selfGear.getRadius() / neighborGear.getRadius());
                if (frequencyLevel < 0) {   // 频率等级小于0的情况，重新以邻居为参考开始同步
                    selfMainKt.expired = true;  // 用于取消已发布的事件
                    neighborMainKt.reset();
                    // TODO 关闭动能损失
                    neighborMainKt.rotatingState.reset();
                    MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
                    break;
                }
                double fixedPhase = -selfMainKt.getAngle() * (1.0 * selfGear.getRadius() / neighborGear.getRadius());
                fixedPhase -= Math.PI / neighborGear.getTeethAmount();

                if (selfMainKt.referenceState.refKtPos == neighborMainKt.referenceState.refKtPos) {
                    int currentFrequency = neighborMainKt.referenceState.frequencyLevel;
                    if (currentFrequency != frequencyLevel) {
                        level.destroyBlock(selfPos, true);
                        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfMainKt));
                        break;
                    }
                    neighborMainKt.referenceState.phase = fixedPhase;
                    continue;
                }
                // 同步邻居
                neighborMainKt.referenceState.refKtPos = selfMainKt.referenceState.refKtPos;
                neighborMainKt.referenceState.phase = fixedPhase;
                neighborMainKt.referenceState.frequencyLevel = frequencyLevel;
                neighborMainKt.referenceState.reversed = !selfMainKt.referenceState.reversed;
                neighborMainKt.setDirty(true);

                //计算等效的转动惯量
                int frequency = MathFunctions.pow2Int(frequencyLevel);
                KtMachineTile refKt = selfMainKt.getRefKtTile();
                refKt.referenceState.equivalentInertia += neighborMainKt.referenceState.getAxialSumInertia() * frequency;

                // 同步完之后，向总线报告一个通知事件
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
            }
        }
    }

    @SubscribeEvent
    public static void onGearInvalidate(KtEvent.KtInvalidateEvent event) {
        KtMachineTile kt = event.getSelfKt();
        IWorld level = event.getWorld();
        if (kt instanceof KtGearTile) {
            KtGearTile selfGear = (KtGearTile) kt;
            Direction.Axis axis = kt.getAxis();
            BlockPos[] verticalSides = selfGear.getSearchPositions();
            for (BlockPos pos : verticalSides) {
                TileEntity te = level.getBlockEntity(pos);
                if (te instanceof KtGearTile) {
                    KtGearTile neighborGear = (KtGearTile) te;
                    if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;

                    neighborGear.getMainKtTile().reset();
                    MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
                }
            }
        }
    }

    private static boolean canConnect(KtGearTile g1, KtGearTile g2, int distanceSqr) {
        switch (distanceSqr) {
            case 1: return g1.getRadius() + g2.getRadius() == 2;
            case 2: return g1.getRadius() + g2.getRadius() == 3;
        }
        return false;
    }

}
