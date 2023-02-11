package photontech.block.kinetic.brake;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.block.hydraulic.HydraulicPipeTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.Hydraulic;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BrakePadControllerTile extends MachineTile {
    public static final String CONNECTED = "Connected";
    public static final String PUSHED = "Pushed";
    public static final String OUTPUT_RESIST = "OutputResist";

    protected boolean connected = false;
    protected boolean pushed = false;
    protected int outputResist;

    public BrakePadControllerTile() {
        super(PtTileEntities.BRAKE_PAD_CONTROLLER.get());
    }

    @Override
    public void tick() {
        if (isServerSide()) {
            assert level != null;
            HydraulicPipeTile pipe = getPipeTile();
            boolean canConnect = pipe != null;
            if (this.connected ^ canConnect) {
                this.connected = canConnect;
                this.setUpdateFlag(true);
            }
            if (connected) {
                assert pipe != null;
                int pressure = pipe.getPressure();
                this.applyPressure(pressure);
            }
            this.updateIfDirty();
        }
    }

    protected void applyPressure(int pressure) {
        this.outputResist = pressure >> 4;
        if (pushed ^ outputResist > 0) {
            this.pushed = outputResist > 0;
            this.setUpdateFlag(true);
        }
    }

    public boolean isPushed() {
        return pushed;
    }

    public int getOutputResist() {
        return outputResist;
    }

    public Direction getBackSide() {
        return getFacing().getOpposite();
    }

    @Nullable
    protected HydraulicPipeTile getPipeTile() {
        assert level != null;
        TileEntity te = level.getBlockEntity(worldPosition.relative(getBackSide()));
        if (te instanceof HydraulicPipeTile && ((HydraulicPipeTile) te).isValidSide(getFacing())) {
            return (HydraulicPipeTile) te;
        }
        return null;
    }

    public Direction getFacing() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.HYDRAULIC_PIPE) {
            if (getBackSide() == side) {
                return LazyOptional.of(() -> Hydraulic.PLACE_HOLDER).cast();
            }
        }
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putBoolean(CONNECTED, this.connected);
        nbt.putBoolean(PUSHED, this.pushed);
        nbt.putInt(OUTPUT_RESIST, this.outputResist);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.connected = nbt.getBoolean(CONNECTED);
        this.pushed = nbt.getBoolean(PUSHED);
        this.outputResist = nbt.getInt(OUTPUT_RESIST);
    }

    public boolean isConnected() {
        return connected;
    }

}
