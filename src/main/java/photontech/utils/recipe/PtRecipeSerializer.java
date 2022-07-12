package photontech.utils.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import static photontech.utils.PtConstants.*;

public abstract class PtRecipeSerializer<T extends ConditionalRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {


    public void addIngredients(@Nonnull List<Ingredient> list, @Nonnull JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement ingredient : jsonElement.getAsJsonArray()) {
                list.add(parseIngredient(ingredient));
            }
        }
        else {
            list.add(parseIngredient(jsonElement));
        }
    }

    public Ingredient parseIngredient(@Nonnull JsonElement ingredient) {
        return Ingredient.fromJson(ingredient);
    }


    public void addFluids(@Nonnull List<FluidStack> list, @Nonnull JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement fluidJSON : jsonElement.getAsJsonArray()) {
                list.add(parseFluidJSON((JsonObject) fluidJSON));
            }
        }
        else {
            list.add(parseFluidJSON((JsonObject) jsonElement));
        }
    }

    public FluidStack parseFluidJSON(@Nonnull JsonObject fluidJSON) {
        int amount = 1000;
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(JSONUtils.getAsString(fluidJSON, FLUID)));
        if (fluidJSON.has(AMOUNT)) {
            amount = Math.max(1, JSONUtils.getAsInt(fluidJSON, AMOUNT));
        }
        if (fluid != null) {
            return new FluidStack(fluid, amount);
        }
        return FluidStack.EMPTY;
    }

    public void addItems(@Nonnull List<ItemStack> list, @Nonnull JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement itemJSON : jsonElement.getAsJsonArray()) {
                list.add(parseItemJSON((JsonObject) itemJSON));
            }
        }
        else {
            list.add(parseItemJSON((JsonObject) jsonElement));
        }
    }

    public ItemStack parseItemJSON(@Nonnull JsonObject itemJSON) {
        int count = 1;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(itemJSON, ITEM)));
        if (itemJSON.has(COUNT)) {
            count = Math.max(count, JSONUtils.getAsInt(itemJSON, COUNT));
        }
        if (item != null) {
            return new ItemStack(item, count);
        }
        return ItemStack.EMPTY;
    }


    @Nullable
    public NonNullList<Ingredient> ingredientsFromNetwork(@Nonnull PacketBuffer packetBuffer) {
        int size = packetBuffer.readInt();
        if (size == 0) return null;
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < size; ++i) {
            ingredients.add(Ingredient.fromNetwork(packetBuffer));
        }
        return ingredients;
    }

    public void ingredientsToNetwork(@Nullable List<Ingredient> ingredients, @Nonnull PacketBuffer packetBuffer) {
        if (ingredients == null) {
            packetBuffer.writeInt(0);
            return;
        }
        packetBuffer.writeInt(ingredients.size());
        for (Ingredient ingredient : ingredients) {
            ingredient.toNetwork(packetBuffer);
        }
    }

    @Nullable
    public NonNullList<FluidStack> fluidsFromNetwork(@Nonnull PacketBuffer packetBuffer) {
        int size = packetBuffer.readInt();
        if (size == 0) return null;
        NonNullList<FluidStack> fluidStacks = NonNullList.create();
        for (int i = 0; i < size; ++i) {
            fluidStacks.add(packetBuffer.readFluidStack());
        }
        return fluidStacks;
    }

    public void fluidsToNetwork(@Nullable List<FluidStack> fluidStacks, @Nonnull PacketBuffer packetBuffer) {
        if (fluidStacks == null) {
            packetBuffer.writeInt(0);
            return;
        }
        packetBuffer.writeInt(fluidStacks.size());
        for (FluidStack stack : fluidStacks) {
            packetBuffer.writeFluidStack(stack);
        }
    }

    public void itemsToNetwork(@Nullable List<ItemStack> itemStacks, @Nonnull PacketBuffer packetBuffer) {
        if (itemStacks == null) {
            packetBuffer.writeInt(0);
            return;
        }
        packetBuffer.writeInt(itemStacks.size());
        for (ItemStack stack : itemStacks) {
            packetBuffer.writeItem(stack);
        }
    }

    @Nullable
    public NonNullList<ItemStack> itemsFromNetwork(@Nonnull PacketBuffer packetBuffer) {
        int size = packetBuffer.readInt();
        if (size == 0) {
            return null;
        }
        NonNullList<ItemStack> fluidStacks = NonNullList.create();
        for (int i = 0; i < size; ++i) {
            fluidStacks.add(packetBuffer.readItem());
        }
        return fluidStacks;
    }

}
