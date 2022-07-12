package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

public class KtRotateBody implements IRotateBody {

    public static final String INERTIA = "Inertia";
    public static final String OMEGA = "Omega";
    public static final String ANGLE = "Angle";
    public static final String LENGTH = "length";
//    public static final String VALID = "valid";
    public static final String LAST_UPDATE_TIME = "LastUpdateTime";
//    protected long inertia;
    protected float omega = 0F;
    protected float angle = 0;
    protected int length = 1;
//    protected boolean valid = true;

    public static KtRotateBody create(long inertia) {
        return new KtRotateBody(inertia);
    }


    public static KtRotateBody createFromNBT(CompoundNBT nbt) {
        KtRotateBody body = new KtRotateBody(0);
        if (nbt != null) {
            body.load(nbt);
        }
        return body;
    }

    protected KtRotateBody(long inertia) {
        if (inertia < 0) {
            inertia = INFINITY;
        }
//        this.inertia = inertia;
    }



//    @Override
//    public long getInertia() {
//        return inertia;
//    }

    @Override
    public float getOmega() {
        return omega;
    }

    @Override
    public void setOmega(float omega) {
        this.omega = omega;
    }

//    @Override
//    public void setInertia(long inertia) {
//        if (inertia < 0) {
//            inertia = INFINITY;
//        }
//        if (inertia > this.inertia) {
//            this.omega = this.inertia * this.omega / inertia;
//        }
//        this.inertia = inertia;
//    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
        this.formatAngle();
    }

//    @Override
//    public void addP(float p) {
//        float thisP = this.omega * this.inertia;
//        thisP += p;
//        this.omega = thisP / this.inertia;
//    }

    @Override
    public void updateAngle() {
        this.angle += omega * 0.05;
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

//    @Override
//    public int getKinetic() {
//        return (int) (0.5 * this.inertia * this.omega * this.omega);
//    }

    @Override
    public void reverse() {
        this.omega = -this.omega;
        this.angle = -this.angle;
    }

//    @Override
//    public boolean isValid() {
//        return valid;
//    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

//    @Override
//    public void invalidate() {
//        this.length = 0;
//        this.valid = false;
//    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
//        nbt.putLong(INERTIA, this.inertia);
        nbt.putFloat(OMEGA, this.omega);
        nbt.putFloat(ANGLE, this.angle);
        nbt.putInt(LENGTH, this.length);
//        nbt.putBoolean(VALID, this.valid);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
//        this.inertia = nbt.getLong(INERTIA);
        this.omega = nbt.getFloat(OMEGA);
        this.angle = nbt.getFloat(ANGLE);
        this.length = nbt.getInt(LENGTH);
//        this.valid = nbt.getBoolean(VALID);
    }
}
