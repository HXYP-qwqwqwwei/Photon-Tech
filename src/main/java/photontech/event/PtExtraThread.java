//package photontech.event;
//
//import net.minecraft.tileentity.TileEntity;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import photontech.utils.tileentity.IPtTickable;
//
//import java.util.Map;
//import java.util.PriorityQueue;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//public class PtExtraThread extends Thread {
//    // 机器表，存有所有需要在此线程中执行的Tile
//    public static final Map<Long, IPtTickable> MACHINE_TABLE = new ConcurrentHashMap<>();
////    private static final Queue<IPtTickable> MACHINE_TABLE = new ConcurrentLinkedQueue<>();
//    // 删除任务队列，一切对MACHINE_TABLE进行删除的任务都在此队列执行
//    public static final Queue<Runnable> REMOVE_TASKS = new ConcurrentLinkedQueue<>();
//    // 注册任务队列，一切对MACHINE_TABLE进行插入的任务都在此队列执行
//    public static final Queue<Runnable> REGISTER_TASKS = new ConcurrentLinkedQueue<>();
//    // 普通任务队列，除了对机器表进行删除和插入的任务都在此队列中执行
//    public static final Queue<Runnable> NORMAL_TASKS = new ConcurrentLinkedQueue<>();
//
//    private static final Logger LOGGER = LogManager.getLogger();
//    public static long PT_TICKS = 0L;
//    public static long PT_TICK_COST = 0;
//    public static long PT_TICK_REMOVE_COST = 0;
//    public static long PT_TICK_REGISTER_COST = 0;
//    public static long PT_TICK_TASK_COST = 0;
//    public static long PT_TICK_MACHINE_COST = 0;
//
//    @Override
//    public void run() {
////        LOGGER.info("tick = " + PT_TICKS + ", machines: " + MACHINE_TABLE.size() + ", tasks: " + NORMAL_TASKS.size());
//
//        long t0 = System.nanoTime();
//        // 执行每个机器的ptTick()
////        for (long posLong : MACHINE_TABLE.keySet()) {
////            MACHINE_TABLE.get(posLong).ptTick();
////            MACHINE_TABLE.contains()
//////            machine.ptTick();
////        }
//        MACHINE_TABLE.forEach(((aLong, iPtTickable) -> iPtTickable.ptTick()));
//        long t1 = System.nanoTime();
//        // 执行Tasks，顺序为注册-删除-普通
//        while (!REGISTER_TASKS.isEmpty()) {
//            REGISTER_TASKS.poll().run();
//        }
//        long t2 = System.nanoTime();
//        while (!REMOVE_TASKS.isEmpty()) {
//            REMOVE_TASKS.poll().run();
//        }
//        long t3 = System.nanoTime();
//        while (!NORMAL_TASKS.isEmpty()) {
//            NORMAL_TASKS.poll().run();
//        }
//        long t4 = System.nanoTime();
//        PT_TICKS += 1;
//        PT_TICK_COST = t4 - t0;
//        PT_TICK_MACHINE_COST = t1 - t0;
//        PT_TICK_REGISTER_COST = t2 - t1;
//        PT_TICK_REMOVE_COST = t3 - t2;
//        PT_TICK_TASK_COST = t4 - t3;
//    }
//
//    public static void initThread() {
//        MACHINE_TABLE.clear();
//        REMOVE_TASKS.clear();
//        PT_TICKS = 0L;
//    }
//
//    public static long getPtTime() {
//        return PT_TICKS;
//    }
//
//    public static void submitTask(Runnable task) {
//        NORMAL_TASKS.offer(task);
//    }
//
//    public static void registerMachine(TileEntity tileEntity) {
//        if (tileEntity instanceof IPtTickable && tileEntity.getLevel() != null && !tileEntity.getLevel().isClientSide) {
//            IPtTickable iPtTickable = (IPtTickable) tileEntity;
//            REGISTER_TASKS.offer(() -> MACHINE_TABLE.put(tileEntity.getBlockPos().asLong(), iPtTickable));
//        }
//    }
//
//    public static boolean isMachineNotRegistered(TileEntity tileEntity) {
//        return MACHINE_TABLE.get(tileEntity.getBlockPos().asLong()) != tileEntity;
//    }
//
//    public static void removeMachine(TileEntity tileEntity) {
//        if (tileEntity instanceof IPtTickable && tileEntity.getLevel() != null && !tileEntity.getLevel().isClientSide) {
//            REMOVE_TASKS.offer(() -> {
//                long posLong = tileEntity.getBlockPos().asLong();
//                if (tileEntity == MACHINE_TABLE.get(posLong)) {
//                    MACHINE_TABLE.remove(posLong, tileEntity);
//                }
//            });
//        }
//    }
//}
