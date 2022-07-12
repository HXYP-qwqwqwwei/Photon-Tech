package photontech.event.block_event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;
import photontech.event.pt_events.KtEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEventPublisher {
    @SubscribeEvent
    public static void onBlockPlaceEvent(@Nonnull BlockEvent.EntityPlaceEvent placeEvent) {
        IWorld level = placeEvent.getWorld();

        if (!level.isClientSide()) {
            BlockPos selfPos = placeEvent.getPos();
            TileEntity self = level.getBlockEntity(selfPos);
            if (self instanceof KtMachineTile) {
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtCreateEvent((KtMachineTile) self));
            }
        }
    }


    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        IWorld level = event.getWorld();
        if (!level.isClientSide()) {
            BlockPos selfPos = event.getPos();
            TileEntity self = level.getBlockEntity(selfPos);
            if (self instanceof KtMachineTile) {
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent((KtMachineTile) self));
            }
        }
    }


}
