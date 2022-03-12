package photontech.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import photontech.PhotonTech;
import photontech.utils.recipe.PtConditionalRecipe;
import photontech.utils.recipe.PtConditionalRecipeSerializer;

public class PtRecipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PhotonTech.ID);

//    public static final RegistryObject<IRecipeSerializer<CrucibleMeltingRecipe>> MELTING_RECIPE = RECIPES.register("melting", CrucibleMeltingRecipeSerializer::new);
    public static final RegistryObject<IRecipeSerializer<PtConditionalRecipe>> CONDITIONAL_RECIPE = RECIPES.register("conditional", PtConditionalRecipeSerializer::new);

    public static class Types {
        public static final IRecipeType<PtConditionalRecipe> CONDITIONAL_TYPE = IRecipeType.register(PhotonTech.ID + "conditional");
//        public static final IRecipeType<CrucibleMeltingRecipe> MELTING_TYPE = IRecipeType.register(Utils.MOD_ID + "melting");
//        public static final IRecipeType<CrucibleMeltingRecipe> COOLING_TYPE = IRecipeType.register(Utils.MOD_ID + "cooling");

    }
}
