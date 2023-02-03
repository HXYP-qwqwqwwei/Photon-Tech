package photontech.utils.data.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import photontech.utils.data.ISaveLoad;

import java.util.List;

public interface IMultiFluidTank extends IFluidHandler, ISaveLoad {

    void setSize(int size);

    void setCapacity(int capacity);

    int getFluidAmount(Fluid fluid);

    int matchTankIndex(Fluid fluid);

    void getAllFluids(List<Fluid> listIn);

    int getSumAmount();

    int getEmptyTankIndex();

    boolean isIndexEnd(int index);

    int fillIntoTank(int toTank, Fluid fluid, int amount, IFluidHandler.FluidAction action);

    void setFluidInEmptyTank(int toTank, Fluid fluid, int amount);

    FluidStack drainFromTank(int fromTank, Fluid fluid, int maxDrain, IFluidHandler.FluidAction action);

    static void fluidExchange(IMultiFluidTank from, IMultiFluidTank to) {
        for (int fromIndex = 0; fromIndex < from.getTanks(); ++fromIndex) {

            FluidStack fromExist = from.getFluidInTank(fromIndex);
            if (fromExist.isEmpty()) {
                continue;
            }
            Fluid fromFluid = fromExist.getFluid();
            int fromAmount = fromExist.getAmount();
            int transferAmount = fromAmount / 2;
            int toIndex = to.matchTankIndex(fromExist.getFluid());

            if (to.isIndexEnd(toIndex)) {
                toIndex = to.getEmptyTankIndex();
                if (!to.isIndexEnd(toIndex)) {
                    to.setFluidInEmptyTank(toIndex, fromFluid, transferAmount);
                    fromExist.setAmount(fromAmount - transferAmount);
                }
                continue;
            }

            naturalFluidTransfer(fromExist, to.getFluidInTank(toIndex));

        }

    }

    static void naturalFluidTransfer(final FluidStack from, final FluidStack to) {
        if (!from.isEmpty() && from.isFluidEqual(to)) {
            int dV = from.getAmount() - to.getAmount();
            int transferAmount = dV > 0 ? (dV + 1) / 2 : 0;
            from.setAmount(from.getAmount() - transferAmount);
            to.setAmount(to.getAmount() + transferAmount);
        }
    }

}
