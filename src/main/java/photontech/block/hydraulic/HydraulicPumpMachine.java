package photontech.block.hydraulic;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.data.Hydraulic;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class HydraulicPumpMachine extends MachineTile {
    public static final String OUTPUT_PRESSURE = "OutputPressure";
    protected int outputPressure = 0;

    public HydraulicPumpMachine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract Direction getOutputSide();


    protected void setOutput(int pressure) {
        assert level != null;
        TileEntity te = level.getBlockEntity(worldPosition.relative(getOutputSide()));
        if (te instanceof HydraulicPipeTile) {
            HydraulicPipeTile pipe = (HydraulicPipeTile) te;
            if (pressure >= this.outputPressure) {
                pipe.pressurize(pressure);
            }
            else {
                pipe.depressurize();
                pipe.pressurize(pressure);
            }
            this.outputPressure = pressure;
        }
    }

    @Override
    public final void setRemoved() {
        if (isServerSide()) {
            this.setOutput(0);
        }
        super.setRemoved();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.HYDRAULIC_PIPE) {
            if (getOutputSide() == side) {
                return LazyOptional.of(() -> Hydraulic.PLACE_HOLDER).cast();
            }
        }
        return LazyOptional.empty();
    }


    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(OUTPUT_PRESSURE, outputPressure);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.outputPressure = nbt.getInt(OUTPUT_PRESSURE);
        super.load(state, nbt);
    }
}
