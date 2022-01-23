package photontech.utils.capability.magnet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.logging.log4j.LogManager;
import photontech.utils.capability.ISaveLoad;


public interface IMagnet extends ISaveLoad {

    enum MagneticPole {
        N, S
    }

    /**
     * 表面磁感应强度B0
     * @return B0
     */
    double getB0();

    void setB0(double B0);

    MagneticPole getMagneticPole();

    void setMagneticPole(MagneticPole pole);

    /**
     * 从from到to的磁感应强度
     * @param from 起点（磁铁）
     * @param to 终点
     * @return B向量
     */
    default Vector3d getB(BlockPos from, BlockPos to) {
        BlockPos vec3i = to.subtract(from);
        Vector3d vecB = new Vector3d(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        double distance = vecB.length();
        if (this.getMagneticPole() == MagneticPole.S) {
            distance = -distance;
        }
        return vecB.normalize().scale(this.getB0() / (distance * distance * distance));
    }

    default void magneticPoleReverse(IMagnet m1, IMagnet m2) {
        MagneticPole pole1 = m1.getMagneticPole();
        m1.setMagneticPole(m2.getMagneticPole());
        m2.setMagneticPole(pole1);
    }

}
