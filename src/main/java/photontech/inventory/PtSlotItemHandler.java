package photontech.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PtSlotItemHandler extends SlotItemHandler {

    private boolean insertable = true;

    public PtSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.insertable;
    }

    public PtSlotItemHandler setRemoveOnly() {
        this.insertable = false;
        return this;
    }

    public PtSlotItemHandler setInsertable() {
        this.insertable = true;
        return this;
    }

}
