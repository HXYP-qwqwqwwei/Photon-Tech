package photontech.event.block_event;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.electric.wire.PtWireTile;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EtWireBlockEvent {
    public static void onWirePlaceEvent(BlockEvent.EntityPlaceEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity tile = level.getBlockEntity(selfPos);
        if (tile instanceof PtWireTile) {

        }
    }
}
