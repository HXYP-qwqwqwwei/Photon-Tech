package photontech.event.block_event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;
import photontech.event.pt_events.KtEvent;
import photontech.utils.helper_functions.AxisHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KtGearsEventHandler {

    @SubscribeEvent
    public static void onGearCreate(KtEvent.KtGearCreateEvent event) {
        IWorld level = event.getWorld();
        if (level.isClientSide()) return;
        event.getGearsKt().flags = 1;
    }

    @SubscribeEvent
    public static void onGearSynchronize(KtEvent.KtGearSynchronizeEvent event) {
        IWorld level = event.getWorld();
        BlockPos selfPos = event.getPos();
        KtGearTile selfGear = event.getGearsKt();
        Direction.Axis axis = selfGear.getAxis();
        Direction[] verticalSides = AxisHelper.getVerticalDirections(axis);
        for (Direction side : verticalSides) {
            TileEntity te = level.getBlockEntity(selfPos.relative(side));
            if (te instanceof KtGearTile) {
                KtGearTile gear = (KtGearTile) te;
                if (!gear.isKtValid() || gear.getAxis() != axis) return;

                LogManager.getLogger().info(te);
                KtMachineTile selfMainKt = selfGear.getMainKtTile();
                KtMachineTile neighborMainKt = gear.getMainKtTile();
                if (selfMainKt.ktStatue.refKtPos == neighborMainKt.ktStatue.refKtPos) {
                    LogManager.getLogger().info("!!!!!!!!!!!!");
                    if (selfMainKt.ktStatue.frequency != -neighborMainKt.ktStatue.frequency) {
                        level.destroyBlock(selfPos, true);
                        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfMainKt));
                    }
                }
                selfGear.ktStatue.refKtPos = neighborMainKt.ktStatue.refKtPos;
                selfGear.ktStatue.phase = neighborMainKt.ktStatue.phase + 1;
                selfGear.ktStatue.frequency = neighborMainKt.ktStatue.frequency;
                selfGear.ktStatue.reversed = !neighborMainKt.ktStatue.reversed;
                selfGear.setDirty(true);

            }

        }

    }
}
