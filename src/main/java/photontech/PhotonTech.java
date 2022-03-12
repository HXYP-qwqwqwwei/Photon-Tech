package photontech;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import photontech.init.*;

@Mod(PhotonTech.ID)
public class PhotonTech {
    public static final String ID = "photontech";

    public PhotonTech() {
        PtItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtFluids.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtTileEntities.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtRecipes.RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PtContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
