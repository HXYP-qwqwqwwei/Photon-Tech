package photontech.block.kinetic.motor.dcbrush;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.kinetic.ResistType;
import photontech.block.kinetic.motor.ActiveKineticMachine;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.electric.ICapacitor;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.helper.MutableDouble;
import photontech.utils.helper.fuctions.PtPhysics;
import photontech.utils.tileentity.IEtMachine;

import static net.minecraft.util.Direction.Axis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class DCBrushMotorCommutatorTile extends ActiveKineticMachine implements IEtMachine {
    public static final String BRUSH_AXIS = "BrushAxis";

    public MutableDouble I = new MutableDouble(0);
    public MutableDouble U = new MutableDouble(0);
    public Direction.Axis brushAxis = null;

    protected DCBrushMotorCoilTile coil = null;

    public DCBrushMotorCommutatorTile(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), initInertia, ResistType.NORMAL_MACHINE);
    }


    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide()) {
            if (this.brushAxis == null) {
                return;
            }

            Direction pDirection = AxisHelper.getAxisPositiveDirection(this.brushAxis);
            Direction nDirection = pDirection.getOpposite();

            TileEntity posTE = level.getBlockEntity(this.worldPosition.relative(pDirection));
            TileEntity negTE = level.getBlockEntity(this.worldPosition.relative(nDirection));
            if (posTE == null || negTE == null) return;
            LazyOptional<ICapacitor> positive = posTE.getCapability(PtCapabilities.CONDUCTOR, nDirection);
            LazyOptional<ICapacitor> negative = negTE.getCapability(PtCapabilities.CONDUCTOR, pDirection);
            if (this.isCoilExist()) {
                positive.ifPresent(p -> negative.ifPresent(n -> {
                    double U = p.getPotential() - n.getPotential();
                    double Kt = this.coil.getKt();
                    double R = this.coil.getR();
                    float av = this.getAngularVelocity();

                    double I = (U - Kt * av) / R;
                    PtPhysics.chargeTransfer(p, n, I);

                    int force = (int) (I * Kt * this.getFrequency());
                    this.setOutput(force);
                }));
            }
        }
    }

    protected boolean isCoilExist() {
        assert this.level != null;
        TileEntity tileEntity = this.level.getBlockEntity(this.worldPosition.relative(this.getBlockState().getValue(FACING)));
        if (tileEntity instanceof DCBrushMotorCoilTile) {
            if (tileEntity != this.coil) {
                this.coil = (DCBrushMotorCoilTile) tileEntity;
                coil.setBrushAxis(brushAxis);
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            if (side != null && side.getAxis() == this.brushAxis) {
                return ICapacitor.PLACE_HOLDER.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        if (this.brushAxis != null) {
            nbt.putString(BRUSH_AXIS, this.brushAxis.getName());
        }
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.brushAxis = Axis.byName(nbt.getString(BRUSH_AXIS));
    }

    @Override
    public double getU() {
        return U.value;
    }

    @Override
    public double getI() {
        return I.value;
    }

    public void setBrushAxis(Axis brushAxis) {
        this.brushAxis = brushAxis;
    }

    public Axis getBrushAxis() {
        return brushAxis;
    }
}
