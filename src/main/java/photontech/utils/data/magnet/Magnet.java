package photontech.utils.data.magnet;

import net.minecraft.util.Direction;
import photontech.utils.data.SaveLoadable;


public interface Magnet extends SaveLoadable {

    enum MagneticPoleType {
        N(1), S(-1);
        final int weight;
        MagneticPoleType(int weight) {
            this.weight = weight;
        }
    }

    Magnet EMPTY = MagnetPole.create(MagneticPoleType.N, 0);

    double getFluxDensity(Direction side);

    void setFluxDensity(double fluxDensity);

    MagneticPoleType getMagneticPole();

    void setMagneticPole(MagneticPoleType pole);

    default void magneticPoleReverse(Magnet m1, Magnet m2) {
        MagneticPoleType pole1 = m1.getMagneticPole();
        m1.setMagneticPole(m2.getMagneticPole());
        m2.setMagneticPole(pole1);
    }

}
