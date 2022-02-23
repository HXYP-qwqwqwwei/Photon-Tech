package photontech.utils.capability.electric;

public interface IMutableConductor extends IEtCapacitor {
    void set(IEtCapacitor capacitor);

    IEtCapacitor get();
}
