package photontech.block.heater.solid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.init.PtContainers;
import photontech.inventory.PtBaseContainer;
import photontech.inventory.PtSlotItemHandler;

import javax.annotation.Nonnull;

public class PtHeaterContainer extends PtBaseContainer {

    PtHeaterTileEntity heaterTileEntity;

    public PtHeaterContainer(int id, PlayerInventory inventory, BlockPos pos, World world) {
        super(PtContainers.HEATER_CONTAINER.get(), id);
        this.tileEntity = world.getBlockEntity(pos);
        this.heaterTileEntity = (PtHeaterTileEntity) this.tileEntity;

        assert heaterTileEntity != null;

        this.addSlot(new PtSlotItemHandler(heaterTileEntity.getItemHandler(), 0, 49, 33));
        this.addSlot(new PtSlotItemHandler(heaterTileEntity.getItemHandler(), 1, 109, 33).setRemoveOnly());

        // Player inventory [2,38)
        this.layoutPlayerInventorySlots(inventory, 7, 83);

    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            ret = stackInSlot.copy();
            if (index == 1) {
                if (!this.moveItemStackTo(stackInSlot, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, ret);
            } else if (index >= 2 && index < 38) {
                if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == ret.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stackInSlot);
        }

        return ret;
    }

    public float getBurningProcess() {
        return this.heaterTileEntity.getHeatCacheForBurning().getProcess();
    }

    public boolean isBurning() {
        return this.heaterTileEntity.getHeatCacheForBurning().isInProcess();
    }

}
