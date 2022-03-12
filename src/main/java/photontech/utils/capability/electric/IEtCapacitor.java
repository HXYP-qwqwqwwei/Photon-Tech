package photontech.utils.capability.electric;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import photontech.world_data.PtComplexCapabilityData;

public interface IEtCapacitor extends PtComplexCapabilityData.ISaveLoadWithRefCnt {
    long INF = Long.MAX_VALUE;
    LazyOptional<IEtCapacitor> PLACE_HOLDER = LazyOptional.of(() -> new IEtCapacitor() {
        @Override
        public double getU() {
            return 0;
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
            return 0;
        }

        @Override
        public void setQ(double charge) {

        }

        @Override
        public boolean isNoRef() {
            return true;
        }

        @Override
        public void addRef() {

        }

        @Override
        public void minusRef() {

        }

        @Override
        public void load(CompoundNBT nbt) {

        }

        @Override
        public CompoundNBT save(CompoundNBT nbt) {
            return null;
        }
    });

    double getU();

    double getC();

    void setC(double capacity);

    double getR();

    void setR(double resistance);

    double getQ();

    void setQ(double charge);

    static double chargeExchange(IEtCapacitor p, IEtCapacitor n, double dU_eq) {
        if (p == n) {
            return Double.POSITIVE_INFINITY;
        }
        final double C1 = p.getC();
        final double C2 = n.getC();
        final double Q1 = p.getQ();
        final double Q2 = n.getQ();

        double dQ0 = (Q1*C2 - Q2*C1 - dU_eq*C1*C2) / (C1 + C2);

        p.setQ(Q1 - dQ0);
        n.setQ(Q2 + dQ0);
        return dQ0;
    }

    static double chargeExchange(IEtCapacitor p, IEtCapacitor n, double dU_eq, double R) {
        if (R == 0) {
            return chargeExchange(p, n, dU_eq);
        }
        final double U1 = p.getU();
        final double U2 = n.getU();
        // 有效电势差U_valid
        final double Uv = U1 - U2 - dU_eq;
        if (p == n) {
            return Uv / R * 0.05;
        }
        final double C1 = p.getC();
        final double C2 = n.getC();
        final double Q1 = p.getQ();
        final double Q2 = n.getQ();

        double dQ0 = (Q1*C2 - Q2*C1 - dU_eq*C1*C2) / (C1 + C2);
        double dQdt = Uv / R * 0.05;
        dQ0 = dQ0 > 0 ? Math.min(dQdt, dQ0) : Math.max(dQdt, dQ0);

        p.setQ(Q1 - dQ0);
        n.setQ(Q2 + dQ0);
        return dQ0 * 20;
    }

}
