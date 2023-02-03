package photontech.utils.data.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class PtIOLimitedItemHandler implements IItemHandler {

    PtItemStackHandler handler;

    Predicate<Integer> insertable = (slot) -> true;
    Predicate<Integer> extractable = (slot) -> true;

    public PtIOLimitedItemHandler(PtItemStackHandler handler) {
        this.handler = handler;
    }

    public PtIOLimitedItemHandler setInsertOK(Predicate<Integer> predicate) {
        this.insertable = predicate;
        return this;
    }

    public PtIOLimitedItemHandler setExtractOK(Predicate<Integer> predicate) {
        this.extractable = predicate;
        return this;
    }


    @Override
    public int getSlots() {
        return this.handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!insertable.test(slot)) {
            return stack;
        }
        return this.handler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!extractable.test(slot)) {
            return ItemStack.EMPTY;
        }
        return this.handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.handler.getSlotLimit(slot);
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.handler.isItemValid(slot, stack);
    }
}
