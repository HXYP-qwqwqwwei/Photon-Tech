package photontech.utils.data;

public interface Hydraulic extends SaveLoadableWithRefCnt {
    String WORKING_FLUID = "WorkingFluid";

    Hydraulic PLACE_HOLDER = HydraulicPipe.create();

    int getPressure();

    void pressurize(int pressure);

    void depressurize();

    int getSize();
}
