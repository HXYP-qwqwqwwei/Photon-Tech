package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class PtRotateBody implements IRegidBody {

    private double inertia;
    private float omega = 0F;
    private double C_D;
    private int kinetic = 0;

    private final Map<IRegidBody, Double> forces = new HashMap<>();

    public static PtRotateBody create(double inertia, double C_D) {
        return new PtRotateBody(inertia, C_D);
    }

    public static PtRotateBody create(double inertia) {
        return new PtRotateBody(inertia, 0.1);
    }

    private PtRotateBody(double inertia, double C_D) {
        if (inertia <= 0) {
            this.inertia = Float.MAX_VALUE;
        }
        else {
            this.inertia = inertia;
        }
        this.C_D = Math.max(0, C_D);
    }

    @Override
    public void addForceFrom(IRegidBody from, double force) {
        if (from == this) {
            return;
        }
        forces.put(from, force);
    }

    @Override
    public void deleteForceFrom(IRegidBody from) {
        forces.remove(from);
    }

    private double getResultantForce() {
        double sum = 0;
        for (Double force : forces.values()) {
            sum += force;
        }
        return sum;
    }

    @Override
    public void update() {

        LogManager.getLogger().info(this.getResultantForce());

        this.omega += this.getResultantForce() / this.inertia;
        double airForce = -this.C_D * this.omega * Math.abs(this.omega) * 0.5;
        this.addForceFrom(AIR, airForce);
    }


    @Override
    public double getInertia() {
        return inertia;
    }

    @Override
    public float getOmega() {
        return omega;
    }

    public void setOmega(float omega) {
        this.omega = omega;
    }

    public void setInertia(double inertia) {
        if (inertia <= 0) {
            inertia = Float.MAX_VALUE;
        }
        this.inertia = inertia;
    }

    public void combine(IRegidBody target) {
        if (target instanceof PtRotateBody) {
            this.removeAllForces();
            this.setOmega(0);
            this.setInertia(this.getInertia() + target.getInertia());
            this.update();
        }
    }

    public void depart(IRegidBody target) {
        if (target instanceof PtRotateBody) {
            this.removeAllForces();
            this.setOmega(0);
            this.setInertia(this.getInertia() - target.getInertia());
            this.update();
        }
    }

    @Nonnull
    public Map<IRegidBody, Double> getForceMap() {
        return forces;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("Inertia", this.inertia);
        nbt.putFloat("Omega", this.omega);
        nbt.putDouble("C_D", this.C_D);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.inertia = nbt.getDouble("Inertia");
        this.omega = nbt.getFloat("Omega");
        this.C_D = nbt.getDouble("C_D");
    }
}
