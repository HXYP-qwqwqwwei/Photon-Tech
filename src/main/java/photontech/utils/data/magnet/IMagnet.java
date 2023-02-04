package photontech.utils.data.magnet;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import photontech.utils.data.ISaveLoad;


public interface IMagnet extends ISaveLoad {

    enum MagneticPoleType {
        N(1), S(-1);
        final int weight;
        MagneticPoleType(int weight) {
            this.weight = weight;
        }
    }

    IMagnet EMPTY = MagnetPole.create(MagneticPoleType.N, 0);

    double getFluxDensity(Direction side);

    void setFluxDensity(double fluxDensity);

    MagneticPoleType getMagneticPole();

    void setMagneticPole(MagneticPoleType pole);

//    /**
//     * 从from到to的磁感应强度
//     * @param from 起点（磁铁）
//     * @param to 终点
//     * @return B向量
//     */
//    default Vector3d getB(BlockPos from, BlockPos to) {
//        BlockPos vec3i = to.subtract(from);
//        Vector3d vecB = new Vector3d(vec3i.getX(), vec3i.getY(), vec3i.getZ());
//        double distance = vecB.length();
//        if (this.getMagneticPole() == MagneticPoleType.S) {
//            distance = -distance;
//        }
//        return vecB.normalize().scale(this.getFluxDensity() / (distance * distance * distance));
//    }

    default void magneticPoleReverse(IMagnet m1, IMagnet m2) {
        MagneticPoleType pole1 = m1.getMagneticPole();
        m1.setMagneticPole(m2.getMagneticPole());
        m2.setMagneticPole(pole1);
    }

}
