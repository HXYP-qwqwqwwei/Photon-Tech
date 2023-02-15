package photontech.utils.helper.fuctions;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import photontech.block.kinetic.KineticMachine;
import photontech.block.kinetic.gears.GearTile;
import photontech.block.magnet.MagnetTile;
import photontech.init.PtCapabilities;
import photontech.utils.data.electric.ElectricCapacitor;
import photontech.utils.data.magnet.Magnet;

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

    // U = (Q1+Q2)/(C1+C2)
    // dQ = Q1 - UC1 = UC2 - Q2
    public static void chargeTransfer(ElectricCapacitor p, ElectricCapacitor n, double I) {
        p.setCharge(p.getCharge() - I * 0.05);
        n.setCharge(n.getCharge() + I * 0.05);
    }

    public static double getMagnetFlux(@Nullable TileEntity te, Direction side) {
        if (te == null) return 0;
        if (te instanceof MagnetTile) {
            return ((MagnetTile) te).getMagnetPole(side).getFluxDensity(side);
        }
        else return te.getCapability(PtCapabilities.MAGNET, side).orElse(Magnet.EMPTY).getFluxDensity(side);
    }

//    public static double cauculateConvergence(Vector3d injectVec, )

    public static double maintainVoltage(ElectricCapacitor p, ElectricCapacitor n, double voltage) {
        if (p == n) {
            return Double.POSITIVE_INFINITY;
        }
        final double C1 = p.getCapacity();
        final double C2 = n.getCapacity();
        final double Q1 = p.getCharge();
        final double Q2 = n.getCharge();

        double dQ0 = (Q1*C2 - Q2*C1 - voltage*C1*C2) / (C1 + C2);

        p.setCharge(Q1 - dQ0);
        n.setCharge(Q2 + dQ0);
        return dQ0;
    }

    public static double maintainVoltage(ElectricCapacitor p, ElectricCapacitor n, double voltage, double R) {
        if (R == 0) {
            return maintainVoltage(p, n, voltage);
        }
        final double U1 = p.getPotential();
        final double U2 = n.getPotential();
        // 有效电势差U_valid
        final double Uv = U1 - U2 - voltage;
        if (p == n) {
            return Uv / R * 0.05;
        }
        final double C1 = p.getCapacity();
        final double C2 = n.getCapacity();
        final double Q1 = p.getCharge();
        final double Q2 = n.getCharge();

        double dQ0 = (Q1*C2 - Q2*C1 - voltage*C1*C2) / (C1 + C2);
        double dQdt = Uv / R * 0.05;
        dQ0 = dQ0 > 0 ? Math.min(dQdt, dQ0) : Math.max(dQdt, dQ0);

        p.setCharge(Q1 - dQ0);
        n.setCharge(Q2 + dQ0);
        return dQ0 * 20;
    }
}
