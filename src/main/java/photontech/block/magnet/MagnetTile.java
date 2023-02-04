package photontech.block.magnet;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.data.magnet.IMagnet;
import photontech.utils.data.magnet.MagnetPole;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;
import static photontech.utils.PtConstants.BlockStateProperties.REVERSED;

public abstract class MagnetTile extends MachineTile {
    public static final String NORTH_POLE = "NorthPole";
    public static final String SOUTH_POLE = "SouthPole";
    protected IMagnet northPole;
    protected IMagnet southPole;

    public MagnetTile(TileEntityType<?> tileEntityTypeIn, double magneticFluxDensity) {
        super(tileEntityTypeIn);
        this.northPole = MagnetPole.create(IMagnet.MagneticPoleType.N, magneticFluxDensity);
        this.southPole = MagnetPole.create(IMagnet.MagneticPoleType.S, magneticFluxDensity);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.MAGNET && side != null) {
            if (this.getAxis() == side.getAxis()) {
                return LazyOptional.of(() -> this.getMagnetPole(side)).cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    public Direction.Axis getAxis() {
        return this.getBlockState().getValue(AXIS);
    }

    public IMagnet getMagnetPole(Direction side) {
        boolean reversed = this.getBlockState().getValue(REVERSED);
        boolean positive = AxisHelper.isAxisPositiveDirection(side);
        return positive ^ reversed ? this.northPole : this.southPole;
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        nbt.put(NORTH_POLE, this.northPole.save(new CompoundNBT()));
        nbt.put(SOUTH_POLE, this.southPole.save(new CompoundNBT()));
        return super.save(nbt);
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.northPole.load(nbt.getCompound(NORTH_POLE));
        this.southPole.load(nbt.getCompound(SOUTH_POLE));
        super.load(state, nbt);
    }
}
