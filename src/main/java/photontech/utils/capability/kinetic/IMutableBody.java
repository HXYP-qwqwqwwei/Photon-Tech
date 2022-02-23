package photontech.utils.capability.kinetic;

import net.minecraft.util.Direction;

public interface IMutableBody extends IRotateBody {
    IMutableBody EMPTY = PtMutableRotateBody.of(PtRotateBody.create(IRotateBody.INFINITY), null);
    int MAX_LENGTH = 32;

    void set(IRotateBody body);

    IRotateBody get();

    int getLength();

    void setLength(int length);

    Direction.Axis getAxis();

    void setAxis(Direction.Axis axis);

    boolean isEmpty();
}
