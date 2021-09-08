package photontech.group;

import photontech.init.PtItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class PtNormalItemGroup extends ItemGroup {
    public PtNormalItemGroup() {
        super("PtItems");
    }

    @Override
    @Nonnull
    public ItemStack makeIcon() {
        return new ItemStack(PtItems.DIAMOND_INGOT.get());
    }
}
