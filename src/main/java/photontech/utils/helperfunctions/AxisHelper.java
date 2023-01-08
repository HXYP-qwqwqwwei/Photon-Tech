package photontech.utils.helperfunctions;

import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

public class AxisHelper {

    public static final Direction[] X_DIRECTIONS = { Direction.EAST, Direction.WEST };
    public static final Direction[] Y_DIRECTIONS = { Direction.UP, Direction.DOWN };
    public static final Direction[] Z_DIRECTIONS = { Direction.SOUTH, Direction.NORTH };
    public static final Direction[] XZ_DIRECTIONS = { Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH };
    public static final Direction[] XY_DIRECTIONS = { Direction.EAST, Direction.UP, Direction.WEST, Direction.DOWN };
    public static final Direction[] YZ_DIRECTIONS = { Direction.UP, Direction.SOUTH, Direction.DOWN, Direction.NORTH };


    private AxisHelper() { }

    /**
     * 获取与输入的两个轴垂直的另一个轴
     * @param axis 第一个轴
     * @param verticalAxis 与第一个轴垂直的另一个轴
     * @return 与上面两个轴垂直的轴，如果输入的两个轴不垂直（即相等），则返回null
     */
    public static Direction.Axis getVerticalAxis(Direction.Axis axis, Direction.Axis verticalAxis) {
        switch (axis) {
            case X:
                switch (verticalAxis) {
                    case Y: return Direction.Axis.Z;
                    case Z: return Direction.Axis.Y;
                    default: return null;
                }
            case Y:
                switch (verticalAxis) {
                    case X: return Direction.Axis.Z;
                    case Z: return Direction.Axis.X;
                    default: return null;
                }
            default:
                switch (verticalAxis) {
                    case X: return Direction.Axis.Y;
                    case Y: return Direction.Axis.X;
                    default: return null;
                }
        }
    }

    public static Direction[] getVerticalDirections(Direction.Axis axis) {
        switch (axis) {
            case X: return YZ_DIRECTIONS;
            case Y: return XZ_DIRECTIONS;
            default: return XY_DIRECTIONS;
        }
    }


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
