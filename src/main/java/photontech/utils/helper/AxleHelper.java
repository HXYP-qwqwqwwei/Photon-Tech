package photontech.utils.helper;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.block.kinetic.axle.AxleBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    public static Direction getAxisPositiveDirection(@Nonnull Direction.Axis axis) {
        switch (axis) {
            case X: return Direction.EAST;
            case Y: return Direction.UP;
            default: return Direction.SOUTH;
        }
    }
}
