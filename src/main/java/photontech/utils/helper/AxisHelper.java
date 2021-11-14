package photontech.utils.helper;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.block.kinetic.axle.AxleBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxisHelper {

    public static final Direction[] X_DIRECTIONS = { Direction.EAST, Direction.WEST };
    public static final Direction[] Y_DIRECTIONS = { Direction.UP, Direction.DOWN };
    public static final Direction[] Z_DIRECTIONS = { Direction.SOUTH, Direction.NORTH };
    public static final Direction[] HORIZONTAL_DIRECTION = { Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH };


    private AxisHelper() { }


    public static Direction getAxisPositiveDirection(@Nonnull Direction.Axis axis) {
        switch (axis) {
            case X: return Direction.EAST;
            case Y: return Direction.UP;
            default: return Direction.SOUTH;
        }
    }

    /**
     * 判断一个方向是否为坐标轴的正方向
     * @param direction 方位
     * @return 是正方向则返回true，否则返回false
     */
    public static boolean isAxisPositiveDirection(@Nonnull Direction direction) {
        switch (direction) {
            case UP:
            case EAST:
            case SOUTH:
                return true;
            default:
                return false;
        }
    }

    public static Direction[] getAxisDirections(Direction.Axis axis) {
        switch (axis) {
            case X: return X_DIRECTIONS;
            case Y: return Y_DIRECTIONS;
        }
        return Z_DIRECTIONS;
    }

}
