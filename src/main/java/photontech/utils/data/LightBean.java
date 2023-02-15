package photontech.utils.data;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class LightBean implements SaveLoadable {
    int intensity;
    float convergence;

    @Override
    public void load(CompoundNBT nbt) {

    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return null;
    }
}
