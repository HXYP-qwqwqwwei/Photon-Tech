package photontech.utils.helper;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.block.axle.AxleBlock;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleHelper {

    private AxleHelper() { }

    public static boolean connectedTo(World world, BlockState axle, BlockPos pos, Direction direction) {
        if (axle.getBlock() instanceof AxleBlock) {
            Direction.Axis axis = axle.getValue(AXIS);
            if (direction.getAxis() == axis) {
                BlockState other = world.getBlockState(pos.relative(direction));
                if (other.getBlock() instanceof AxleBlock) {
                    return other.getValue(AXIS) == axis;
                }
            }
        }
        return false;
    }
}
