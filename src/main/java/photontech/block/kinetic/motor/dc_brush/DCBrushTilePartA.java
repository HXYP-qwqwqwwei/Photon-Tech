package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.EtTransmissionLine;
import photontech.utils.capability.electric.IEtCapacitor;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.helper.AxisHelper;
import photontech.utils.helper.MutableDouble;
import photontech.utils.tileentity.IChargeExchange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static photontech.utils.PtConstants.BlockStateProperties.*;
import static net.minecraft.state.properties.BlockStateProperties.*;

public class DCBrushTilePartA extends AxleTile implements IChargeExchange {
    public MutableDouble I = new MutableDouble(0);
    public Direction.Axis electricValidAxis;

    protected LazyOptional<IEtCapacitor> positive = LazyOptional.of(() -> EtTransmissionLine.create(10.0, 0.0));
    protected LazyOptional<IEtCapacitor> negative = LazyOptional.of(() -> EtTransmissionLine.create(10.0, 0.0));
    protected DCBrushTilePartB partB = null;

    public DCBrushTilePartA(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), initInertia);
    }


    @Override
    public void tick() {
        super.tick();
        if (level != null && !level.isClientSide()) {
            if (electricValidAxis == null) {
                this.updateElectricValidAxis();
            }
            Direction pDirection = AxisHelper.getAxisPositiveDirection(this.electricValidAxis);
            Direction nDirection = pDirection.getOpposite();
            positive.ifPresent(p -> this.chargeExchangeWithTile(p, level.getBlockEntity(this.worldPosition.relative(pDirection)), nDirection));
            negative.ifPresent(n -> this.chargeExchangeWithTile(n, level.getBlockEntity(this.worldPosition.relative(nDirection)), pDirection));
            if (this.partBExist()) {
                positive.ifPresent(p -> {
                    negative.ifPresent(n -> {
                        double dU = p.getU() - n.getU();
                        double B = partB.getB(this.electricValidAxis);
                        double L = partB.getWireLength();
                        double K = B * L;
                        this.getMainBody().ifPresent(body -> {
                            float omega = body.getOmega();
                            double I = (dU - K * omega / 1024) / partB.getR();
                            double F = I * K;
                            float accelerate = (float) (F / body.getInertia());
                            body.setOmega(omega + accelerate * 0.05F);
                            this.I.value = I;
                        });
                        p.setQ(p.getQ() - this.I.value * 0.05);
                        n.setQ(n.getQ() + this.I.value * 0.05);
//                        double dq = IPtCapacitor.chargeExchange(p.get(), n.get(), partB.getR());
//                        dq = dq == 0.0 ? -IPtCapacitor.chargeExchange(n.get(), p.get(), partB.getR()) : dq;
//                        this.I.value = dq / 0.05;
                    });
                });
            }
            else this.I.value = 0;
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

    protected void updateElectricValidAxis() {
        boolean axisRotated = this.getBlockState().getValue(AXIS_ROTATED);
        switch (this.getAxis()) {
            case X:
                this.electricValidAxis = axisRotated ? Direction.Axis.Y : Direction.Axis.Z;
                break;
            case Z:
                this.electricValidAxis = axisRotated ? Direction.Axis.Y : Direction.Axis.X;
                break;
            default:
                this.electricValidAxis = axisRotated ? Direction.Axis.X : Direction.Axis.Z;
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            this.updateElectricValidAxis();
            if (side != null && side.getAxis() == this.electricValidAxis) {
                return side == AxisHelper.getAxisPositiveDirection(this.electricValidAxis) ? this.positive.cast() : this.negative.cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
