package photontech.utils.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import photontech.init.PtCapabilities;
import photontech.init.PtRecipes;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.heat.PtHeatCache;
import photontech.utils.capability.heat.PtHeatReservoir;
import photontech.utils.capability.item.PtIOLimitedItemHandler;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.recipe.PtConditionalRecipe;
import photontech.utils.recipe.RecipeCondition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PtMachineTile extends PtMultiContainerTileEntity implements ITickableTileEntity, IHeatReservoirTile {

    protected LazyOptional<PtIOLimitedItemHandler> itemIOHandler = LazyOptional.empty();
    protected LazyOptional<PtIOLimitedItemHandler> recipeIOHandler = LazyOptional.empty();
    protected LazyOptional<PtHeatReservoir> heatReservoir = LazyOptional.empty();
    protected List<PtConditionalRecipe> cachedRecipes;
    protected List<PtHeatCache> heatCaches;

    // Kinetic
//    protected LazyOptional<PtRotateBody> eastRotateBody = LazyOptional.empty();
//    protected LazyOptional<PtRotateBody> southRotateBody = LazyOptional.empty();
//    protected LazyOptional<PtRotateBody> westRotateBody = LazyOptional.empty();
//    protected LazyOptional<PtRotateBody> northRotateBody = LazyOptional.empty();
//    protected LazyOptional<PtRotateBody> upRotateBody = LazyOptional.empty();
//    protected LazyOptional<PtRotateBody> downRotateBody = LazyOptional.empty();

    public PtMachineTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // RECIPE
    protected PtConditionalRecipe getConditionalRecipe(String group, Predicate<PtConditionalRecipe> recipeFilter, @Nullable Comparator<PtConditionalRecipe> comparator) {
        assert level != null;
        Stream<PtConditionalRecipe> stream = level.getRecipeManager()
                .getAllRecipesFor(PtRecipes.Types.CONDITIONAL_TYPE)
                .stream()
                .filter(recipe -> recipe.getGroup().equals(group))
                .filter(recipeFilter);
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }
        List<PtConditionalRecipe> recipes = stream.limit(1).collect(Collectors.toList());
        if (!recipes.isEmpty()) {
            return recipes.get(0);
        }
        return null;
    }

    protected boolean handleRecipeInput(PtConditionalRecipe recipe, int inputSlotBegin, int inputSlotEnd, int inputTankBegin, int inputTankEnd) {
        if (recipe == null) {
            return false;
        }
        return recipe.extractAllInput(this.getRecipeIOHandler(), inputSlotBegin, inputSlotEnd, this.getFluidTanks(), inputTankBegin, inputTankEnd);
    }

    protected void handleRecipeResult(PtConditionalRecipe recipe) {

        if (recipe == null) {
            return;
        }

        if (!recipe.getOutputFluids().isEmpty()) {
            for (FluidStack fluidStack : recipe.getOutputFluids()) {
                this.getFluidTanks().fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            }
        }
        if (!recipe.getOutputItems().isEmpty()) {
            for (ItemStack itemStack : recipe.getOutputItems()) {
                ItemStack remainItem = ItemHandlerHelper.insertItem(this.getRecipeIOHandler(), itemStack.copy(), false);
                if (!remainItem.isEmpty()) {
                    assert level != null;
                    Block.popResource(level, this.worldPosition.offset(0.5, 0.5, 0.5), remainItem);
                }
            }
        }
    }

    /**
     *
     * @param recipe
     * @param heatCache
     * @return whether this recipe is still in process
     */
    protected boolean startHeatRecipeProcess(PtConditionalRecipe recipe, PtHeatCache heatCache) {
        if (recipe == null) {
            return false;
        }
        RecipeCondition condition = recipe.getCondition();
        float rate = condition.rate < 0 ? heatCache.getHeatTransferRate() : condition.rate;

        IHeatReservoir.heatExchange(this.getHeatReservoir(), heatCache, rate);

        if (heatCache.isProcessDone()) {
            initHeatProcess(heatCache, condition);
            return false;
        }
        return true;
    }

    protected void updateCachedRecipe(String group, int cacheIndex, int inputSlotBegin, int inputSlotEnd, int inputTankBegin, int inputTankEnd, @Nullable Comparator<PtConditionalRecipe> comparator) {
        PtConditionalRecipe newRecipe = this.getConditionalRecipe(
                group,
                recipe -> recipe.testInput(this.getItemIOHandler(), inputSlotBegin, inputSlotEnd, this.getFluidTanks(), inputTankBegin, inputTankEnd),
                comparator
        );

        if (newRecipe != null) {
            if (!newRecipe.equals(this.cachedRecipes.get(cacheIndex))) {
                this.cachedRecipes.set(cacheIndex, newRecipe);
                this.initHeatProcess(this.heatCaches.get(cacheIndex), newRecipe.getCondition());
            }
        }
        else {
            this.heatCaches.get(cacheIndex).init(9999, 0);
            this.cachedRecipes.set(cacheIndex, null);
        }
    }

    protected void initHeatProcess(PtHeatCache heatCache, RecipeCondition condition) {
        heatCache.init(condition.temperature, condition.heat);
    }


    protected void saveCachedRecipe(CompoundNBT nbt, PtConditionalRecipe recipe, String key) {
        if (recipe != null) {
            nbt.put(key, recipe.saveToNBT(new CompoundNBT()));
        }
    }

    // HELP FUNCTIONS (SERVER ONLY)
    protected boolean isFullyOpenAir() {
        BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY() + 1, this.worldPosition.getZ());
        assert level != null;
        while (level.getBlockState(pos).getMaterial() == Material.AIR) {
            if (pos.getY() >= 255) {
                return true;
            }
            pos.setY(pos.getY() + 1);
        }
        return false;
    }

    protected boolean isUnderSunshine() {
        BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY() + 1, this.worldPosition.getZ());
        assert level != null;
        while (level.getBlockState(pos).getMaterial() == Material.AIR || level.getBlockState(pos).getMaterial() == Material.GLASS) {
            if (pos.getY() >= 255) {
                return true;
            }
            pos.setY(pos.getY() + 1);
        }
        return false;
    }


    // NETWORKING
    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.heatReservoir.ifPresent(reservoir -> nbt.put("HeatReservoir", reservoir.saveToNBT(new CompoundNBT())));
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.heatReservoir.ifPresent(reservoir -> reservoir.loadFromNBT(nbt.getCompound("HeatReservoir")));
    }


    // GETTER
    public PtIOLimitedItemHandler getItemIOHandler() {
        return itemIOHandler.orElse(new PtIOLimitedItemHandler(this.getItemHandler()));
    }

    public PtIOLimitedItemHandler getRecipeIOHandler() {
        return recipeIOHandler.orElse(new PtIOLimitedItemHandler(this.getItemHandler()));
    }

    @Override
    public PtHeatReservoir getHeatReservoir() {
        return this.heatReservoir.orElse(new PtHeatReservoir());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.HEAT_RESERVOIR) {
            return this.heatReservoir.cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.itemIOHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
