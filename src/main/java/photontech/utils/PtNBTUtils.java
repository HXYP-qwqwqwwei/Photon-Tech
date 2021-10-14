package photontech.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fluids.FluidStack;

import java.io.StringReader;
import java.util.List;


public class PtNBTUtils {

    public static final String ITEMS = "Items";
    public static final String INGREDIENTS = "Ingredients";
    public static final String FLUIDS = "Fluids";
    public static final String ROTATE_BODIES = "RigidBodies";

    public static INBT writeIngredientToNBT(Ingredient ingredient) throws CommandSyntaxException {
        JsonElement json = ingredient.toJson();
        if (json.isJsonArray()) {
            ListNBT list = new ListNBT();
            for (JsonElement element : json.getAsJsonArray()) {
                list.add(JsonToNBT.parseTag(element.toString()));
            }
            return list;
        }
        return JsonToNBT.parseTag(json.toString());
    }

    public static Ingredient readIngredientFromNBT(INBT nbt) {
        JsonReader reader = new JsonReader(new StringReader(nbt.toString()));
        reader.setLenient(true);
        return Ingredient.fromJson(new JsonParser().parse(reader));
    }

    public static void loadItemsFromNBT(List<ItemStack> list, CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get(ITEMS);
        if (listNBT != null) {
            for (INBT itemNBT : listNBT) {
                list.add(ItemStack.of((CompoundNBT) itemNBT));
            }
        }
    }

    public static CompoundNBT saveItemsToNBT(List<ItemStack> list, CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        for (ItemStack itemStack : list) {
            listNBT.add(itemStack.serializeNBT());
        }
        nbt.put(ITEMS, listNBT);
        return nbt;
    }

    public static void loadFluidsFromNBT(List<FluidStack> list, CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get(FLUIDS);
        if (listNBT != null) {
            for (INBT fluidNBT : listNBT) {
                list.add(FluidStack.loadFluidStackFromNBT((CompoundNBT) fluidNBT));
            }
        }
    }

    public static CompoundNBT saveFluidsToNBT(List<FluidStack> list, CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        for (FluidStack fluidStack : list) {
            listNBT.add(fluidStack.writeToNBT(new CompoundNBT()));
        }
        nbt.put(FLUIDS, listNBT);
        return nbt;
    }

    public static void loadIngredientsFromNBT(List<Ingredient> list, CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get(INGREDIENTS);
        if (listNBT != null) {
            for (INBT ingredientNBT : listNBT) {
                list.add(readIngredientFromNBT(ingredientNBT));
            }
        }
    }

    public static CompoundNBT saveIngredientsToNBT(List<Ingredient> list, CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        for (Ingredient ingredient : list) {
            try {
                listNBT.add(writeIngredientToNBT(ingredient));
            }
            catch (CommandSyntaxException ignored){}
        }
        nbt.put(INGREDIENTS, listNBT);
        return nbt;
    }

//    public static void loadRigidBodies(List<PtRotateBody> list, CompoundNBT nbt) {
//        ListNBT listNBT = (ListNBT) nbt.get(ROTATE_BODIES);
//        if (listNBT != null) {
//            for (INBT rigidNBT : listNBT) {
//                list.add();
//            }
//
//        }
//    }

}
