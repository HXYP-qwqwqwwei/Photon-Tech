package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IRigidBody {

//    IRigidBody AIR = PtRotateBody.create(0, 0);

    float getOmega();

    void setOmega(float omega);

    int getKinetic();

    double getInertia();


    CompoundNBT save(CompoundNBT nbt);

    void load(CompoundNBT nbt);

    static void kineticTransfer(IRigidBody r1, IRigidBody r2) {
        float w1 = r1.getOmega();
        float w2 = r2.getOmega();
        if (w1 > w2) {
            double I1 = r1.getInertia();
            double I2 = r2.getInertia();
            float w_eq = MathHelper.sqrt((I1*w1*w1 + I2*w2*w2) / (I1 + I2));
            r1.setOmega(w_eq);
            r2.setOmega(w_eq);
        }
    }
}
