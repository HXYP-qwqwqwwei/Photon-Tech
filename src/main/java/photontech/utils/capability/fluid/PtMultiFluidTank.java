package photontech.utils.capability.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class PtMultiFluidTank implements IMultiFluidTank {

    protected NonNullList<FluidStack> tanks;
    protected Predicate<FluidStack> validator;
    protected int capacity;
    private int size;

    public PtMultiFluidTank() {
        this(1000);
    }

    public PtMultiFluidTank(int capacity) {
        this(1, capacity);
    }

    public PtMultiFluidTank(int size, int capacity) {
        this.size = size;
        initTanks();
        this.capacity = capacity;
    }

    public PtMultiFluidTank(PtMultiFluidTank other) {
        this.tanks = other.tanks;
        this.validator = other.validator;
        this.capacity = other.capacity;
        this.size = other.size;
    }

    private void initTanks() {
        this.tanks = NonNullList.withSize(size, FluidStack.EMPTY);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
        initTanks();
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    @Override
    public int getEmptyTankIndex() {
        int tankIndex = 0;
        for (; tankIndex < size; ++tankIndex) {
            if (tanks.get(tankIndex).isEmpty()) {
                break;
            }
        }
        return tankIndex;
    }

    @Override
    public boolean isIndexEnd(int index) {
        return index >= getTanks();
    }

    public int getCapacity() {
        return getTankCapacity(0);
    }


    @Override
    public int matchTankIndex(Fluid fluid) {
        assert fluid != Fluids.EMPTY;

        int tankIndex = 0;

        while (tankIndex < this.size) {
            FluidStack stack = tanks.get(tankIndex);
            if (stack.getFluid() == fluid) {
                break;
            }
            tankIndex += 1;
        }
        return tankIndex;
    }

    @Override
    public int getTanks() {
        return this.size;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tanks.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return isFluidValid(stack);
    }

    public boolean isFluidValid(@Nonnull FluidStack stack) {
        return true;
    }

    @Override
    public int getSumAmount() {
        int amount = 0;
        for (int i = 0; i < this.size; ++i) {
            FluidStack stack = tanks.get(i);
            if (!stack.isEmpty()) {
                amount += stack.getAmount();
            }
        }
        return amount;
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        int tankIndex = matchTankIndex(resource.getFluid());
        if (isIndexEnd(tankIndex) && isIndexEnd(tankIndex = getEmptyTankIndex())) {
            return 0;
        }

        return fillIntoTank(tankIndex, resource.getFluid(), resource.getAmount(), action);

    }

    @Override
    public int fillIntoTank(int toTank, Fluid fluid, int amount, IFluidHandler.FluidAction action) {
        int remainSpace = this.getCapacity() - this.getSumAmount();
        int fillInAmount = Math.min(remainSpace, amount);
        int storedAmount = this.tanks.get(toTank).getAmount();

        if (action.execute()) {
            this.tanks.set(toTank, new FluidStack(fluid, fillInAmount + storedAmount));
        }

        this.onContentsChanged();
        return fillInAmount;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        for (int tankIndex = 0; tankIndex < size; ++tankIndex) {
            FluidStack stack = this.tanks.get(tankIndex);
            if (!stack.isEmpty()) {
                return drainFromTank(tankIndex, stack.getFluid(), maxDrain, action);
            }
        }
        return FluidStack.EMPTY;
    }



    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        int tankIndex = this.matchTankIndex(resource.getFluid());
        if (isIndexEnd(tankIndex)) {
            return FluidStack.EMPTY;
        }

        return drainFromTank(tankIndex, resource.getFluid(), resource.getAmount(), action);
    }

    /**
     * Drain fluid from a tank
     * @param fromTank - tank will be drained from.
     * @param fluid - which Fluid will be drained.
     * @param maxDrain - maximum amount of fluid to drain.
     * @param action - If SIMULATE, drain will only be simulated.
     * @return same as drain()
     */
    @Override
    public FluidStack drainFromTank(int fromTank, Fluid fluid, int maxDrain, IFluidHandler.FluidAction action) {
        FluidStack storedFluidStack = this.tanks.get(fromTank);
        int storedAmount = storedFluidStack.getAmount();

        if (storedAmount <= maxDrain) {
            if (action.simulate()) {
                return storedFluidStack.copy();
            }
            this.tanks.set(fromTank, FluidStack.EMPTY);
            this.onContentsChanged();
            return new FluidStack(fluid, storedAmount);
        }
        else if (action.simulate()) {
            return new FluidStack(fluid, maxDrain);
        }

        storedFluidStack.setAmount(storedAmount - maxDrain);
        this.onContentsChanged();
        return new FluidStack(fluid, maxDrain);
    }


    @Override
    public int getFluidAmount(Fluid fluid) {
        int tankIndex = matchTankIndex(fluid);
        if (isIndexEnd(tankIndex)) {
            return this.tanks.get(tankIndex).getAmount();
        }
        return 0;
    }


    @Override
    public void getAllFluids(List<Fluid> listIn) {
        for (FluidStack stack : tanks) {
            if (!stack.isEmpty()) {
                listIn.add(stack.getFluid());
            }
        }
    }


    public PtMultiFluidTank readFromNBT(CompoundNBT nbt) {

        int size = nbt.getInt("Size");
        if (this.size != size) {
            this.setSize(size);
        }
        for (int i = 0; i < size; ++i) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt.getCompound("Tank_" + i));
            tanks.set(i, fluidStack);
        }
        return this;
    }


    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        int size = tanks.size();
        nbt.putInt("Size", size);
        for (int i = 0; i < size; ++i) {
            nbt.put("Tank_" + i, tanks.get(i).writeToNBT(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void setFluidInEmptyTank(int toTank, Fluid fluid, int amount) {
        if (tanks.get(toTank).isEmpty()) {
            tanks.set(toTank, new FluidStack(fluid, amount));
        }
        this.onContentsChanged();
    }


    protected void onContentsChanged() {
        this.tanks.sort(Comparator.comparingInt((FluidStack f) -> f.getFluid().getFluid().getAttributes().getDensity()).reversed());
    }
}
