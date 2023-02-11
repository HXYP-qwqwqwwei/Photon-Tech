package photontech.utils.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class HydraulicPipe implements Hydraulic {
    int refCnt = 1;
    int pressure;
    FluidStack workingFluid = FluidStack.EMPTY;

    private HydraulicPipe() { }

    public static HydraulicPipe create() {
        return new HydraulicPipe();
    }

    @Override
    public void load(CompoundNBT nbt) {
        nbt.put(WORKING_FLUID, this.workingFluid.writeToNBT(new CompoundNBT()));
        this.refCnt = nbt.getInt(REF_CNT);
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        this.workingFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound(WORKING_FLUID));
        nbt.putInt(REF_CNT, this.refCnt);
        return nbt;
    }

    @Override
    public boolean isNoRef() {
        return refCnt == 0;
    }

    @Override
    public void plusRef() {
        refCnt += 1;
    }

    @Override
    public void minusRef() {
        if (this.refCnt == 0) {
            throw new RuntimeException("Dereference a non-ref Data");
        }
        refCnt -= 1;
    }


    @Override
    public int getPressure() {
        return pressure;
    }

    @Override
    public void pressurize(int pressure) {
        if (pressure >= this.pressure) {
            this.pressure = pressure;
        }
    }

    public void depressurize() {
        this.pressure = 0;
    }

    @Override
    public int getSize() {
        return refCnt * 16;
    }
}
