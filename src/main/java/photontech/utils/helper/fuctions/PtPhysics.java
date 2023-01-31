package photontech.utils.helper.fuctions;

import photontech.block.kinetic.KineticMachine;
import photontech.block.kinetic.gears.GearTile;

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
}
