package photontech.utils.capability.kinetic;

import photontech.utils.capability.ISaveLoad;

public interface IRotateBody extends ISaveLoad {

    double DOUBLE_PI = Math.PI * 2;
    long INFINITY = 0x3f3f3f3f3f3f3f3fL;

    float getOmega();

    void setOmega(float omega);

    int getKinetic();

    long getInertia();

    void setInertia(long inertia);

    float getAngle();

    void setAngle(float angle);

    void addP(float p);

    void updateAngle();

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

    static void kineticTransferWithEnv(IRotateBody body, double rate) {
        float w1 = body.getOmega();
        float w2 = 0;
        double I1 = body.getInertia();

        // I1w1 + I2w2 = w_eq(I1+I2)
        float w_eq = (float) ((I1*w1 + rate *w2) / (I1 + rate));

        body.setOmega(w_eq);
    }

    static void useConservationOfMomentum(IRotateBody b1, float r1, IRotateBody b2, float r2) {
        float v1 = b1.getOmega() * r1;
        float v2 = b2.getOmega() * r2;
        double I1 = b1.getInertia();
        double I2 = b2.getInertia();

        float v_eq = (float) ((I1*v1 + I2*v2) / (I1 + I2));

        b1.setOmega(v_eq / r1);
        b2.setOmega(v_eq / r2);

    }
}
