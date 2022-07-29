package photontech.event.block_event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;
import photontech.event.pt_events.KtEvent;
import photontech.utils.helper_functions.AxisHelper;

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
        Direction[] verticalSides = AxisHelper.getVerticalDirections(axis);
        for (Direction side : verticalSides) {
            TileEntity te = level.getBlockEntity(selfPos.relative(side));
            if (te instanceof KtGearTile) {
                KtGearTile neighborGear = (KtGearTile) te;
                if (!neighborGear.isKtValid() || neighborGear.getAxis() != axis) continue;

                KtMachineTile selfMainKt = selfGear.getMainKtTile();
                KtMachineTile neighborMainKt = neighborGear.getMainKtTile();
                if (selfMainKt.ktReferenceState.refKtPos == neighborMainKt.ktReferenceState.refKtPos) {
                    if (selfMainKt.ktReferenceState.frequency != -neighborMainKt.ktReferenceState.frequency) {
                        level.destroyBlock(selfPos, true);
                        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfMainKt));
                        break;
                    }
                    continue;
                }
                neighborMainKt.ktReferenceState.refKtPos = selfMainKt.ktReferenceState.refKtPos;
                neighborMainKt.ktReferenceState.phase  = selfMainKt.ktReferenceState.phase + 1;
                neighborMainKt.ktReferenceState.frequency = selfMainKt.ktReferenceState.frequency;
                neighborMainKt.ktReferenceState.reversed = !selfMainKt.ktReferenceState.reversed;
                neighborMainKt.setDirty(true);
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

}
