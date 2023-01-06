package photontech.event.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.AxleMaterial;
import photontech.block.kinetic.KtMachineTile;
import photontech.event.pt.KtEvent;
import photontech.init.PtCapabilities;
import photontech.utils.helperfunctions.AxisHelper;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KtAxialEventHandler {


    @SubscribeEvent
    public static void onKtCreated(KtEvent.KtCreateEvent event) {
        if (event.getWorld().isClientSide()) return;

        // 初始化
        KtMachineTile selfKt = event.getSelfKt();
        selfKt.initAll();

        Direction positiveSide = AxisHelper.getAxisPositiveDirection(selfKt.getAxis());
        KtMachineTile neighbor = getNeighborKt(selfKt, positiveSide);
        KtEvent.KtActiveEvent activeEvent = new KtEvent.KtActiveEvent(selfKt, neighbor != null ? positiveSide.getOpposite() : positiveSide);
        MinecraftForge.EVENT_BUS.post(activeEvent);
    }

    @SubscribeEvent
    public static void onKtActive(KtEvent.KtActiveEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        KtMachineTile selfKt = event.getSelfKt();
        Direction side = event.getUpdateDirection();

        AxleMaterial selfMaterial = selfKt.getAxleMaterial();

        // 为self初始化
        selfKt.initAll();

        // 检查该方向邻居
        KtMachineTile neighbor = getNeighborKt(selfKt, side);
        // 若该方向有邻居
        if (neighbor != null) {
            KtMachineTile.KtRotatingState rotState = neighbor.getRotatingState();

            // 若邻居的连接数未达上限，则将自己与其合并，合并后总连接数+1，否则跳过此步
            if (rotState.axialLength < selfMaterial.maxConnect) {
                neighbor.addAxialInertia(selfKt.referenceState.getSelfInertia());
                rotState.axialLength += 1;
                selfKt.setMainBodyPosition(neighbor.getMainBodyPosition());
            }
            else { //邻居的连接数已到达上限，则破坏自身
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(selfKt));
                level.destroyBlock(selfPos, true);
                return;
            }
        }

        // 检查反方向邻居
        KtMachineTile negativeNeighbor = getNeighborKt(selfKt, side.getOpposite());
        // 若反方向有邻居，则在邻居处触发active事件，然后返回
        if (negativeNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KtEvent.KtActiveEvent(negativeNeighbor, side));
            return;
        }
        // 否则表明轴向合并已完成，向总线报告一个完成事件
        MinecraftForge.EVENT_BUS.post(new KtEvent.KtAxialCombinedEvent(selfKt));
    }

    @SubscribeEvent
    public static void onKtInvalidate(KtEvent.KtInvalidateEvent event) {
        KtMachineTile selfKt = event.getSelfKt();
        Direction.Axis axis = selfKt.getAxis();
        Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
        Direction negativeSide = positiveSide.getOpposite();

        KtMachineTile positiveNeighbor = getNeighborKt(selfKt, positiveSide);
        KtMachineTile negativeNeighbor = getNeighborKt(selfKt, negativeSide);
        selfKt.setMainBodyPosition(BlockPos.ZERO);

        if (positiveNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KtEvent.KtActiveEvent(positiveNeighbor, negativeSide));
        }
        if (negativeNeighbor != null) {
            MinecraftForge.EVENT_BUS.post(new KtEvent.KtActiveEvent(negativeNeighbor, positiveSide));
        }
    }


    @Nullable
    private static KtMachineTile getNeighborKt(KtMachineTile kt, Direction direction) {
        IWorld level = kt.getLevel();
        AxleMaterial material = kt.getAxleMaterial();
        if (level == null || material == AxleMaterial.INVALID) return null;
        TileEntity neighbor = level.getBlockEntity(kt.getBlockPos().relative(direction));
        if (neighbor instanceof KtMachineTile) {
            KtMachineTile neighborKt = (KtMachineTile) neighbor;
            return kt.getCapability(PtCapabilities.ROTATING_STATE, direction).isPresent()
                    && neighborKt.getCapability(PtCapabilities.ROTATING_STATE, direction.getOpposite()).isPresent()
                    && neighborKt.getAxleMaterial() == material ? neighborKt : null;
        }
        return null;
    }
}
