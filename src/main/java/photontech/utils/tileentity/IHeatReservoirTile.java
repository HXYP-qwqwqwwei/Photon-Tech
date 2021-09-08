package photontech.utils.tileentity;

import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.heat.PtHeatReservoir;

public interface IHeatReservoirTile extends IHeatExchange {

    IHeatReservoir getHeatReservoir();

    default PtHeatReservoir createHeatReservoir(float initTemp, float overloadTemp, float capacity, float transferRate) {
        return new PtHeatReservoir(initTemp, overloadTemp, capacity, transferRate);
    }

}
