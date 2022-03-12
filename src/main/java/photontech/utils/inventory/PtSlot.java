package photontech.utils.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PtSlot extends Slot {

    private boolean insertable = true;

    public PtSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.insertable;
    }

    public PtSlot setRemoveOnly() {
        this.insertable = false;
        return this;
    }

    public PtSlot setInsertable() {
        this.insertable = true;
        return this;
    }


}
