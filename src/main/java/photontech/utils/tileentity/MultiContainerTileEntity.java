package photontech.utils.tileentity;

import net.minecraftforge.common.util.Constants;
import photontech.utils.data.item.PtItemStackHandler;
import photontech.utils.data.fluid.PtMultiFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MultiContainerTileEntity extends TileEntity implements IMultiTankTile, IItemHandlerTile {

    protected LazyOptional<PtItemStackHandler> mainItemHandler = LazyOptional.empty();
    protected LazyOptional<PtMultiFluidTank> fluidTanks = LazyOptional.empty();
    protected boolean isDirty = false;


    public MultiContainerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.mainItemHandler.ifPresent(handler -> handler.deserializeNBT(nbt.getCompound("Inventory")));
        this.fluidTanks.ifPresent(tank -> tank.load(nbt.getCompound("FluidTank")));
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.mainItemHandler.ifPresent(handler -> nbt.put("Inventory", handler.serializeNBT()));
        this.fluidTanks.ifPresent(tank -> nbt.put("FluidTank", tank.save(new CompoundNBT())));
        return nbt;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.mainItemHandler.cast();
        }
        else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.fluidTanks.cast();
        }
        return LazyOptional.empty();
    }

    // Networking

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        assert level != null;
        this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    // Getter
    @Override
    public PtMultiFluidTank getFluidTanks() {
        return this.fluidTanks.orElse(new PtMultiFluidTank());
    }

    @Override
    public PtItemStackHandler getItemHandler() {
        return this.mainItemHandler.orElse(new PtItemStackHandler());
    }

    public void updateIfDirty() {
        if (!isDirty || level == null || level.isClientSide) return;
        level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        this.setDirty(false);
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }


}
