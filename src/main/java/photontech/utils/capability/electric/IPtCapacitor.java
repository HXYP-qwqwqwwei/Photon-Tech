package photontech.utils.capability.electric;

import org.apache.logging.log4j.LogManager;

public interface IPtCapacitor {
    long INF = Long.MAX_VALUE;

    double getU();

    double getC();

    void setC(double capacity);

    double getR();

    void setR(double resistance);

    double getQ();

    void setQ(double charge);

    static double chargeExchange(IPtCapacitor cp1, IPtCapacitor cp2) {
        if (cp1 == cp2) {
            return 0.0;
        }
        final double U1 = cp1.getU();
        final double U2 = cp2.getU();
//        LogManager.getLogger().info("dU = " + (U1 - U2));
        if (U1 <= U2) {
            return 0.0;
        }
        final double C1 = cp1.getC();
        final double C2 = cp2.getC();
        final double R1 = cp1.getR();
        final double R2 = cp2.getR();
        final double Q1 = cp1.getQ();
        final double Q2 = cp2.getQ();

        double dQ = (U2 - U1) / (R1 + R2) * 2 * 0.05;
        double dQ0 = (Q2 * C1 - Q1 * C2) / (C1 + C2);
        // dQ0 <= dQ < 0
        dQ = Math.max(dQ, dQ0);
        cp1.setQ(Q1 + dQ);
        cp2.setQ(Q2 - dQ);
        return dQ;
    }

}
