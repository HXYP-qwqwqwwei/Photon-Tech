package photontech;

import photontech.init.*;
import photontech.utils.Utils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Utils.MOD_ID)
public class PhotonTech {

    public PhotonTech() {
        PtItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtFluids.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtTileEntities.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtRecipes.RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
