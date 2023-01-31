package photontech.event.handler.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.AxleMaterial;
import photontech.block.kinetic.KineticMachine;
import photontech.event.define.kinetic.AxialCombinedEvent;
import photontech.event.define.kinetic.KineticInvalidateEvent;
import photontech.event.define.kinetic.KineticPlaceEvent;
import photontech.event.define.kinetic.KineticActivateEvent;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.helper.fuctions.PtPhysics;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KineticAxialEventHandler {


    @SubscribeEvent
    public static void onKineticMachinePlace(KineticPlaceEvent event) {
        if (event.getWorld().isClientSide()) return;

        // 初始化
        KineticMachine machine = event.getMachine();
        machine.axialReset();

        Direction positiveSide = AxisHelper.getAxisPositiveDirection(machine.getAxis());

        KineticMachine posNeighbor = getNeighbor(machine, positiveSide);
        KineticMachine negNeighbor = getNeighbor(machine, positiveSide.getOpposite());
        PtPhysics.axialMomentumConservation(machine, posNeighbor, negNeighbor);
        if (posNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KineticActivateEvent(machine, positiveSide));
        } else if (negNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KineticActivateEvent(machine, positiveSide.getOpposite()));
        } else {
            MinecraftForge.EVENT_BUS.post(new AxialCombinedEvent(machine));
        }
    }

    /**
     * 轴向合并，先尝试向更新方向邻居合并，然后通知反方向的邻居递归地合并
     */
    @SubscribeEvent
    public static void onKineticActivate(KineticActivateEvent event) {
        BlockPos pos = event.getPos();
        IWorld level = event.getWorld();
        KineticMachine current = event.getMachine();
        Direction side = event.getUpdateDirection();

        // 初始化
        current.axialReset();

        // 检查该方向邻居
        KineticMachine neighbor = getNeighbor(current, side);
        // 若该方向有邻居，尝试连接，失败则破坏自身
        if (neighbor != null && !current.axialConnectTo(neighbor)) {
            MinecraftForge.EVENT_BUS.post(new KineticInvalidateEvent(current));
            level.destroyBlock(pos, true);
            return;
        }

        // 检查反方向邻居
        KineticMachine negativeNeighbor = getNeighbor(current, side.getOpposite());
        // 若反方向有邻居，则在邻居处触发active事件，然后返回
        if (negativeNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KineticActivateEvent(negativeNeighbor, side));
            return;
        }
        // 否则表明轴向合并已完成，向总线报告一个完成事件
        MinecraftForge.EVENT_BUS.post(new AxialCombinedEvent(current));
    }

    @SubscribeEvent
    public static void onKineticInvalidate(KineticInvalidateEvent event) {
        KineticMachine current = event.getMachine();
        Direction.Axis axis = current.getAxis();
        Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
        Direction negativeSide = positiveSide.getOpposite();

        KineticMachine positiveNeighbor = getNeighbor(current, positiveSide);
        KineticMachine negativeNeighbor = getNeighbor(current, negativeSide);
        float av = current.getAngularVelocity();
        current.kineticInvalidate();

        if (positiveNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KineticActivateEvent(positiveNeighbor, negativeSide));
            positiveNeighbor.setAngularVelocity(av);
        }
        if (negativeNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KineticActivateEvent(negativeNeighbor, positiveSide));
            negativeNeighbor.setAngularVelocity(av);
        }
    }


    @Nullable
    private static KineticMachine getNeighbor(KineticMachine km, Direction direction) {
        IWorld level = km.getLevel();
        AxleMaterial material = km.getAxleMaterial();
        if (level == null || material == AxleMaterial.INVALID) return null;
        TileEntity te = level.getBlockEntity(km.getBlockPos().relative(direction));
        if (te instanceof KineticMachine) {
            KineticMachine neighbor = (KineticMachine) te;
            return km.isKtValidSide(direction)
                    && neighbor.isKtValidSide(direction.getOpposite())
                    && neighbor.getAxleMaterial() == material ? neighbor : null;
        }
        return null;
    }
}
