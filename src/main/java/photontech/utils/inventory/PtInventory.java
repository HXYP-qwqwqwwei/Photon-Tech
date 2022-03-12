package photontech.utils.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class PtInventory implements IInventory, IRecipeHelperPopulator {
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> listeners;

    public PtInventory(int numSlots) {
        this.slotsCount = numSlots;
        this.inventoryContents = NonNullList.withSize(numSlots, ItemStack.EMPTY);
    }

    public PtInventory(ItemStack... stacksIn) {
        this.slotsCount = stacksIn.length;
        this.inventoryContents = NonNullList.of(ItemStack.EMPTY, stacksIn);
    }

    public PtInventory(NonNullList<ItemStack> stacksList) {
        this.slotsCount = stacksList.size();
        this.inventoryContents = stacksList;
    }

    public PtInventory getSubInventory(int beginIndex, int endIndex) {
        NonNullList<ItemStack> subList = NonNullList.create();
        for (int index = beginIndex; index < endIndex; ++index) {
            subList.add(this.inventoryContents.get(index));
        }
        return new PtInventory(subList);
    }

    public NonNullList<ItemStack> getInventoryContents() {
        return inventoryContents;
    }

    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    public void addListener(IInventoryChangedListener listener) {
        if (this.listeners == null) {
            this.listeners = Lists.newArrayList();
        }

        this.listeners.add(listener);
    }

    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    public void removeListener(IInventoryChangedListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.inventoryContents.size() ? this.inventoryContents.get(index) : ItemStack.EMPTY;
    }

    public List<ItemStack> removeAllItems() {
        List<ItemStack> list = this.inventoryContents.stream().filter((p_233544_0_) -> {
            return !p_233544_0_.isEmpty();
        }).collect(Collectors.toList());
        this.clearContent();
        return list;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ItemStackHelper.removeItem(this.inventoryContents, index, count);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    public ItemStack removeItemType(Item p_223374_1_, int p_223374_2_) {
        ItemStack itemstack = new ItemStack(p_223374_1_, 0);

        for(int i = this.slotsCount - 1; i >= 0; --i) {
            ItemStack itemstack1 = this.getItem(i);
            if (itemstack1.getItem().equals(p_223374_1_)) {
                int j = p_223374_2_ - itemstack.getCount();
                ItemStack itemstack2 = itemstack1.split(j);
                itemstack.grow(itemstack2.getCount());
                if (itemstack.getCount() == p_223374_2_) {
                    break;
                }
            }
        }

        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    public ItemStack addItem(ItemStack stack) {
        ItemStack itemstack = stack.copy();
        this.moveItemToOccupiedSlotsWithSameType(itemstack);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.moveItemToEmptySlots(itemstack);
            return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
        }
    }

    public boolean canAddItem(ItemStack p_233541_1_) {
        boolean flag = false;

        for(ItemStack itemstack : this.inventoryContents) {
            if (itemstack.isEmpty() || this.isSameItem(itemstack, p_233541_1_) && itemstack.getCount() < itemstack.getMaxStackSize()) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.inventoryContents.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.inventoryContents.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int index, ItemStack stack) {
        this.inventoryContents.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return this.slotsCount;
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.inventoryContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void setChanged() {
        if (this.listeners != null) {
            for(IInventoryChangedListener iinventorychangedlistener : this.listeners) {
                iinventorychangedlistener.containerChanged(this);
            }
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    public void clearContent() {
        this.inventoryContents.clear();
        this.setChanged();
    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.inventoryContents) {
            helper.accountStack(itemstack);
        }

    }

    public String toString() {
        return this.inventoryContents.stream().filter((p_223371_0_) -> {
            return !p_223371_0_.isEmpty();
        }).collect(Collectors.toList()).toString();
    }

    private void moveItemToEmptySlots(ItemStack p_223375_1_) {
        for(int i = 0; i < this.slotsCount; ++i) {
            ItemStack itemstack = this.getItem(i);
            if (itemstack.isEmpty()) {
                this.setItem(i, p_223375_1_.copy());
                p_223375_1_.setCount(0);
                return;
            }
        }

    }

    private void moveItemToOccupiedSlotsWithSameType(ItemStack p_223372_1_) {
        for(int i = 0; i < this.slotsCount; ++i) {
            ItemStack itemstack = this.getItem(i);
            if (this.isSameItem(itemstack, p_223372_1_)) {
                this.moveItemsBetweenStacks(p_223372_1_, itemstack);
                if (p_223372_1_.isEmpty()) {
                    return;
                }
            }
        }

    }

    private boolean isSameItem(ItemStack p_233540_1_, ItemStack p_233540_2_) {
        return p_233540_1_.getItem() == p_233540_2_.getItem() && ItemStack.tagMatches(p_233540_1_, p_233540_2_);
    }

    private void moveItemsBetweenStacks(ItemStack p_223373_1_, ItemStack p_223373_2_) {
        int i = Math.min(this.getMaxStackSize(), p_223373_2_.getMaxStackSize());
        int j = Math.min(p_223373_1_.getCount(), i - p_223373_2_.getCount());
        if (j > 0) {
            p_223373_2_.grow(j);
            p_223373_1_.shrink(j);
            this.setChanged();
        }

    }

    public void read(ListNBT p_70486_1_) {
        for(int i = 0; i < p_70486_1_.size(); ++i) {
            ItemStack itemstack = ItemStack.of(p_70486_1_.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.addItem(itemstack);
            }
        }

    }

    public ListNBT write() {
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundNBT()));
            }
        }

        return listnbt;
    }
}
