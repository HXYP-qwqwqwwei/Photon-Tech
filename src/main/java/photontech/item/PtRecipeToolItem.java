package photontech.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import photontech.group.PtItemGroups;
import photontech.init.PtItems;

/**
 * 工具物品类
 * 这类物品在工作台合成的时候不会消耗
 */
public class PtRecipeToolItem extends Item {

    public PtRecipeToolItem() {
        super(new Properties().tab(PtItemGroups.NORMAL_ITEM_GROUP));
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
