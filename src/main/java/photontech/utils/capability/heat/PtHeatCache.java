package photontech.utils.capability.heat;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class PtHeatCache extends PtHeatReservoir implements IHeatCache {

    private int requiredTransitionHeat;
    private float transitionTemperature;
    private int surplusHeat = 0;

    public PtHeatCache() {
        this(298.15F);
    }

    public PtHeatCache(float temperature) {
        this.setHeat((int) (temperature * this.getCapacity()));
    }


    public void init(float transitionTemperature, int requiredTransitionHeat) {
        this.transitionTemperature = transitionTemperature;
        this.requiredTransitionHeat = requiredTransitionHeat;
        this.setHeat((int) (transitionTemperature * this.getCapacity()));
        this.surplusHeat = 0;
    }


    @Override
    public int extractHeat(int maxHeat, boolean simulate) {
        int extracted = 0;
        extracted = Math.max(extracted, maxHeat);
        if (this.requiredTransitionHeat > 0) {
            extracted = Math.min(extracted, this.surplusHeat);
        }
        else {
            extracted = Math.min(extracted, this.surplusHeat - this.requiredTransitionHeat);
        }
        if (!simulate) {
            this.setSurplusHeat(this.surplusHeat - extracted);
        }
        return extracted;
    }

    @Override
    public int acceptHeat(int maxHeat, boolean simulate) {
        int accepted = 0;
        accepted = Math.max(accepted, maxHeat);
        if (this.requiredTransitionHeat > 0) {
            accepted = Math.min(accepted, this.requiredTransitionHeat - this.surplusHeat);
        }
        else {
            accepted = Math.min(accepted, -this.surplusHeat);
        }
        if (!simulate) {
            this.setSurplusHeat(this.surplusHeat + accepted);
        }
        return accepted;
    }

    @Override
    public int getSurplusHeat() {
        return this.surplusHeat;
    }

    public void setSurplusHeat(int surplusHeat) {
        this.surplusHeat = surplusHeat;
    }

    @Override
    public float getProcess() {
        if (this.requiredTransitionHeat == 0) {
            return 0;
        }
        return Math.max(0.0F, 1.0F * this.surplusHeat / requiredTransitionHeat);
    }

    public boolean isProcessDone() {
        return this.surplusHeat == this.requiredTransitionHeat;
    }

    public boolean isInProcess() {
        return !this.isProcessDone();
    }

    @Override
    public float getHeatTransferRate() {
        return 10000F;
    }

    @Override
    public void load(CompoundNBT nbt) {
        super.load(nbt);
        this.requiredTransitionHeat = nbt.getInt("TransitionHeat");
        this.transitionTemperature = nbt.getFloat("TransitionTemperature");
        this.surplusHeat = nbt.getInt("SurplusHeat");
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("TransitionHeat", this.requiredTransitionHeat);
        nbt.putFloat("TransitionTemperature", this.transitionTemperature);
        nbt.putInt("SurplusHeat", this.surplusHeat);
        return nbt;
    }

    @Override
    public float getCapacity() {
        return 1000F;
    }

    public int getRequiredTransitionHeat() {
        return requiredTransitionHeat;
    }
}
