package photontech.utils.helper.fuctions;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import photontech.block.kinetic.KineticMachine;
import photontech.block.kinetic.gears.GearTile;
import photontech.block.magnet.MagnetTile;
import photontech.init.PtCapabilities;
import photontech.utils.data.electric.ICapacitor;
import photontech.utils.data.magnet.IMagnet;

import javax.annotation.Nullable;

public class PtPhysics {

    public static void axialMomentumConservation(KineticMachine... machines) {
        long L = 0;
        long I = 0;
        for (KineticMachine ma : machines) {
            if (ma == null) continue;
            L += ma.getMomentum();
            I += ma.getInertia();
        }
        float av = 1F * L / I;
        for (KineticMachine ma : machines) {
            if (ma == null) continue;
            ma.setAngularVelocity(av);
        }
    }

    // L = I1*v*f1 + I2*v*f2
    // L = (I1f1 + I2f2)v
    public static void gearsMomentumConservation(GearTile g1, GearTile g2, boolean reverse) {
        int c = reverse ? -1 : 1;
        long L = g1.getMomentum() + g2.getMomentum() * c;
        long I = g1.getInertia() / g1.getRadius() + g2.getInertia() / g2.getRadius();
        float av = 1F * L / I;
        g1.setAngularVelocity(av / g1.getRadius());
        g2.setAngularVelocity(av / g2.getRadius() * c);
    }

    public static void chargeTransfer(ICapacitor p, ICapacitor n, double I) {
        p.setQ(p.getQ() - I * 0.05);
        n.setQ(n.getQ() + I * 0.05);
    }

    public static double getMagnetFlux(@Nullable TileEntity te, Direction side) {
        if (te == null) return 0;
        if (te instanceof MagnetTile) {
            return ((MagnetTile) te).getMagnetPole(side).getFluxDensity(side);
        }
        else return te.getCapability(PtCapabilities.MAGNET, side).orElse(IMagnet.EMPTY).getFluxDensity(side);
    }

    public static double chargeExchange(ICapacitor p, ICapacitor n, double dU_eq) {
        if (p == n) {
            return Double.POSITIVE_INFINITY;
        }
        final double C1 = p.getCapacity();
        final double C2 = n.getCapacity();
        final double Q1 = p.getQ();
        final double Q2 = n.getQ();

        double dQ0 = (Q1*C2 - Q2*C1 - dU_eq*C1*C2) / (C1 + C2);

        p.setQ(Q1 - dQ0);
        n.setQ(Q2 + dQ0);
        return dQ0;
    }

    public static double chargeExchange(ICapacitor p, ICapacitor n, double dU_eq, double R) {
        if (R == 0) {
            return chargeExchange(p, n, dU_eq);
        }
        final double U1 = p.getPotential();
        final double U2 = n.getPotential();
        // 有效电势差U_valid
        final double Uv = U1 - U2 - dU_eq;
        if (p == n) {
            return Uv / R * 0.05;
        }
        final double C1 = p.getCapacity();
        final double C2 = n.getCapacity();
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
