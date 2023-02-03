package photontech.block.crucible;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import photontech.init.PtContainers;
import photontech.utils.inventory.PtBaseContainer;
import photontech.utils.data.fluid.PtMultiFluidTank;

import javax.annotation.Nonnull;

public class PtCrucibleContainer extends PtBaseContainer {

    PtCrucibleTileEntity crucibleTileEntity;

    public PtCrucibleContainer(int id, PlayerInventory inventory, BlockPos pos, World world) {
        super(PtContainers.CRUCIBLE_CONTAINER.get(), id);
        this.tileEntity = world.getBlockEntity(pos);

        this.crucibleTileEntity = (PtCrucibleTileEntity) this.tileEntity;

        assert crucibleTileEntity != null;
        // I/O [0, 9)
        this.addSlotBox(crucibleTileEntity.getItemHandler(), 0, 3, 3, 68, 41);
        // Catalyze [9, 10)
        this.addSlotBox(crucibleTileEntity.getItemHandler(), 9, 1, 1, 31, 85);

        // player inventory [10, 46)
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
            if (index < 9) {
                if (!this.moveItemStackTo(stackInSlot, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, ret);
            } else if (index == 9) {
                if (!this.moveItemStackTo(stackInSlot, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index < 46) {
                if (!this.moveItemStackTo(stackInSlot, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
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
