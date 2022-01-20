package photontech.utils.capability;

import net.minecraft.nbt.CompoundNBT;

public interface ISaveLoad {
    void load(CompoundNBT nbt);

    CompoundNBT save(CompoundNBT nbt);
}
