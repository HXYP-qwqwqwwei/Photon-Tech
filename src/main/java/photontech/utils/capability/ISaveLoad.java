package photontech.utils.capability;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public interface ISaveLoad {
    void load(CompoundNBT nbt);

    @Nonnull
    CompoundNBT save(CompoundNBT nbt);
}
