package photontech.event.pt_events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;

public class AxleInsertEvent extends BlockEvent {

    public AxleInsertEvent(IWorld world, BlockPos pos, BlockState state) {
        super(world, pos, state);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
