package photontech.block.magnet.permanent;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.magnet.MagnetTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.helper_functions.AxisHelper;

import static photontech.utils.PtConstants.BlockStateProperties.*;
import static net.minecraft.state.properties.BlockStateProperties.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PermanentMagnetTile extends MagnetTile {

    public PermanentMagnetTile(double B0) {
        super(PtTileEntities.PERMANENT_MAGNET.get(), B0);
    }

    @Override
    public void tick() {

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.MAGNET && side != null) {
            BlockState state = this.getBlockState();
            Direction.Axis axis = state.getValue(AXIS);
            if (axis == side.getAxis()) {
                boolean reversed = state.getValue(REVERSED);
                boolean positive = AxisHelper.isAxisPositiveDirection(side);
                return positive ^ reversed ? this.NPole.cast() : this.SPole.cast();
            }
        }
        return super.getCapability(cap, side);
    }

}
