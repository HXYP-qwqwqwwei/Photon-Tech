package photontech.utils.capability.electric;

public interface IMutableConductor extends IPtCapacitor {
    void set(IPtCapacitor capacitor);

    IPtCapacitor get();
}
