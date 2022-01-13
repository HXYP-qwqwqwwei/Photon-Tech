//package photontech.event;
//
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
//import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 处理线程的启动和终止事件
// */
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class PtExtraThreadHandler {
//    public static ScheduledExecutorService PT_SERVICE;
//    public static final int PERIOD = 20;
//
//    @SubscribeEvent
//    public static void startPtThread(FMLServerStartingEvent event) {
//        PtExtraThread.initThread();
//        PT_SERVICE = Executors.newScheduledThreadPool(1);
//        PT_SERVICE.scheduleAtFixedRate(new PtExtraThread(), 0L, PERIOD, TimeUnit.MILLISECONDS);
//    }
//
//    @SubscribeEvent
//    public static void shutdownPtThread(FMLServerStoppingEvent event) {
//        PT_SERVICE.shutdown();
//    }
//
//}
