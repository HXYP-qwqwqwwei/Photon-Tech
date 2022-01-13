package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.PtElectricTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.capability.electric.IPtCapacitor;
import photontech.utils.capability.electric.PtMutableConductor;
import photontech.utils.helper.MutableDouble;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtWireTile extends PtElectricTile {
    protected LazyOptional<IMutableConductor> conductor;


    public PtWireTile(double resistance, double capacity) {
        super(PtTileEntities.WIRE.get());
        this.conductor = LazyOptional.of(() -> PtMutableConductor.create(resistance, capacity));
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.getBlockState();
            this.exchangeCharge(state, Direction.values());
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            if (side == null || this.getBlockState().getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                return this.conductor.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }
}
