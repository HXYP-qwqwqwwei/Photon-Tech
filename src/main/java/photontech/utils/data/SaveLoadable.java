package photontech.utils.data;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public interface SaveLoadable {
    void load(CompoundNBT nbt);

    @Nonnull
    CompoundNBT save(CompoundNBT nbt);
}
