package photontech.event;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import photontech.block.kinetic.axle.AxleTER;
import photontech.block.kinetic.gears.PtGearsTER;
import photontech.block.heater.solid.PtBurningItemHeaterTER;
import photontech.block.kinetic.motor.dc_brush.DCBrushMotorTERPartA;
import photontech.block.kinetic.motor.dc_brush.DCBrushMotorTERPartB;
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
import photontech.utils.PtConstants;
import photontech.utils.Utils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(PtBlocks.QUARTZ_CRUCIBLE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.MIRROR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.AXLE.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.SUN_GLASS.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.SUN_GLASS_REVERSE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.GEARS_BLOCK.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.GEARS_BLOCK.get(), RenderType.cutout());

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
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.AXLE_TILE.get(), AxleTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.GEARS_TILEENTITY.get(), PtGearsTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), DCBrushMotorTERPartA::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), DCBrushMotorTERPartB::new);
        });
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_B);
        ModelLoader.addSpecialModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_CONTACTOR);
        ModelLoader.addSpecialModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_BRUSH);
        ModelLoader.addSpecialModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_WIRES);
        ModelLoader.addSpecialModel(PtConstants.MODELS.IRON_AXLE_MODEL);
        ModelLoader.addSpecialModel(PtConstants.MODELS.MIRROR_FRAME);
        ModelLoader.addSpecialModel(PtConstants.MODELS.MIRROR_SUPPORT);
        ModelLoader.addSpecialModel(PtConstants.MODELS.SILVER_MIRROR);
        ModelLoader.addSpecialModel(PtConstants.MODELS.WOODEN_GEAR_MODEL);
    }
}
