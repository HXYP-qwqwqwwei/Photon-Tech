package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

public class PtVariableRotateBody implements IRotateBody {
    private IRotateBody body;

    private PtVariableRotateBody(PtRotateBody body) {
        this.body = body;
    }

    public static PtVariableRotateBody of(PtRotateBody body) {
        return new PtVariableRotateBody(body);
    }

    public IRotateBody get() {
        return this.body;
    }

    public void set(IRotateBody newBody) {
        this.body = newBody;
    }

    @Override
    public float getOmega() {
        return body.getOmega();
    }

    @Override
    public void setOmega(float omega) {
        body.setOmega(omega);
    }

    @Override
    public int getKinetic() {
        return body.getKinetic();
    }

    @Override
    public long getInertia() {
        return body.getInertia();
    }

    @Override
    public float getAngle() {
        return body.getAngle();
    }

    @Override
    public void setAngle(float angle) {
        body.setAngle(angle);
    }

    @Override
    public void updateAngle(long tick, int dTMilliseconds) {
        body.updateAngle(tick, dTMilliseconds);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return body.save(nbt);
    }

    @Override
    public void load(CompoundNBT nbt) {
        body.load(nbt);
    }

    @Override
    public void reverse() {
        body.reverse();
    }

    @Override
    public void setInertia(long inertia) {
        body.setInertia(inertia);
    }
}
