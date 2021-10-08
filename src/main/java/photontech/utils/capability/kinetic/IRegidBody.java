package photontech.utils.capability.kinetic;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IRegidBody {

    IRegidBody AIR = PtRotateBody.create(0, 0);

    float getOmega();

//    int getKinetic();
//
    double getInertia();

    void addForceFrom(IRegidBody from, double force);

    void deleteForceFrom(IRegidBody from);

    void update();

    @Nonnull
    Map<IRegidBody, Double> getForceMap();

    default void removeAllForces() {
        for (IRegidBody body : this.getForceMap().keySet()) {
            body.deleteForceFrom(this);
        }
    }

    CompoundNBT save(CompoundNBT nbt);

    void load(CompoundNBT nbt);

}
