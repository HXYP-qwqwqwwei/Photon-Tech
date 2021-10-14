package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IRigidBody {

    IRigidBody EMPTY = PtRotateBody.create(0);
    double DOUBLE_PI = Math.PI * 2;

    float getOmega();

    void setOmega(float omega);

    int getKinetic();

    double getInertia();

    float getAngle();

    void setAngle(float angle);

    void updateAngle();

    CompoundNBT save(CompoundNBT nbt);

    void load(CompoundNBT nbt);

    void reverse();

//    static void kineticTransfer(IRigidBody r1, IRigidBody r2, float offsetAngle, boolean opposite) {
//        if (r1 == r2) {
//            return;
//        }
//        float w1 = r1.getOmega();
//        float w2 = r2.getOmega();
//        if (w1 > w2) {
//            double I1 = r1.getInertia();
//            double I2 = r2.getInertia();
//            float w_eq = MathHelper.sqrt((I1*w1*w1 + I2*w2*w2) / (I1 + I2));
//            // I1W1 + I2W2 = w(I1+I2)
//            r1.setOmega(w_eq);
//            r2.setOmega(w_eq * (opposite ? -1 : 0));
//            r2.setAngle(r1.getAngle() + offsetAngle);
//        }
//    }

    static void kineticTransfer(IRigidBody r1, IRigidBody r2, float offsetAngle, boolean opposite) {
        if (r1 == r2) {
            return;
        }
        if (opposite) {
            r2.reverse();
        }
        float w1 = r1.getOmega();
        float w2 = r2.getOmega();
        double I1 = r1.getInertia();
        double I2 = r2.getInertia();
        // I1w1 + I2w2 = w_eq(I1+I2)
        float w_eq = (float) ((I1*w1 + I2*w2) / (I1 + I2));

//        LogManager.getLogger().info("w1:" + w1);
//        LogManager.getLogger().info("w2:" + w2);
//        LogManager.getLogger().info("w_eq:" + w_eq);
        r1.setOmega(w_eq);
        r2.setOmega(w_eq);
        r2.setAngle(r1.getAngle() + offsetAngle);
        if (opposite) {
            r2.reverse();
        }
    }

    static void kineticTransfer(IRigidBody r1, IRigidBody r2) {
        kineticTransfer(r1, r2, 0, false);
    }
}
