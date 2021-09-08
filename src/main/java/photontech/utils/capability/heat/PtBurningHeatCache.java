package photontech.utils.capability.heat;

public class PtBurningHeatCache extends PtHeatCache {
    public PtBurningHeatCache(){
        super();
    }

    PtBurningHeatCache(float temperature) {
        super(temperature);
    }

    @Override
    public int acceptHeat(int maxHeat, boolean simulate) {
        return 0;
    }
}
