package photontech.utils.data.electric;

import net.minecraftforge.common.util.LazyOptional;
import photontech.utils.data.ISaveLoadWithRefCnt;

public interface ICapacitor extends ISaveLoadWithRefCnt {
    LazyOptional<ICapacitor> PLACE_HOLDER = LazyOptional.of(() -> DCWireCapacitor.create(Double.POSITIVE_INFINITY));

    double getPotential();

    double getCapacity();

    double getQ();

    void setQ(double charge);

}
