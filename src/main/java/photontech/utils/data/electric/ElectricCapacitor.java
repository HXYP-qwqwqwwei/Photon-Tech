package photontech.utils.data.electric;

import net.minecraftforge.common.util.LazyOptional;
import photontech.utils.data.SaveLoadableWithRefCnt;

public interface ElectricCapacitor extends SaveLoadableWithRefCnt {
    LazyOptional<ElectricCapacitor> PLACE_HOLDER = LazyOptional.of(() -> DCWireCapacitor.create(Double.POSITIVE_INFINITY));

    double getPotential();

    double getCapacity();

    double getCharge();

    void setCharge(double charge);

}
