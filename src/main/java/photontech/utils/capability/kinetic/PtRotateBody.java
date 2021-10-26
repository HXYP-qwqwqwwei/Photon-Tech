package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

public class PtRotateBody implements IRotateBody {

    protected double inertia;
    protected float omega = 0F;
    protected float angle = 0;

    public static PtRotateBody create(double inertia) {
        return new PtRotateBody(inertia);
    }

    public static PtRotateBody createFromNBT(CompoundNBT nbt) {
        PtRotateBody body = new PtRotateBody(0);
        if (nbt != null) {
            body.load(nbt);
        }
        return body;
    }

    protected PtRotateBody(double inertia) {
        if (inertia <= 0) {
            this.inertia = Float.MAX_VALUE;
        }
        else {
            this.inertia = inertia;
        }
    }



    @Override
    public double getInertia() {
        return inertia;
    }

    @Override
    public float getOmega() {
        return omega;
    }

    @Override
    public void setOmega(float omega) {
        this.omega = omega;
    }

    public void setInertia(double inertia) {
        if (inertia <= 0) {
            inertia = Float.MAX_VALUE;
        }
        this.inertia = inertia;
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
        this.formatAngle();
    }

    @Override
    public void updateAngle() {
        this.angle += omega;
        this.formatAngle();
    }

    private void formatAngle() {
        if (this.angle > DOUBLE_PI) {
            this.angle -= ((int) (angle / DOUBLE_PI)) * DOUBLE_PI;
        }
        if (this.angle < -DOUBLE_PI) {
            this.angle = -this.angle;
            this.angle -= ((int) (angle / DOUBLE_PI)) *  DOUBLE_PI;
            this.angle = -this.angle;
        }
    }

    @Override
    public int getKinetic() {
        return (int) (0.5 * this.inertia * this.omega * this.omega);
    }

    @Override
    public void reverse() {
        this.omega = -this.omega;
        this.angle = -this.angle;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("Inertia", this.inertia);
        nbt.putFloat("Omega", this.omega);
        nbt.putFloat("Angle", this.angle);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.inertia = nbt.getDouble("Inertia");
        this.omega = nbt.getFloat("Omega");
        this.angle = nbt.getFloat("Angle");
    }
}
