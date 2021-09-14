package photontech.utils.capability.kinetic;

import net.minecraft.util.math.MathHelper;

public class PtRotateBody implements IRotate {
    private float omega;
    private final float inertia;
    private int kinetic = 0;

    public PtRotateBody(float inertia) {
        if (inertia == 0) {
            this.inertia = Integer.MAX_VALUE;
        }
        else {
            this.inertia = inertia;
        }
    }

    public int extractKinetic(int maxKinetic, boolean simulate) {
        int extracted = Math.min(maxKinetic, this.kinetic);
        if (!simulate) {
            this.kinetic -= extracted;
        }
        return extracted;
    }

    public int acceptKinetic(int maxKinetic, boolean simulate) {
        int accepted = Math.max(maxKinetic, 0);
        if (!simulate) {
            this.kinetic += accepted;
        }
        return accepted;
    }

    @Override
    public int getKinetic() {
        return kinetic;
    }

    public float getInertia() {
        return inertia;
    }

    public float getOmega() {
        return MathHelper.sqrt(this.kinetic / this.inertia);
    }
}
