package photontech.utils.capability.kinetic;

public interface IMutableBody extends IRotateBody {
    PtMutableRotateBody EMPTY = PtMutableRotateBody.create(IRotateBody.INFINITY);

    void set(IRotateBody body);

    IRotateBody get();
}
