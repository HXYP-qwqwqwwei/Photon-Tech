//package photontech.utils.capability.electric;
//
//import net.minecraft.nbt.CompoundNBT;
//
//public class EtMutableConductor implements IMutableConductor {
//    IEtCapacitor capacitor;
//
//    protected EtMutableConductor(IEtCapacitor capacitor) {
//        this.capacitor = capacitor;
//    }
//
//    public static EtMutableConductor of(IEtCapacitor capacitor) {
//        return new EtMutableConductor(capacitor);
//    }
//
//    public static EtMutableConductor create(double capacity, double resistance) {
//        return of(EtTransmissionLine.create(capacity, resistance));
//    }
//
//    @Override
//    public void set(IEtCapacitor capacitor) {
//        this.capacitor = capacitor;
//    }
//
//    @Override
//    public IEtCapacitor get() {
//        return this.capacitor;
//    }
//
//    @Override
//    public double getU() {
//        return this.capacitor.getU();
//    }
//
//    @Override
//    public double getC() {
//        return this.capacitor.getC();
//    }
//
//    @Override
//    public void setC(double capacity) {
//        this.capacitor.setC(capacity);
//    }
//
//    @Override
//    public double getR() {
//        return this.capacitor.getR();
//    }
//
//    @Override
//    public void setR(double resistance) {
//        this.capacitor.setR(resistance);
//    }
//
//    @Override
//    public double getQ() {
//        return this.capacitor.getQ();
//    }
//
//    @Override
//    public void setQ(double charge) {
//        this.capacitor.setQ(charge);
//    }
//
//    @Override
//    public CompoundNBT save(CompoundNBT nbt) {
//        return this.get().save(nbt);
//    }
//
//    @Override
//    public void load(CompoundNBT nbt) {
//        this.get().load(nbt);
//    }
//}
