package photontech.group;

import photontech.init.PtItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class PtBlockGroup extends ItemGroup {
    public PtBlockGroup() {
        super("Phonto Tech: Blocks");
    }

    @Override
    @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(PtItems.DIAMOND_INGOT.get());
    }
}
