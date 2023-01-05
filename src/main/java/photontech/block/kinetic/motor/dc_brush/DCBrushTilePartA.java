package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.kinetic.FullAxleTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.IEtCapacitor;
import photontech.utils.helperfunctions.AxisHelper;
import photontech.utils.helperfunctions.MutableDouble;
import photontech.utils.tileentity.IEtMachine;

import static net.minecraft.util.Direction.Axis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class DCBrushTilePartA extends FullAxleTile implements IEtMachine {
    public static final String BRUSH_AXIS = "BrushAxis";

    public MutableDouble I = new MutableDouble(0);
    public MutableDouble U = new MutableDouble(0);
    public Direction.Axis brushAxis = null;

    protected DCBrushTilePartB partB = null;

    public DCBrushTilePartA(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), initInertia, true);
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
            LazyOptional<IEtCapacitor> positive = posTE.getCapability(PtCapabilities.CONDUCTOR, nDirection);
            LazyOptional<IEtCapacitor> negative = negTE.getCapability(PtCapabilities.CONDUCTOR, pDirection);
            if (this.partBExist()) {
                positive.ifPresent(p -> negative.ifPresent(n -> {
                    this.U.value = p.getU() - n.getU();
                    // TODO 重写这部分的实现
//                    this.getMainBody().ifPresent(body -> {
//                        float omega = body.getOmega();
//                        double K = this.partB.getK(this.brushAxis);
//                        double R = this.partB.getR();
//                        double dU_eq = omega * K * 0.1;
//                        this.I.value = IEtCapacitor.chargeExchange(p, n, dU_eq, R);
//                        double F = this.I.value * K;
//                        body.setOmega(body.getOmega() + (float) (F * 0.05) / this.ktReferenceStatue.sumInertia);
//                    });
                }));
            }
        }
    }

    protected boolean partBExist() {
        assert this.level != null;
        TileEntity tileEntity = this.level.getBlockEntity(this.worldPosition.relative(this.getBlockState().getValue(FACING)));
        if (tileEntity instanceof DCBrushTilePartB) {
            if (tileEntity != this.partB) {
                this.partB = (DCBrushTilePartB) tileEntity;
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
                return IEtCapacitor.PLACE_HOLDER.cast();
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
