package photontech.utils.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import photontech.init.PtRecipes;
import photontech.utils.PtNBTUtils;
import photontech.utils.Utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PtConditionalRecipe implements IRecipe<IInventory>, IPtSerializableRecipe {

//    public static final ResourceLocation LOCATION = new ResourceLocation(Utils.MOD_ID, "crucible");

    protected final List<Ingredient> inputItems = new ArrayList<>();
    protected final List<FluidStack> inputFluids = new ArrayList<>();

    protected final List<ItemStack> outputItems = new ArrayList<>();
    protected final List<FluidStack> outputFluids = new ArrayList<>();

    protected final RecipeCondition condition;

    protected float xp;

    protected boolean catalyzable;

    protected final ResourceLocation recipeId;

    protected final String group;

    protected PtConditionalRecipe(ResourceLocation recipeId, String group,
                                  RecipeCondition condition,
                                  float xp,
                                  List<Ingredient> inputItems,
                                  List<FluidStack> inputFluids,
                                  List<ItemStack> outputItems,
                                  List<FluidStack> outputFluids) {

        this.recipeId = recipeId;
        this.group = group;

        this.condition = condition;
        this.xp = Math.max(0.0F, xp);

        if (inputItems != null) {
            this.inputItems.addAll(inputItems);
        }
        if (inputFluids != null) {
            this.inputFluids.addAll(inputFluids);
        }
        if (outputItems != null) {
            this.outputItems.addAll(outputItems);
        }
        if (outputFluids != null) {
            this.outputFluids.addAll(outputFluids);
        }
        trim();
    }

    private void trim() {

        ((ArrayList<Ingredient>) this.inputItems).trimToSize();
        ((ArrayList<FluidStack>) this.inputFluids).trimToSize();

        ((ArrayList<ItemStack>) this.outputItems).trimToSize();
        ((ArrayList<FluidStack>) this.outputFluids).trimToSize();
    }

    // region GETTERS
    public List<Ingredient> getInputItems() {

        return inputItems;
    }

    public List<FluidStack> getInputFluids() {

        return inputFluids;
    }

    public List<ItemStack> getOutputItems() {

        return outputItems;
    }

    public List<FluidStack> getOutputFluids() {

        return outputFluids;
    }



    public float getXp() {

        return xp;
    }

    public boolean isCatalyzable() {

        return catalyzable;
    }

    public boolean testCondition(RecipeCondition condition) {
        return this.condition.testAllCondition(condition);
    }

    public boolean testInputItems(IItemHandler items, int begin, int end) {
        mainLoop: for (Ingredient ingredient : this.inputItems) {
            for (int i = begin; i < end; ++i) {
                if (ingredient.test(items.getStackInSlot(i))) {
                    continue mainLoop;
                }
            }
            return false;
        }
        return true;
    }

    public boolean testInputFluids(IFluidHandler fluids, int begin, int end) {
        mainLoop: for(FluidStack fluidStack : this.inputFluids) {
            for (int i = begin; i < end; ++i) {
                FluidStack fluidInTank = fluids.getFluidInTank(i);
                if (fluidStack.isFluidEqual(fluidInTank) && fluidInTank.getAmount() >= fluidStack.getAmount()) {
                    continue mainLoop;
                }
            }
            return false;
        }
        return true;
    }

    public boolean test(RecipeCondition condition,
                        IItemHandler items, int slotBegin, int slotEnd,
                        IFluidHandler fluids, int tankBegin, int tankEnd) {
        return this.testCondition(condition) &&
                this.testInputItems(items, slotBegin, slotEnd) &&
                this.testInputFluids(fluids, tankBegin, tankEnd);
    }

    public boolean testInput(IItemHandler items, int slotBegin, int slotEnd,
                             IFluidHandler fluids, int tankBegin, int tankEnd) {
        return this.testInputItems(items, slotBegin, slotEnd) && this.testInputFluids(fluids, tankBegin, tankEnd);
    }



    public RecipeCondition getCondition() {
        return condition;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return true;
    }

    @Override
    public ItemStack assemble(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PtRecipes.CONDITIONAL_RECIPE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return PtRecipes.Types.CONDITIONAL_TYPE;
    }

    @Nonnull
    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PtConditionalRecipe recipe = (PtConditionalRecipe) o;
        return recipeId.equals(recipe.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId);
    }

    @Override
    public CompoundNBT saveToNBT(CompoundNBT nbt) {
        nbt.put("Condition", this.condition.writeToNBT(new CompoundNBT()));
        nbt.putFloat("Xp", this.xp);
        nbt.putString("Group", this.group);
        nbt.putString("RecipeId", this.recipeId.toString());
        nbt.put("InputItems", PtNBTUtils.saveIngredientsToNBT(this.inputItems, new CompoundNBT()));
        nbt.put("InputFluids", PtNBTUtils.saveFluidsToNBT(this.inputFluids, new CompoundNBT()));
        nbt.put("OutputItems", PtNBTUtils.saveItemsToNBT(this.outputItems, new CompoundNBT()));
        nbt.put("OutputFluids", PtNBTUtils.saveFluidsToNBT(this.outputFluids, new CompoundNBT()));
        return nbt;
    }

    public static PtConditionalRecipe loadFromNBT(CompoundNBT nbt) {
        if (nbt.isEmpty()) {
            return null;
        }
        RecipeCondition condition = RecipeCondition.loadFromNBT(nbt.getCompound("Condition"));
        float xp = nbt.getFloat("Xp");
        String group = nbt.getString("Group");
        ResourceLocation recipeId = new ResourceLocation(nbt.getString("RecipeId"));
        List<Ingredient> inputItems = NonNullList.create();
        List<FluidStack> inputFluids = NonNullList.create();
        List<ItemStack> outputItems = NonNullList.create();
        List<FluidStack> outputFluids = NonNullList.create();
        PtNBTUtils.loadIngredientsFromNBT(inputItems, nbt.getCompound("InputItems"));
        PtNBTUtils.loadFluidsFromNBT(inputFluids, nbt.getCompound("InputFluids"));
        PtNBTUtils.loadItemsFromNBT(outputItems, nbt.getCompound("OutputItems"));
        PtNBTUtils.loadFluidsFromNBT(outputFluids, nbt.getCompound("OutputFluids"));
        return new PtConditionalRecipe(recipeId, group, condition, xp, inputItems, inputFluids, outputItems, outputFluids);
    }

    public boolean extractInputItems(IItemHandler itemHandler, int slotBegin, int slotEnd) {
        mainLoop: for (Ingredient ingredient : this.inputItems) {
            for (int i = slotBegin; i < slotEnd; ++i) {

                ItemStack exist = itemHandler.getStackInSlot(i);

                if (ingredient.test(exist)) {
                    for (ItemStack extract : ingredient.getItems()) {
                        if (exist.sameItem(extract) && exist.getCount() >= extract.getCount()) {
                            itemHandler.extractItem(i, extract.getCount(), false);
                            continue mainLoop;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public boolean extractInputFluids(IFluidHandler fluidHandler, int tankBegin, int tankEnd) {
        mainLoop: for (FluidStack extract : this.inputFluids) {
            for (int i = tankBegin; i < tankEnd; ++i) {

                FluidStack exist = fluidHandler.getFluidInTank(i);

                if (exist.isFluidEqual(extract) && exist.getAmount() >= extract.getAmount()) {
                    fluidHandler.drain(extract, IFluidHandler.FluidAction.EXECUTE);
                    continue mainLoop;
                }
            }
            return false;
        }
        return true;
    }

    public boolean extractAllInput(IItemHandler itemHandler, int slotBegin, int slotEnd, IFluidHandler fluidHandler, int tankBegin, int tankEnd) {
        return extractInputItems(itemHandler, slotBegin, slotEnd) && extractInputFluids(fluidHandler, tankBegin, tankEnd);
    }

}
