package photontech.event.handler.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.data.PtDataManager;
import photontech.utils.IMixinWorld;
import photontech.utils.tileentity.ChainedUpdatingMachine;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChainedBlockEventHandler {


    @SubscribeEvent
    public static void onPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        BlockPos pos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity te = level.getBlockEntity(pos);
        if (te instanceof ChainedUpdatingMachine) {
            ChainedUpdatingMachine machine = (ChainedUpdatingMachine) te;
            machine.setNewID(machine.getDataManager(level).allocateID());
            ((IMixinWorld)level).updateChainedMachine(machine);
        }
    }

    @SubscribeEvent
    public static void onRemoveEvent(BlockEvent.BreakEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity te = level.getBlockEntity(selfPos);
        if (te instanceof ChainedUpdatingMachine) {
            ChainedUpdatingMachine machine = ((ChainedUpdatingMachine) te);
            PtDataManager<?> data = machine.getDataManager(level);

            for (Direction side : Direction.values()) {
                if (machine.isValidSide(side)) {
                    TileEntity neighbor = level.getBlockEntity(selfPos.relative(side));
                    if (neighbor instanceof ChainedUpdatingMachine) {
                        ChainedUpdatingMachine neighborMachine = (ChainedUpdatingMachine) neighbor;
                        if (neighborMachine.getCapability(machine.getUpdateCap(), side.getOpposite()).isPresent()) {
                            neighborMachine.setNewID(data.allocateID());
                            ((IMixinWorld)level).updateChainedMachine(neighborMachine);
                        }
                    }
                }
            }

        }
    }
}
