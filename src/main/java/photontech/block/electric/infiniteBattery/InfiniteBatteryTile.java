package photontech.block.electric.infiniteBattery;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.electric.ElectricCapacitor;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.helper.fuctions.PtPhysics;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static photontech.utils.PtConstants.BlockStateProperties.REVERSED;

public class InfiniteBatteryTile extends MachineTile {

    double voltage;
    public static final String VOLTAGE = "Voltage";

    public InfiniteBatteryTile(double voltage) {
        super(PtTileEntities.INFINITE_BATTERY.get());
        this.voltage = voltage;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            boolean reversed = this.getBlockState().getValue(REVERSED);
            Direction.Axis axis = this.getBlockState().getValue(BlockStateProperties.AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            if (reversed) positiveSide = positiveSide.getOpposite();

            TileEntity posTE = level.getBlockEntity(this.worldPosition.relative(positiveSide));
            TileEntity negTE = level.getBlockEntity(this.worldPosition.relative(positiveSide.getOpposite()));
            if (posTE == null || negTE == null) return;
            LazyOptional<ElectricCapacitor> positive = posTE.getCapability(PtCapabilities.CONDUCTOR, positiveSide.getOpposite());
            LazyOptional<ElectricCapacitor> negative = negTE.getCapability(PtCapabilities.CONDUCTOR, positiveSide);
            positive.ifPresent(p -> negative.ifPresent(n -> PtPhysics.maintainVoltage(p, n, voltage)));
        }

    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putDouble(VOLTAGE, this.voltage);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.voltage = nbt.getDouble(VOLTAGE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            Direction.Axis axis = this.getBlockState().getValue(BlockStateProperties.AXIS);
            if (side != null && side.getAxis() == axis) {
                return ElectricCapacitor.PLACE_HOLDER.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

}
