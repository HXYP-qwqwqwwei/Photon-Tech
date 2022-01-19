package photontech.block.electric.infiniteBattery;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.PtElectricMachineTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.capability.electric.InfiniteCapacitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtInfiniteBatteryTile extends PtElectricMachineTile {

    protected LazyOptional<IMutableConductor> positive;
    protected LazyOptional<IMutableConductor> negative;

    public PtInfiniteBatteryTile(double voltage) {
        super(PtTileEntities.INFINITE_BATTERY.get());
        this.positive = LazyOptional.of(() -> InfiniteCapacitor.create(voltage / 2));
        this.negative = LazyOptional.of(() -> InfiniteCapacitor.create(-voltage / 2));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
            if (side == facing) {
                return this.positive.cast();
            }
            else if (side == facing.getOpposite()) {
                return this.negative.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            Direction direction = this.getBlockState().getValue(BlockStateProperties.FACING);
            Direction[] validDirections = {direction, direction.getOpposite()};
            this.chargeExchangeByDirections(validDirections);
        }

    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.saveCap(this.positive, "Positive", nbt);
        this.saveCap(this.negative, "Negative", nbt);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.loadCap(this.positive, "Positive", nbt);
        this.loadCap(this.negative, "Negative", nbt);
    }
}
