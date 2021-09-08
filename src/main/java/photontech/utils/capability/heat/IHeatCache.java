package photontech.utils.capability.heat;

public interface IHeatCache extends IHeatReservoir {
    float getProcess();

    int getSurplusHeat();
}
