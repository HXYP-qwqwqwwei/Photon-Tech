package photontech.utils.capability.electric;

import net.minecraft.nbt.CompoundNBT;

public class PtConductor implements IPtCapacitor {
    // 电容
    protected double capacity;
    // 电阻
    protected double resistance;
    // 存储的电荷量
    protected double charge;

    private PtConductor(double capacity, double resistance) {
        this.capacity = capacity;
        this.resistance = resistance;
    }

    public static PtConductor create(double capacity, double resistance) {
        return new PtConductor(capacity, resistance);
    }

    @Override
    public double getU() {
        return this.getQ() / this.getC();
    }

    @Override
    public double getC() {
        return capacity;
    }

    @Override
    public void setC(double capacity) {
        this.capacity = capacity;
    }

    @Override
    public double getR() {
        return resistance;
    }

    @Override
    public void setR(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public double getQ() {
        return this.charge;
    }

    @Override
    public void setQ(double charge) {
        this.charge = charge;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("Capacity", this.capacity);
        nbt.putDouble("Charge", this.charge);
        nbt.putDouble("Resistance", this.resistance);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.capacity = nbt.getFloat("Capacity");
        this.charge = nbt.getDouble("Charge");
        this.resistance = nbt.getDouble("Resistance");
    }

}
