package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

public interface IRotateBody {

    IRotateBody AIR = PtRotateBody.create(0);
    double DOUBLE_PI = Math.PI * 2;

    float getOmega();

    void setOmega(float omega);

    int getKinetic();

    double getInertia();

    void setInertia(double inertia);

    float getAngle();

    void setAngle(float angle);

    void updateAngle(long time);

    CompoundNBT save(CompoundNBT nbt);

    void load(CompoundNBT nbt);

    void reverse();

    static void kineticTransfer(IRotateBody r1, IRotateBody r2, boolean opposite) {
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

        r1.setOmega(w_eq);
        r2.setOmega(w_eq);
        if (opposite) {
            r2.reverse();
        }
    }

    static void kineticTransfer(IRotateBody r1, IRotateBody r2) {
        kineticTransfer(r1, r2, false);
    }

    static void synchronizeRotateAngle(IRotateBody reference, IRotateBody target, float offset) {
//        synchronizeRotateAngle(reference, target, offset, false);
    }

    static void synchronizeRotateAngle(IRotateBody reference, IRotateBody target, float offset, boolean reverse) {
        if (reverse) {
            target.setAngle(-reference.getAngle() - offset);
        }
        else {
            target.setAngle(reference.getAngle() + offset);
        }
    }

}
