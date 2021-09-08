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
import java.nio.charset.StandardCharsets;

import static photontech.utils.PtConstants.*;

public class PtConditionalRecipeSerializer extends PtRecipeSerializer<PtConditionalRecipe> {

    @Nonnull
    @Override
    public PtConditionalRecipe fromJson(@Nonnull ResourceLocation location, @Nonnull JsonObject jsonObject) {

        NonNullList<Ingredient> inputItems = NonNullList.create();
        NonNullList<FluidStack> inputFluids = NonNullList.create();
        NonNullList<ItemStack> outputItems = NonNullList.create();
        NonNullList<FluidStack> outputFluids = NonNullList.create();

        // GROUP
        String group = JSONUtils.getAsString(jsonObject, GROUP);

        // CONDITION
        RecipeCondition condition = RecipeCondition.parse(JSONUtils.getAsJsonObject(jsonObject, CONDITION));

        // INPUT ITEMS
        if (jsonObject.has(INPUT_ITEMS)) {
            JsonElement inputItemsObj = jsonObject.get(INPUT_ITEMS);
            this.addIngredients(inputItems, inputItemsObj);
        }

        // INPUT FLUIDS
        if (jsonObject.has(INPUT_FLUIDS)) {
            JsonElement inputFluidsObj = jsonObject.get(INPUT_FLUIDS);
            this.addFluids(inputFluids, inputFluidsObj);
        }

        // OUTPUT ITEMS
        if (jsonObject.has(OUTPUT_ITEMS)) {
            JsonElement outputItemsObj = jsonObject.get(OUTPUT_ITEMS);
            this.addItems(outputItems, outputItemsObj);
        }

        // OUTPUT FLUIDS
        if (jsonObject.has(OUTPUT_FLUIDS)) {
            JsonElement outputFluidsObj = jsonObject.get(OUTPUT_FLUIDS);
            this.addFluids(outputFluids, outputFluidsObj);
        }

        return new PtConditionalRecipe(location, group, condition, 0, inputItems, inputFluids, outputItems, outputFluids);
    }

    @Nullable
    @Override
    public PtConditionalRecipe fromNetwork(@Nonnull ResourceLocation location, @Nonnull PacketBuffer packetBuffer) {

        RecipeCondition condition = RecipeCondition.fromNetwork(packetBuffer);

        NonNullList<Ingredient> inputItems = this.ingredientsFromNetwork(packetBuffer);
        NonNullList<FluidStack> inputFluids = this.fluidsFromNetwork(packetBuffer);

        NonNullList<ItemStack> outputItems = this.itemsFromNetwork(packetBuffer);
        NonNullList<FluidStack> outputFluids = this.fluidsFromNetwork(packetBuffer);

        byte[] buff = new byte[Short.MAX_VALUE];
        packetBuffer.readBytes(buff);
        String group = new String(buff);

        return new PtConditionalRecipe(location, group, condition, 0, inputItems, inputFluids, outputItems, outputFluids);
    }


    @Override
    public void toNetwork(@Nonnull PacketBuffer packetBuffer, PtConditionalRecipe recipe) {

        recipe.getCondition().toNetwork(packetBuffer);
        this.ingredientsToNetwork(recipe.getInputItems(), packetBuffer);
        this.fluidsToNetwork(recipe.getInputFluids(), packetBuffer);

        this.itemsToNetwork(recipe.getOutputItems(), packetBuffer);
        this.fluidsToNetwork(recipe.getOutputFluids(), packetBuffer);

        byte[] buff = recipe.getGroup().getBytes(StandardCharsets.US_ASCII);
        packetBuffer.writeBytes(buff);
    }
}
