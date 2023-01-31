package photontech.event.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.KineticMachine;
import photontech.event.pt.KtEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEventPublisher {
    @SubscribeEvent
    public static void onBlockPlaceEvent(@Nonnull BlockEvent.EntityPlaceEvent placeEvent) {
        IWorld level = placeEvent.getWorld();
        if (level.isClientSide()) return;

        BlockPos selfPos = placeEvent.getPos();
        TileEntity te = level.getBlockEntity(selfPos);
        if (te instanceof KineticMachine) {
            KineticMachine machine = (KineticMachine) te;
            if (!machine.isActive()) return;
            MinecraftForge.EVENT_BUS.post(new KtEvent.KtCreateEvent(machine));
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        IWorld level = event.getWorld();
        if (!level.isClientSide()) {
            BlockPos selfPos = event.getPos();
            TileEntity self = level.getBlockEntity(selfPos);
            if (self instanceof KineticMachine && ((KineticMachine) self).isActive()) {
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent((KineticMachine) self));
            }
        }
    }

    @SubscribeEvent
    public static void onKtKtActiveEvent(KtEvent.KtAxialCombinedEvent event) {
        event.getMachine().primaryReset();
        MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(event.getMachine()));
    }

}
