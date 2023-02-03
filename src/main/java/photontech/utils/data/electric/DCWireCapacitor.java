package photontech.utils.data.electric;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class DCWireCapacitor implements ICapacitor {
    // 电容
//    protected double capacity;
    // 存储的电荷量
    protected double charge;
    // 过载电流
    protected double overloadCurrent;
    public int refCnt = 1;

    private DCWireCapacitor(double overloadCurrent) {
        this.overloadCurrent = overloadCurrent;
    }

    public static DCWireCapacitor create(double overloadEtCurrent) {
        return new DCWireCapacitor(overloadEtCurrent);
    }

    @Override
    public double getPotential() {
        return this.getQ() / this.getCapacity();
    }

    @Override
    public double getCapacity() {
        return overloadCurrent * 0.05;
    }

    @Override
    public double getQ() {
        return this.charge;
    }

    @Override
    public void setQ(double charge) {
        this.charge = charge;
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
//        nbt.putDouble("Capacity", this.capacity);
        nbt.putDouble("Charge", this.charge);
        nbt.putDouble("OverloadEtCurrent", this.overloadCurrent);
        nbt.putInt("RefCnt", this.refCnt);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
//        this.capacity = nbt.getFloat("Capacity");
        this.charge = nbt.getDouble("Charge");
        this.overloadCurrent = nbt.getDouble("OverloadEtCurrent");
        this.refCnt = nbt.getInt("RefCnt");
    }

    @Override
    public boolean isNoRef() {
        return refCnt == 0;
    }

    @Override
    public void plusRef() {
        this.refCnt += 1;
    }

    @Override
    public void minusRef() {
        if (this.refCnt == 0) {
            throw new RuntimeException("Dereference a non-ref Data");
        }
        this.refCnt -= 1;
    }
}
