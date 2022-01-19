package photontech.utils.capability.electric;

import net.minecraft.nbt.CompoundNBT;

public class PtMutableConductor implements IMutableConductor {
    IPtCapacitor capacitor;

    protected PtMutableConductor(IPtCapacitor capacitor) {
        this.capacitor = capacitor;
    }

    public static PtMutableConductor of(IPtCapacitor capacitor) {
        return new PtMutableConductor(capacitor);
    }

    public static PtMutableConductor create(double capacity, double resistance) {
        return of(PtConductor.create(capacity, resistance));
    }

    @Override
    public void set(IPtCapacitor capacitor) {
        this.capacitor = capacitor;
    }

    @Override
    public IPtCapacitor get() {
        return this.capacitor;
    }

    @Override
    public double getU() {
        return this.capacitor.getU();
    }

    @Override
    public double getC() {
        return this.capacitor.getC();
    }

    @Override
    public void setC(double capacity) {
        this.capacitor.setC(capacity);
    }

    @Override
    public double getR() {
        return this.capacitor.getR();
    }

    @Override
    public void setR(double resistance) {
        this.capacitor.setR(resistance);
    }

    @Override
    public double getQ() {
        return this.capacitor.getQ();
    }

    @Override
    public void setQ(double charge) {
        this.capacitor.setQ(charge);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return this.get().save(nbt);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.get().load(nbt);
    }
}
