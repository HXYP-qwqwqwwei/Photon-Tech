package photontech.event;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import photontech.block.heater.solid.PtBurningItemHeaterTER;
import photontech.block.mirror.PtMirrorTER;
import photontech.init.PtBlocks;
import photontech.init.PtFluids;
import photontech.init.PtTileEntities;
import photontech.block.crucible.PtCrucibleTER;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import photontech.utils.Utils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(PtBlocks.QUARTZ_CRUCIBLE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.MIRROR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.SUN_GLASS.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.SUN_GLASS_REVERSE.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(PtFluids.MILK_FLUID.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtFluids.MILK_FLUID_FLOWING.get(), RenderType.translucent());
            // TODO
        });
    }

    @SubscribeEvent
    public static void onTileEntityRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.CRUCIBLE_TILEENTITY.get(), PtCrucibleTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.HEATER_TILEENTITY.get(), PtBurningItemHeaterTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.MIRROR_TILEENTITY.get(), PtMirrorTER::new);
        });
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ResourceLocation(Utils.MOD_ID, "special/frame"));
        ModelLoader.addSpecialModel(new ResourceLocation(Utils.MOD_ID, "special/support"));
        ModelLoader.addSpecialModel(new ResourceLocation(Utils.MOD_ID, "special/silver_mirror"));
    }
}
