package photontech.block.electric.infiniteBattery;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.ElectricMachine;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.electric.ICapacitor;
import photontech.utils.helper.fuctions.PtPhysics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfiniteBatteryTile extends ElectricMachine {

    double voltage;
    public static final String VOLTAGE = "Voltage";

    public InfiniteBatteryTile(double voltage) {
        super(PtTileEntities.INFINITE_BATTERY.get());
        this.voltage = voltage;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
            if (side != null && side.getAxis() == facing.getAxis()) {
                return ICapacitor.PLACE_HOLDER.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
            TileEntity posTE = level.getBlockEntity(this.worldPosition.relative(facing));
            TileEntity negTE = level.getBlockEntity(this.worldPosition.relative(facing.getOpposite()));
            if (posTE == null || negTE == null) return;
            LazyOptional<ICapacitor> positive = posTE.getCapability(PtCapabilities.CONDUCTOR, facing.getOpposite());
            LazyOptional<ICapacitor> negative = negTE.getCapability(PtCapabilities.CONDUCTOR, facing);
            positive.ifPresent(p -> negative.ifPresent(n -> PtPhysics.chargeExchange(p, n, voltage)));
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
}
