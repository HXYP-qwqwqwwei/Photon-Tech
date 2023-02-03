package photontech.utils.data.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

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
