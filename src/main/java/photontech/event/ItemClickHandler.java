package photontech.event;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtItems;

@Mod.EventBusSubscriber()
public class ItemClickHandler {
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getSide().isServer()) {
            if (event.getItemStack().getItem() == PtItems.WRENCH.get()) {
                LogManager.getLogger().info("LEFT CLICKED!");
            }
        }
    }
}
