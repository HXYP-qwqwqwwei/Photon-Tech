package photontech.utils.capability.electric;

public class InfiniteCapacitor implements IMutableConductor {
    private double U;

    protected InfiniteCapacitor(double U) {
        this.U = U;
    }

    public static InfiniteCapacitor create(double U) {
        return new InfiniteCapacitor(U);
    }

    @Override
    public double getU() {
        return this.U;
    }

    @Override
    public double getC() {
        return 0;
    }

    @Override
    public void setC(double capacity) {

    }

    @Override
    public double getR() {
        return 0;
    }

    @Override
    public void setR(double resistance) {

    }

    @Override
    public double getQ() {
        return this.U * Double.POSITIVE_INFINITY;
    }

    @Override
    public void setQ(double charge) {

    }

    public void setU(double u) {
        U = u;
    }

    @Override
    public void set(IPtCapacitor capacitor) {

    }

    @Override
    public IPtCapacitor get() {
        return this;
    }
}
