package photontech.event.handler;

import photontech.block.heater.solid.PtBurningItemHeaterScreen;
import photontech.block.crucible.PtCrucibleScreen;
import photontech.init.PtContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler {
    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ScreenManager.register(PtContainers.CRUCIBLE_CONTAINER.get(), PtCrucibleScreen::new);
            ScreenManager.register(PtContainers.HEATER_CONTAINER.get(), PtBurningItemHeaterScreen::new);
        });
    }
}