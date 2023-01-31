package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ConnectCondition {
    private final BlockPos pos;
    private final int radius;
    private final int offset;
    private final Direction.Axis axis;
    private final boolean reverse;

    public static ConnectCondition of(BlockPos pos, int radius, int offset, Direction.Axis axis) {
        return new ConnectCondition(pos, radius, offset, axis);
    }

    public static ConnectCondition of(BlockPos pos, int radius, int offset, Direction.Axis axis, boolean reverse) {
        return new ConnectCondition(pos, radius, offset, axis, reverse);
    }

    private ConnectCondition(BlockPos pos, int radius, int offset, Direction.Axis axis) {
        this.pos = pos;
        this.radius = radius;
        this.offset = offset;
        this.axis = axis;
        this.reverse = true;
    }

    private ConnectCondition(BlockPos pos, int radius, int offset, Direction.Axis axis, boolean reverse) {
        this.pos = pos;
        this.radius = radius;
        this.offset = offset;
        this.axis = axis;
        this.reverse = reverse;
    }


    public BlockPos getPos() {
        return pos;
    }

    public boolean reverse() {
        return reverse;
    }

    public boolean test(GearTile gear) {
        return gear.getRadius() == radius && gear.getAxis() == axis && gear.getOffset() == offset;
    }

}
