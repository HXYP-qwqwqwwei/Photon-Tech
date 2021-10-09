package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class PtRotateBody implements IRigidBody {

    private double inertia;
    private float omega = 0F;

    public static PtRotateBody create(double inertia) {
        return new PtRotateBody(inertia);
    }

    private PtRotateBody(double inertia) {
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
    public int getKinetic() {
        return (int) (0.5 * this.inertia * this.omega * this.omega);
    }


    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("Inertia", this.inertia);
        nbt.putFloat("Omega", this.omega);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.inertia = nbt.getDouble("Inertia");
        this.omega = nbt.getFloat("Omega");
    }
}
