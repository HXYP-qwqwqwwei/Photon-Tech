package photontech.event.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.KineticMachine;
import photontech.block.kinetic.gears.ConnectCondition;
import photontech.block.kinetic.gears.GearTile;
import photontech.event.pt.KtEvent;
import photontech.utils.helper.fuctions.PtMath;
import photontech.utils.helper.fuctions.PtPhysics;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KtGearsEventHandler {

    @SubscribeEvent
    public static void onAxialCombinedEvent(KtEvent.KtGearSynchronizeNotifyEvent event) {
        IWorld level = event.getWorld();
        KineticMachine selfKt = event.getMachine();
        selfKt.gearNotifyTick = level.getLevelData().getGameTime();
        selfKt.getTerminal().expired = false;
    }

    @SubscribeEvent
    public static void onGearSynchronize(KtEvent.KtGearSynchronizeEvent event) {
        IWorld level = event.getWorld();
        BlockPos pos = event.getPos();
        GearTile currGear = event.getGearKt();
        KineticMachine terminal = currGear.getTerminal();
        // 过期的事件
        if (terminal.expired) {
            return;
        }

        // 从自身开始，递归地向邻居传递，邻居均以自身为参考
        ConnectCondition[] conditions = currGear.getConnectConditions();
        for (ConnectCondition condition : conditions) {
            GearTile neighborGear = getConnectableGear(level, condition);
            if (neighborGear == null) continue;
            KineticMachine neighborTerminal = neighborGear.getTerminal();
            PtPhysics.gearsMomentumConservation(currGear, neighborGear, condition.reverse());

            // 以自身为参考，计算邻居频率
            int frequencyLevel = terminal.getFreqLevel() + PtMath.log2Int(1.0 * currGear.getRadius() / neighborGear.getRadius());
            if (frequencyLevel < 0) {   // 频率等级小于0的情况，重新以邻居为参考开始同步
                terminal.expired = true;  // 用于取消已发布的事件
                neighborTerminal.primaryReset();
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
                break;
            }
            // 计算相位
            float fixedPhase = -terminal.getPhase() * (1F * currGear.getRadius() / neighborGear.getRadius());
            fixedPhase -= Math.PI / neighborGear.getTeethAmount();
            // 是否反向
            boolean reversed = terminal.reversed() ^ condition.reverse();

            // 冲突的连接
            if (terminal.samePrimary(neighborTerminal)) {
                if (neighborTerminal.getFreqLevel() != frequencyLevel || neighborTerminal.reversed() != reversed) {
                    MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(currGear));
                    level.destroyBlock(pos, true);
                    break;
                }
                neighborTerminal.setPhase(fixedPhase);
                continue;
            }
            // 同步邻居
            neighborTerminal.gearCombine(terminal, frequencyLevel, fixedPhase, reversed);

            // 同步完之后，向总线报告一个通知事件
            MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onGearInvalidate(KtEvent.KtInvalidateEvent event) {
        KineticMachine machine = event.getMachine();
        IWorld level = event.getWorld();

        if (machine instanceof GearTile) {
            GearTile currGear = (GearTile) machine;
            ConnectCondition[] conditions = currGear.getConnectConditions();

            for (ConnectCondition condition : conditions) {
                GearTile neighborGear = getConnectableGear(level, condition);
                if (neighborGear == null) continue;

                neighborGear.getTerminal().primaryReset();
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeNotifyEvent(neighborGear));
            }
        }
    }

    @Nullable
    private static GearTile getConnectableGear(IWorld world, ConnectCondition condition) {
        TileEntity te = world.getBlockEntity(condition.getPos());
        if (te instanceof GearTile) {
            GearTile gear = (GearTile) te;
            if (gear.isActive() && condition.test(gear)) return gear;
        }
        return null;
    }

}
