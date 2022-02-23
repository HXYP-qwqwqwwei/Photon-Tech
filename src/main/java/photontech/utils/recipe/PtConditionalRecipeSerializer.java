package photontech.utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static photontech.utils.PtConstants.*;

public class PtConditionalRecipeSerializer extends PtRecipeSerializer<PtConditionalRecipe> {

    @Nonnull
    @Override
    public PtConditionalRecipe fromJson(@Nonnull ResourceLocation location, @Nonnull JsonObject jsonObject) {

        // CONDITION
        RecipeCondition condition = RecipeCondition.parse(JSONUtils.getAsJsonObject(jsonObject, CONDITION));

        // RATE
        float rate = -1;

        // INPUT
        List<Ingredient> inputItems = null;
        List<FluidStack> inputFluids = null;

        // OUTPUT
        List<ItemStack> outputItems = null;
        List<FluidStack> outputFluids = null;

        // CATALYZE
        Ingredient catalyzeItem = Ingredient.EMPTY;
        FluidStack catalyzeFluid = FluidStack.EMPTY;

        // GROUP
        String group = JSONUtils.getAsString(jsonObject, GROUP);

        // RATE
        if (jsonObject.has(RATE)) {
            rate = JSONUtils.getAsFloat(jsonObject, RATE);
        }

        // INPUT ITEMS
        if (jsonObject.has(INPUT_ITEMS)) {
            JsonElement inputItemsObj = jsonObject.get(INPUT_ITEMS);
            inputItems = NonNullList.create();
            this.addIngredients(inputItems, inputItemsObj);
        }

        // INPUT FLUIDS
        if (jsonObject.has(INPUT_FLUIDS)) {
            JsonElement inputFluidsObj = jsonObject.get(INPUT_FLUIDS);
            inputFluids = NonNullList.create();
            this.addFluids(inputFluids, inputFluidsObj);
        }

        // OUTPUT ITEMS
        if (jsonObject.has(OUTPUT_ITEMS)) {
            JsonElement outputItemsObj = jsonObject.get(OUTPUT_ITEMS);
            outputItems = NonNullList.create();
            this.addItems(outputItems, outputItemsObj);
        }

        // OUTPUT FLUIDS
        if (jsonObject.has(OUTPUT_FLUIDS)) {
            JsonElement outputFluidsObj = jsonObject.get(OUTPUT_FLUIDS);
            outputFluids = NonNullList.create();
            this.addFluids(outputFluids, outputFluidsObj);
        }

        // CATALYZE
        if (jsonObject.has(ITEM_CATALYST)) {
            JsonElement catalyzeItemObj = jsonObject.get(ITEM_CATALYST);
            catalyzeItem = parseIngredient(catalyzeItemObj);
        }

        if (jsonObject.has(FLUID_CATALYST)) {
            JsonElement catalyzeFluidObj = jsonObject.get(FLUID_CATALYST);
            catalyzeFluid = parseFluidJSON((JsonObject) catalyzeFluidObj);
        }

        return new PtConditionalRecipe(location, group, condition, rate, inputItems, inputFluids, outputItems, outputFluids, catalyzeItem, catalyzeFluid);
    }

    @Nullable
    @Override
    public PtConditionalRecipe fromNetwork(@Nonnull ResourceLocation location, @Nonnull PacketBuffer packetBuffer) {

        // 反应条件
        RecipeCondition condition = RecipeCondition.fromNetwork(packetBuffer);

        // 输入
        NonNullList<Ingredient> inputItems = this.ingredientsFromNetwork(packetBuffer);
        NonNullList<FluidStack> inputFluids = this.fluidsFromNetwork(packetBuffer);

        // 输出
        NonNullList<ItemStack> outputItems = this.itemsFromNetwork(packetBuffer);
        NonNullList<FluidStack> outputFluids = this.fluidsFromNetwork(packetBuffer);

        // 催化剂
        Ingredient catalyzeItem = Ingredient.fromNetwork(packetBuffer);
        FluidStack catalyzeFluid = FluidStack.readFromPacket(packetBuffer);

        // 组
        String group = packetBuffer.readUtf();

        return new PtConditionalRecipe(location, group, condition, 0, inputItems, inputFluids, outputItems, outputFluids, catalyzeItem, catalyzeFluid);
    }


    @Override
    public void toNetwork(@Nonnull PacketBuffer packetBuffer, PtConditionalRecipe recipe) {

        // 反应条件
        recipe.getCondition().toNetwork(packetBuffer);

        // 输入
        this.ingredientsToNetwork(recipe.getInputItems(), packetBuffer);
        this.fluidsToNetwork(recipe.getInputFluids(), packetBuffer);

        // 输出
        this.itemsToNetwork(recipe.getOutputItems(), packetBuffer);
        this.fluidsToNetwork(recipe.getOutputFluids(), packetBuffer);

        // 催化剂
        recipe.itemCatalyst.toNetwork(packetBuffer);
        recipe.fluidCatalyst.writeToPacket(packetBuffer);

        // 组
        packetBuffer.writeUtf(recipe.getGroup());
    }
}
