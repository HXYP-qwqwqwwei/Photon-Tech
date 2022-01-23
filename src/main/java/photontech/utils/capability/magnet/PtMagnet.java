package photontech.utils.capability.magnet;

import net.minecraft.nbt.CompoundNBT;

public class PtMagnet implements IMagnet {
    protected double B0;
    MagneticPole pole;

    private PtMagnet(MagneticPole pole, double B0) {
        this.pole = pole;
        this.B0 = B0;
    }

    public static IMagnet create(MagneticPole pole, double B0) {
        return new PtMagnet(pole, B0);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.B0 = nbt.getDouble("B0");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("B0", this.B0);
        return nbt;
    }

    @Override
    public double getB0() {
        return B0;
    }

    @Override
    public void setB0(double B0) {
        this.B0 = B0;
    }

    @Override
    public MagneticPole getMagneticPole() {
        return this.pole;
    }

    @Override
    public void setMagneticPole(MagneticPole pole) {
        this.pole = pole;
    }
}
