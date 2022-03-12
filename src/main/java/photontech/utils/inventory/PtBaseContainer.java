package photontech.utils.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;


public class PtBaseContainer extends Container {
    protected TileEntity tileEntity;

    protected PtBaseContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity playerIn) {
        if (this.tileEntity == null || this.tileEntity.isRemoved()) {
            return false;
        }
        // 在有效的时候发送方块更新以保持同步
        Objects.requireNonNull(tileEntity.getLevel()).sendBlockUpdated(tileEntity.getBlockPos(), tileEntity.getBlockState(), tileEntity.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        return !this.isDistanceOutOfRange(this.tileEntity, playerIn);
    }


    protected void addSlotLine(IItemHandler itemHandler, int beginIndex, int len, int xPos, int yPos, boolean removeOnly) {
        for (int i = 0, index = beginIndex; i < len; ++i, ++index) {
            PtSlotItemHandler slot = new PtSlotItemHandler(itemHandler, index, xPos, yPos);
            if (removeOnly) {
                slot.setRemoveOnly();
            }
            this.addSlot(slot);
            xPos += 18;
        }
    }

    protected void addSlotBox(IItemHandler itemHandler, int beginIndex, int nRow, int nCol, int xPos, int yPos, boolean removeOnly) {
        for (int row = 0, index = beginIndex; row < nRow; ++row, index += nCol) {
            this.addSlotLine(itemHandler, index, nCol, xPos, yPos, removeOnly);
            yPos += 18;
        }
    }

    protected void addSlotBox(IItemHandler itemHandler, int beginIndex, int nRow, int nCol, int xPos, int yPos) {
        this.addSlotBox(itemHandler, beginIndex, nRow, nCol, xPos, yPos, false);
    }

    protected void layoutPlayerInventorySlots(PlayerInventory inventory, int xPos, int yPos) {
        // Player inventory
        addSlotBox(new ItemStackHandler(inventory.items), 9, 3, 9, xPos, yPos);

        // Hotbar
        yPos += 58;
        addSlotBox(new ItemStackHandler(inventory.items), 0, 1, 9, xPos, yPos);
    }

    protected final boolean isDistanceOutOfRange(TileEntity tileEntity, PlayerEntity playerEntity) {
        return isDistanceOutOfRange(tileEntity, playerEntity, 64D);
    }

    protected final boolean isDistanceOutOfRange(TileEntity tileEntity, PlayerEntity playerEntity, double maxDis) {
        return tileEntity.getBlockPos().distSqr(playerEntity.blockPosition()) > maxDis;
    }

//    @Override
//    public void setData(int p_75137_1_, int p_75137_2_) {
//        super.setData(p_75137_1_, p_75137_2_);
//        Objects.requireNonNull(tileEntity.getLevel()).sendBlockUpdated(tileEntity.getBlockPos(), tileEntity.getBlockState(), tileEntity.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
//    }
}
