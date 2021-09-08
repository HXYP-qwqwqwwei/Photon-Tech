package photontech.utils.recipe;

import net.minecraft.nbt.CompoundNBT;

public interface IPtSerializableRecipe{
    CompoundNBT saveToNBT(CompoundNBT nbt);

}
