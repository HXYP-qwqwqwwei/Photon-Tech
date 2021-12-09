package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

public class PtRotateBody implements IRotateBody {

    protected long inertia;
    protected float omega = 0F;
    protected float angle = 0;
    protected long lastUpdateTime = 0L;

    public static PtRotateBody create(long inertia) {
        return new PtRotateBody(inertia);
    }

    public static PtRotateBody createFromNBT(CompoundNBT nbt) {
        PtRotateBody body = new PtRotateBody(0);
        if (nbt != null) {
            body.load(nbt);
        }
        return body;
    }

    protected PtRotateBody(long inertia) {
        if (inertia < 0) {
            inertia = INFINITY;
        }
        this.inertia = inertia;
//        else {
//            this.inertia = inertia;
//        }
    }



    @Override
    public long getInertia() {
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

    @Override
    public void setInertia(long inertia) {
        if (inertia < 0) {
            inertia = INFINITY;
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
    public void updateAngle(long time, int dtMilliseconds) {
        if (time != this.lastUpdateTime) {
            this.angle += omega * dtMilliseconds * 0.001;
            this.lastUpdateTime = time;
            this.formatAngle();
        }
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
        nbt.putLong("Inertia", this.inertia);
        nbt.putFloat("Omega", this.omega);
        nbt.putFloat("Angle", this.angle);
        nbt.putLong("LastUpdateTime", this.lastUpdateTime);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.inertia = nbt.getLong("Inertia");
        this.omega = nbt.getFloat("Omega");
        this.angle = nbt.getFloat("Angle");
        this.lastUpdateTime = nbt.getLong("LastUpdateTime");
    }
}
