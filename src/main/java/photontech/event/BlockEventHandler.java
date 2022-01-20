package photontech.event;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.axle.AxleBlock;
import photontech.block.kinetic.axle.AxleTile;

import static net.minecraft.state.properties.BlockStateProperties.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEventHandler {
    @SubscribeEvent
    public static void onBlockEvent(BlockEvent event) {
        BlockState blockState = event.getState();
        BlockPos pos = event.getPos();
        IWorld level = event.getWorld();
        if (blockState.getBlock() instanceof AxleBlock) {
            TileEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof AxleTile) {
                ((AxleTile)tileEntity).setCurrentAxis(blockState.getValue(AXIS));
            }
        }
    }
}
