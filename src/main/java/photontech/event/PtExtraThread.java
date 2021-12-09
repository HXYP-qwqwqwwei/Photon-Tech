package photontech.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import photontech.utils.tileentity.IPtTickable;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PtExtraThread extends Thread {
    private static final Map<Long, IPtTickable> MACHINE_LIST = new ConcurrentHashMap<>();
    private static final Queue<Runnable> TASKS = new ConcurrentLinkedQueue<>();
    private static final Logger LOGGER = LogManager.getLogger();
    public static long PT_TICKS = 0L;

    @Override
    public void run() {
        LOGGER.info("tick = " + PT_TICKS + ", machines: " + MACHINE_LIST.size() + ", tasks: " + TASKS.size());

        // 执行每个机器的ptTick()
        for (IPtTickable machine : MACHINE_LIST.values()) {
            machine.ptTick();
        }
        // 执行Tasks
        while (!TASKS.isEmpty()) {
            TASKS.poll().run();
        }
        PT_TICKS += 1;
    }

    public static void initThread() {
        MACHINE_LIST.clear();
        TASKS.clear();
        PT_TICKS = 0L;
    }

    public static long getPtTime() {
        return PT_TICKS;
    }

    public static boolean submitTask(Runnable task) {
        return TASKS.offer(task);
    }

    public static boolean registerMachine(TileEntity tileEntity) {
        if (tileEntity instanceof IPtTickable && tileEntity.hasLevel() && !tileEntity.getLevel().isClientSide) {
            IPtTickable iPtTickable = (IPtTickable) tileEntity;
            return TASKS.offer(() -> MACHINE_LIST.put(tileEntity.getBlockPos().asLong(), iPtTickable));
        }
        return false;
    }

    public static void removeMachine(TileEntity tileEntity) {
        if (tileEntity instanceof IPtTickable && tileEntity.hasLevel() && !tileEntity.getLevel().isClientSide) {
            TASKS.offer(() -> {
                long posLong = tileEntity.getBlockPos().asLong();
                if (tileEntity == MACHINE_LIST.get(posLong)) {
                    MACHINE_LIST.remove(posLong, tileEntity);
                }
            });
        }
    }
}
