package photontech.event;

import io.netty.util.concurrent.SingleThreadEventExecutor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import photontech.utils.tileentity.IPtTickable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PtExtraThreadHandler {
    public static ScheduledExecutorService PT_SERVICE;
    public static final int PERIOD = 1000;

    @SubscribeEvent
    public static void startPtThread(FMLServerStartingEvent event) {
        PtExtraThread.initThread();
        PT_SERVICE = Executors.newScheduledThreadPool(1);
        PT_SERVICE.scheduleAtFixedRate(new PtExtraThread(), 0L, PERIOD, TimeUnit.MILLISECONDS);
    }

    @SubscribeEvent
    public static void shutdownPtThread(FMLServerStoppingEvent event) {
        PT_SERVICE.shutdown();
    }

}
