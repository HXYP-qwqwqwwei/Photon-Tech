package photontech.utils.capability.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PtItemStackHandler extends ItemStackHandler {


    public PtItemStackHandler() {
        super();
    }

    public PtItemStackHandler(int size) {
        super(size);
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

}
