package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.PtElectricMachineTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.capability.electric.PtMutableConductor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class PtWireTile extends PtElectricMachineTile {
    protected LazyOptional<IMutableConductor> conductor;


    public PtWireTile(double capacity, double resistance) {
        super(PtTileEntities.WIRE.get());
        this.conductor = LazyOptional.of(() -> PtMutableConductor.create(capacity, resistance));
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            this.chargeExchangeByDirections(this.getValidDirections());
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            return this.conductor.cast();
        }
        else return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.saveCap(this.conductor, "Conductor", nbt);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.loadCap(this.conductor, "Conductor", nbt);
    }

    public Direction[] getValidDirections() {
        return Arrays.stream(Direction.values()).filter(direction -> this.getBlockState().getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction))).toArray(Direction[]::new);
    }
}
