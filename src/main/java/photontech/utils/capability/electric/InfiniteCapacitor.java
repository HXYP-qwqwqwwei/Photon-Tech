package photontech.utils.capability.electric;

import net.minecraft.nbt.CompoundNBT;

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
        return Long.MAX_VALUE;
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
        return this.U * this.getC();
    }

    @Override
    public void setQ(double charge) {

    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("Voltage", this.U);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.U = nbt.getDouble("Voltage");
    }

    public void setU(double u) {
        U = u;
    }

    @Override
    public void set(IEtCapacitor capacitor) {

    }

    @Override
    public IEtCapacitor get() {
        return this;
    }

}
