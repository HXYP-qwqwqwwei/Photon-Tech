package photontech.utils.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import photontech.init.PtFluids;

public interface IFluidTankBlock {

    class VariableBoolean {
        private boolean isTrue = false;

        public void setTrue() {
            isTrue = true;
        }

        public boolean isTrue() {
            return isTrue;
        }
    }

    default boolean useWithMilkBucket(World worldIn, BlockPos pos, PlayerEntity player, Hand hand) {

        final VariableBoolean result = new VariableBoolean();

        if (player.getItemInHand(hand).getItem() == Items.MILK_BUCKET) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(tank -> {
                    FluidStack fluidStack = new FluidStack(PtFluids.MILK_FLUID.get(), 1000);
                    int sim = tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);
                    if (sim == 1000) {
                        tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        if (!player.isCreative()) {
                            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                        }

                        result.setTrue();
                    }
                });
            }
        }
        return result.isTrue();
    }

    default boolean tryGetMilkBucket(World worldIn, BlockPos pos, PlayerEntity player, Hand hand) {

        final VariableBoolean result = new VariableBoolean();
        ItemStack holdStack = player.getItemInHand(hand);

        if (holdStack.getItem() == Items.BUCKET) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(tank -> {
                    FluidStack fluidStack = tank.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                    if (fluidStack.getAmount() == 1000 && fluidStack.getFluid() == PtFluids.MILK_FLUID.get()) {
                        tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                        if (!player.isCreative()) {
                            holdStack.setCount(holdStack.getCount() - 1);
                            player.setItemInHand(hand, holdStack);
                            ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
                            if (!player.addItem(milkBucket)) {
                                Block.popResource(worldIn, new BlockPos(player.getPosition(0.5F)), milkBucket);
                            }
                        }
                        result.setTrue();
                    }
                });
            }
        }
        return result.isTrue();
    }

}
