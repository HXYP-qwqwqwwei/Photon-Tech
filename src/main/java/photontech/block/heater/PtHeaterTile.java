package photontech.block.heater;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PtHeaterTile extends PtMachineTile {

    private Direction heatOutputSide;

    public PtHeaterTile(TileEntityType<?> tileEntityTypeIn, Direction heatOutputSide) {
        super(tileEntityTypeIn);
        this.heatOutputSide = heatOutputSide;
        this.heatReservoir = LazyOptional.of(() -> this.createHeatReservoir(ENVIRONMENT_TEMPERATURE, 10000, 100, 100F));
    }

    public void setHeatOutputSide(Direction side) {
        this.heatOutputSide = side;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.HEAT_RESERVOIR) {
            if (side == heatOutputSide) {
                return this.heatReservoir.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }
}
