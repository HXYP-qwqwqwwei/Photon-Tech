package photontech.event.handler;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import photontech.block.crucible.PtCrucibleTER;
import photontech.block.heater.solid.PtBurningItemHeaterTER;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.HalfAxleTile;
import photontech.block.kinetic.gears.GearTile;
import photontech.block.kinetic.motor.dc_brush.DCBrushMotorTERPartA;
import photontech.block.kinetic.motor.dc_brush.DCBrushMotorCoilTile;
import photontech.block.kinetic.motor.infinity.InfinityMotorTER;
import photontech.block.mirror.PtMirrorTER;
import photontech.init.PtBlocks;
import photontech.init.PtFluids;
import photontech.init.PtTileEntities;
import photontech.utils.PtConstants;
import photontech.utils.client.render.Compartment;
import photontech.utils.client.render.KtMachineTER;
import photontech.utils.client.render.SuperByteBufferCache;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final SuperByteBufferCache BUFFER_CACHE = new SuperByteBufferCache();

    @SubscribeEvent
    public static void onRenderTypeSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(PtBlocks.QUARTZ_CRUCIBLE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtBlocks.MIRROR.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.IRON_AXLE.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.BRUSH_DC_MOTOR_PART_B.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PtBlocks.BRUSH_DC_MOTOR_PART_A.get(), RenderType.cutout());

            RenderTypeLookup.setRenderLayer(PtBlocks.INFINITY_MOTOR_BLOCK.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(PtFluids.MILK_FLUID.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(PtFluids.MILK_FLUID_FLOWING.get(), RenderType.translucent());
        });

    }

    @SubscribeEvent
    public static void onTileEntityRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.CRUCIBLE_TILEENTITY.get(), PtCrucibleTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.HEATER_TILEENTITY.get(), PtBurningItemHeaterTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.MIRROR_TILEENTITY.get(), PtMirrorTER::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.AXLE_TILE.get(), KtMachineTER<FullAxleTile>::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.HALF_AXLE_TILE.get(), KtMachineTER<HalfAxleTile>::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.SMALL_GEARS_TILEENTITY.get(), KtMachineTER<GearTile>::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.LARGE_GEARS_TILEENTITY.get(), KtMachineTER<GearTile>::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), DCBrushMotorTERPartA::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), KtMachineTER<DCBrushMotorCoilTile>::new);
            ClientRegistry.bindTileEntityRenderer(PtTileEntities.INFINITY_MOTOR.get(), InfinityMotorTER::new);

        });

        BUFFER_CACHE.registerCompartment(Compartment.BLOCK_MODEL);
        BUFFER_CACHE.registerCompartment(Compartment.GENERIC_MODEL);
        BUFFER_CACHE.registerCompartment(Compartment.ITEM_MODEL);
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(PtConstants.MODELS.DC_BRUSH_MODEL);
        ModelLoader.addSpecialModel(PtConstants.MODELS.INFINITY_MOTOR_ROTATER);

        ModelLoader.addSpecialModel(PtConstants.MODELS.MIRROR_FRAME);
        ModelLoader.addSpecialModel(PtConstants.MODELS.MIRROR_SUPPORT);
        ModelLoader.addSpecialModel(PtConstants.MODELS.SILVER_MIRROR);
        ModelLoader.addSpecialModel(PtConstants.MODELS.WOODEN_GEAR_MODEL);
    }
}
