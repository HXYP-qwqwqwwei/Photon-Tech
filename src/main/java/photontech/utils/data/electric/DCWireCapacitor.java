package photontech.utils.data.electric;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class DCWireCapacitor implements ElectricCapacitor {
    public static final String CHARGE = "Charge";
    public static final String OVERLOAD_CURRENT = "OverloadCurrent";
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

    public static DCWireCapacitor create(double overloadCurrent) {
        return new DCWireCapacitor(overloadCurrent);
    }

    @Override
    public double getPotential() {
        return this.getCharge() / this.getCapacity();
    }

    @Override
    public double getCapacity() {
        return overloadCurrent * 0.05;
    }

    @Override
    public double getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
//        nbt.putDouble("Capacity", this.capacity);
        nbt.putDouble(CHARGE, this.charge);
        nbt.putDouble(OVERLOAD_CURRENT, this.overloadCurrent);
        nbt.putInt(REF_CNT, this.refCnt);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
//        this.capacity = nbt.getFloat("Capacity");
        this.charge = nbt.getDouble(CHARGE);
        this.overloadCurrent = nbt.getDouble(OVERLOAD_CURRENT);
        this.refCnt = nbt.getInt(REF_CNT);
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
