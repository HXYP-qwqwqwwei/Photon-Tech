package photontech.block.crucible;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.init.PtContainers;
import photontech.inventory.PtBaseContainer;
import photontech.inventory.PtSlotItemHandler;
import photontech.utils.capability.fluid.PtMultiFluidTank;

import javax.annotation.Nonnull;

public class PtCrucibleContainer extends PtBaseContainer {

    PtCrucibleTileEntity crucibleTileEntity;

    public PtCrucibleContainer(int id, PlayerInventory inventory, BlockPos pos, World world) {
        super(PtContainers.CRUCIBLE_CONTAINER.get(), id);
        this.tileEntity = world.getBlockEntity(pos);

        this.crucibleTileEntity = (PtCrucibleTileEntity) this.tileEntity;

        // input [0, 8)
        assert crucibleTileEntity != null;
        this.addSlotBox(crucibleTileEntity.getItemHandler(), 0, 2, 4, 27, 19);
        // output [8, 16)
        this.addSlotBox(crucibleTileEntity.getItemHandler(), 8, 2, 4, 27, 85, true);
        // catalyst[16] and cache[17]
        this.addSlot(new PtSlotItemHandler(crucibleTileEntity.getItemHandler(), 16, 86, 61));
        this.addSlot(new PtSlotItemHandler(crucibleTileEntity.getItemHandler(), 17, 105, 20).setRemoveOnly());

        // player inventory [18, 54)
        this.layoutPlayerInventorySlots(inventory, 6, 133);
    }



    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            ret = stackInSlot.copy();
            if (index >= 8 && index < 16) {
                if (!this.moveItemStackTo(stackInSlot, 18, 54, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, ret);
            } else if (index >= 18 && index < 54) {
                if (!this.moveItemStackTo(stackInSlot, 0, 8, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 18, 54, false)) {
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

    public float getTemperature() {
        return this.crucibleTileEntity.getHeatReservoir().getTemperature();
    }

    public int getHeat() {
        return this.crucibleTileEntity.getHeatReservoir().getHeat();
    }

    public float getCapacity() {
        return this.crucibleTileEntity.getHeatReservoir().getCapacity();
    }

    public float getOverloadTemperature() {
        return this.crucibleTileEntity.getHeatReservoir().getOverloadTemperature();
    }

    public float getHeatTransferRate() {
        return this.crucibleTileEntity.getHeatReservoir().getHeatTransferRate();
    }

    public PtMultiFluidTank getFluidTanks() {
        return this.crucibleTileEntity.getFluidTanks();
    }

    public float getMeltingProcess() {
        return this.crucibleTileEntity.getHeatCacheForMelting().getProcess();
    }

    public float getCoolingProcess() {
        return this.crucibleTileEntity.getHeatCacheForCooling().getProcess();
    }

    public float getOtherProcess() {
        return this.crucibleTileEntity.getHeatCacheForOther().getProcess();
    }
}
