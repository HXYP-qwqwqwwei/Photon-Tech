package photontech.utils.capability.kinetic;

public interface IMutableBody extends IRotateBody {
    void set(IRotateBody body);

    IRotateBody get();
}
