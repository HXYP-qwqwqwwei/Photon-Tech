package photontech.item;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class PtCatalyzedItem extends PtRecipeToolItem {
    private final int maxDamage;

    public PtCatalyzedItem(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return maxDamage;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        // 损坏
        if (itemStack.hurt(1, new Random(), null)) {
            return ItemStack.EMPTY;
        }
        return itemStack;
    }
}
