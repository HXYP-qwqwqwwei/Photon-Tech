package photontech.utils.capability.electric;

import net.minecraft.nbt.CompoundNBT;

public class EtTransmissionLine implements IEtCapacitor {
    // 电容
    protected double capacity;
    // 存储的电荷量
    protected double charge;
    // 过载电流
    protected double overloadEtCurrent;
    protected int id;
    public int refCnt = 1;

    private EtTransmissionLine(double capacity, double overloadEtCurrent) {
        this.capacity = capacity;
        this.overloadEtCurrent = overloadEtCurrent;
    }

    public static EtTransmissionLine create(double capacity, double overloadEtCurrent) {
        return new EtTransmissionLine(capacity, overloadEtCurrent);
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
        return 0;
    }

    @Override
    public void setR(double resistance) {}

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
        nbt.putDouble("OverloadEtCurrent", this.overloadEtCurrent);
        nbt.putInt("RefCnt", this.refCnt);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.capacity = nbt.getFloat("Capacity");
        this.charge = nbt.getDouble("Charge");
        this.overloadEtCurrent = nbt.getDouble("OverloadEtCurrent");
        this.refCnt = nbt.getInt("RefCnt");
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }
}
