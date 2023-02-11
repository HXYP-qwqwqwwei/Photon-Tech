package photontech.utils.data.magnet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;

public class MagnetPole implements Magnet {
    public static final String FLUX_DENSITY = "FluxDensity";
    protected double fluxDensity;
    MagneticPoleType poleType;

    private MagnetPole(MagneticPoleType poleType, double fluxDensity) {
        this.poleType = poleType;
        this.fluxDensity = Math.max(0, fluxDensity);
    }

    public static Magnet create(MagneticPoleType pole, double fluxDensity) {
        return new MagnetPole(pole, fluxDensity);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.fluxDensity = nbt.getDouble(FLUX_DENSITY);
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble(FLUX_DENSITY, this.fluxDensity);
        return nbt;
    }

    @Override
    public double getFluxDensity(Direction side) {
        return fluxDensity * poleType.weight * (AxisHelper.isAxisPositiveDirection(side) ? 1 : -1);
    }

    @Override
    public void setFluxDensity(double fluxDensity) {
        this.fluxDensity = Math.max(0, fluxDensity);
    }

    @Override
    public MagneticPoleType getMagneticPole() {
        return this.poleType;
    }

    @Override
    public void setMagneticPole(MagneticPoleType pole) {
        this.poleType = pole;
    }
}
