package photontech.utils.tileentity;

import photontech.utils.data.heat.IHeatReservoir;
import photontech.utils.data.heat.PtHeatReservoir;

public interface IHeatReservoirTile extends IHeatExchange {

    IHeatReservoir getHeatReservoir();

    default PtHeatReservoir createHeatReservoir(float initTemp, float overloadTemp, float capacity, float transferRate) {
        return new PtHeatReservoir(initTemp, overloadTemp, capacity, transferRate);
    }

}
