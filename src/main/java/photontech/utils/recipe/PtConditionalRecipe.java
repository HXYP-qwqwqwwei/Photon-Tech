package photontech.utils.recipe;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PtConditionalRecipe implements IRecipe<IInventory>, IPtSerializableRecipe {

    public static interface CatalyzeHandler {
        ItemStack usedCatalyze(ItemStack cat);
    }

    public static final PtConditionalRecipe EMPTY = new PtConditionalRecipe(
            new ResourceLocation("empty"),
            "empty",
            RecipeCondition.IMPOSSIBLE,
            0,
            null, null, null, null,
            Ingredient.EMPTY,
            FluidStack.EMPTY
    );

    // 输入物品/流体
    protected final List<Ingredient> inputItems = new ArrayList<>();
    protected final List<FluidStack> inputFluids = new ArrayList<>();

    // 输出物品/流体
    protected final List<ItemStack> outputItems = new ArrayList<>();
    protected final List<FluidStack> outputFluids = new ArrayList<>();

    // 催化剂
    protected final Ingredient itemCatalyst;
    protected final FluidStack fluidCatalyst;

    protected final RecipeCondition condition;
    // 反应速率，若小于0，则表示无穷大
    public final float rate;

    // 配方id，配方的唯一标识符
    protected final ResourceLocation recipeId;

    // 配方所在组
    protected final String group;

    protected PtConditionalRecipe(ResourceLocation recipeId, String group,
                                  RecipeCondition condition,
                                  float rate,
                                  @Nullable List<Ingredient> inputItems,
                                  @Nullable List<FluidStack> inputFluids,
                                  @Nullable List<ItemStack> outputItems,
                                  @Nullable List<FluidStack> outputFluids,
                                  @Nonnull Ingredient itemCatalyst,
                                  @Nonnull FluidStack fluidCatalyst) {

        this.recipeId = recipeId;
        this.group = group;
        this.condition = condition;
        this.rate = rate;
        this.itemCatalyst = itemCatalyst;
        this.fluidCatalyst = fluidCatalyst;

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

    public boolean testItemCatalyst(IItemHandler items, int slotCat) {
        if (this.itemCatalyst.isEmpty()) return true;
        if (slotCat < 0) return false;
        ItemStack existCat = items.getStackInSlot(slotCat);
        return this.itemCatalyst.test(existCat);
    }

    public boolean testAndDamageCatalyst(IItemHandler items, int slotCat) {
        if (testItemCatalyst(items, slotCat)) {
            damageCatalyze(items, slotCat);
            return true;
        }
        return false;
    }

    public static void damageCatalyze(IItemHandler itemHandler, int slotCat) {
        ItemStack itemStack = itemHandler.getStackInSlot(slotCat).copy();
        if (!itemStack.isEmpty()) {
            itemHandler.extractItem(slotCat, itemStack.getCount(), false);
            itemHandler.insertItem(slotCat, itemStack.getContainerItem(), false);
        }
    }

    public boolean testFluidCatalyst(IFluidHandler fluids, int tankBegin, int tankEnd) {
        // TODO
        return true;
    }

    public boolean test(RecipeCondition condition,
                        IItemHandler items, int slotBegin, int slotEnd, int slotCat,
                        IFluidHandler fluids, int tankBegin, int tankEnd) {

        return this.testCondition(condition) &&
                this.testInputItems(items, slotBegin, slotEnd) &&
                this.testInputFluids(fluids, tankBegin, tankEnd) &&
                this.testItemCatalyst(items, slotCat) &&
                this.testFluidCatalyst(fluids, tankBegin, tankEnd);
    }

    public boolean testInput(IItemHandler items, int slotBegin, int slotEnd, int slotCatalyst,
                             IFluidHandler fluids, int tankBegin, int tankEnd) {
        return this.testInputItems(items, slotBegin, slotEnd)
                && this.testInputFluids(fluids, tankBegin, tankEnd)
                && this.testItemCatalyst(items, slotCatalyst)
                && this.testFluidCatalyst(fluids, tankBegin, tankEnd);
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

    public boolean extractAllInput(IItemHandler itemHandler, int slotBegin, int slotEnd, int slotCat, IFluidHandler fluidHandler, int tankBegin, int tankEnd) {
        return extractInputItems(itemHandler, slotBegin, slotEnd) && extractInputFluids(fluidHandler, tankBegin, tankEnd) && testAndDamageCatalyst(itemHandler, slotCat);
    }

    public RecipeCondition getCondition() {
        return condition;
    }


    @Override
    public CompoundNBT saveToNBT(CompoundNBT nbt) {
        // 基本信息：条件，速率，分组，id
        nbt.put("Condition", this.condition.writeToNBT(new CompoundNBT()));
        nbt.putFloat("Rate", this.rate);
        nbt.putString("Group", this.group);
        nbt.putString("RecipeId", this.recipeId.toString());
        // 输入输出
        nbt.put("InputItems", PtNBTUtils.saveIngredientsToNBT(this.inputItems, new CompoundNBT()));
        nbt.put("InputFluids", PtNBTUtils.saveFluidsToNBT(this.inputFluids, new CompoundNBT()));
        nbt.put("OutputItems", PtNBTUtils.saveItemsToNBT(this.outputItems, new CompoundNBT()));
        nbt.put("OutputFluids", PtNBTUtils.saveFluidsToNBT(this.outputFluids, new CompoundNBT()));
        // 催化信息
        try {
            nbt.put("ItemCatalyst", PtNBTUtils.writeIngredientToNBT(this.itemCatalyst));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        nbt.put("FluidCatalyst", this.fluidCatalyst.writeToNBT(new CompoundNBT()));
        return nbt;
    }

    public static PtConditionalRecipe loadFromNBT(CompoundNBT nbt) {
        if (nbt.isEmpty()) {
            return null;
        }
        RecipeCondition condition = RecipeCondition.loadFromNBT(nbt.getCompound("Condition"));
        float rate = nbt.getFloat("Rate");
        String group = nbt.getString("Group");
        ResourceLocation recipeId = new ResourceLocation(nbt.getString("RecipeId"));
        List<Ingredient> inputItems = NonNullList.create();
        List<FluidStack> inputFluids = NonNullList.create();
        List<ItemStack> outputItems = NonNullList.create();
        List<FluidStack> outputFluids = NonNullList.create();
        // 输入输出
        PtNBTUtils.loadIngredientsFromNBT(inputItems, nbt.getCompound("InputItems"));
        PtNBTUtils.loadFluidsFromNBT(inputFluids, nbt.getCompound("InputFluids"));
        PtNBTUtils.loadItemsFromNBT(outputItems, nbt.getCompound("OutputItems"));
        PtNBTUtils.loadFluidsFromNBT(outputFluids, nbt.getCompound("OutputFluids"));
        // 催化剂
        Ingredient itemCatalyst = PtNBTUtils.readIngredientFromNBT(nbt.getCompound("ItemCatalyst"));
        FluidStack fluidCatalyst = FluidStack.loadFluidStackFromNBT(nbt.getCompound("FluidCatalyst"));
        return new PtConditionalRecipe(recipeId, group, condition, rate, inputItems, inputFluids, outputItems, outputFluids, itemCatalyst, fluidCatalyst);
    }


    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World worldIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PtRecipes.CONDITIONAL_RECIPE.get();
    }

    @Nonnull
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
}
