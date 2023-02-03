package photontech.utils.data.heat;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class PtHeatReservoir implements IHeatReservoir {

    private int heat;
    private float overloadTemp;
    private float capacity;
    private float heatTransferRate;

    public PtHeatReservoir() {
        this(298.15F, 1000, 1000, 0);
    }

    public PtHeatReservoir(float overloadTemp, float transferRate) {
        this(298.15F, overloadTemp, 1000, transferRate);
    }

    public PtHeatReservoir(float overloadTemp, float capacity, float transferRate) {
        this(298.15F, overloadTemp, capacity, transferRate);
    }

    public PtHeatReservoir(float initTemp, float overloadTemp, float capacity, float transferRate) {
        this.heat = (int) (initTemp * capacity);
        this.overloadTemp = overloadTemp;
        this.capacity = capacity;
        this.heatTransferRate = transferRate;
    }


    public boolean isOverloaded() {
        return this.getTemperature() > this.overloadTemp;
    }

    @Override
    public float getTemperature() {
        return this.getHeat() / this.getCapacity();
    }

    @Override
    public int extractHeat(int maxHeat, boolean simulate) {
        int extracted = 0;
        extracted = Math.max(extracted, maxHeat);
        extracted = Math.min(extracted, this.heat);
        if (!simulate) {
            this.setHeat(this.heat - extracted);
        }
        return extracted;
    }

    @Override
    public int acceptHeat(int maxHeat, boolean simulate) {
        int accepted = 0;
        accepted = Math.max(accepted, maxHeat);
        if (!simulate) {
            this.setHeat(this.heat + accepted);
        }
        return accepted;
    }

    @Override
    public int getHeat() {
        return this.heat;
    }

    @Override
    public void setHeat(int heat) {
        this.heat = heat;
    }

    @Override
    public float getCapacity() {
        return this.capacity;
    }

    @Override
    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    @Override
    public float getHeatTransferRate() {
        return this.heatTransferRate;
    }

    public void setHeatTransferRate(float rate) {
        this.heatTransferRate = rate;
    }

    public float getOverloadTemperature() {
        return this.overloadTemp;
    }

    public void setOverloadTemp(float overloadTemp) {
        this.overloadTemp = overloadTemp;
    }

    public void load(CompoundNBT nbt) {
        this.setCapacity(nbt.getFloat("Capacity"));
        this.setHeatTransferRate(nbt.getFloat("Rate"));
        this.setHeat(nbt.getInt("Heat"));
        this.setOverloadTemp(nbt.getFloat("OverloadTemp"));
    }


    @Nonnull
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("Heat", this.getHeat());
        nbt.putFloat("Capacity", this.getCapacity());
        nbt.putFloat("Rate", heatTransferRate);
        nbt.putFloat("OverloadTemp", overloadTemp);
        return nbt;
    }

}
