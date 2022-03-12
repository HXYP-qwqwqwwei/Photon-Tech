package photontech.event.block_event;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.axle.AxleBlock;
import photontech.block.kinetic.axle.KtMachineTile;
import photontech.block.kinetic.axle.KtRotatingBlock;
import photontech.event.pt_events.AxleInsertEvent;
import photontech.init.PtCapabilities;
import photontech.utils.helper_functions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AxleBlockEventHandler {
    public static final int AXLE_MAX_LENGTH = 8;

    @SubscribeEvent
    public static void onAxlePlaceEvent(@Nonnull BlockEvent.EntityPlaceEvent event) {
        handleAxleActiveEvent(event);
    }

    private static void handleAxleActiveEvent(BlockEvent event) {
        BlockState blockState = event.getState();
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity self = level.getBlockEntity(selfPos);
        if (self instanceof KtMachineTile) {
            KtMachineTile selfKt = (KtMachineTile) self;
            AxleBlock.AxleMaterial selfMaterial = selfKt.getAxleMaterial();
            // 自身无效，则返回
            if (selfMaterial == AxleBlock.AxleMaterial.INVALID) {
                return;
            }

            final BlockPos beginPos;
            Direction.Axis axis = blockState.getValue(AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            // 寻找正方向轴的起点，否则以自身位置为起点
            TileEntity tile = level.getBlockEntity(selfPos.relative(positiveSide));
            if (testAxisAndAxleMaterial(tile, axis, selfMaterial)) {
                beginPos = ((KtMachineTile) tile).getMainBodyPosition();
            }
            else beginPos = selfPos;

            // 进行搜索和组合
            if (searchAndCombineAxles(level, beginPos, axis) < 0) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean testAxisAndAxleMaterial(TileEntity tile, Direction.Axis axis, AxleBlock.AxleMaterial material) {
        if (tile instanceof KtMachineTile) {
            KtMachineTile tileKt = (KtMachineTile) tile;
            return tileKt.getAxis() == axis && tileKt.getAxleMaterial() == material;
        }
        return false;
    }

    @SubscribeEvent
    public static void onAxleRemoveEvent(BlockEvent.BreakEvent event) {
        BlockState blockState = event.getState();
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity self = level.getBlockEntity(selfPos);
        if (self instanceof KtMachineTile) {
            KtMachineTile selfKt = (KtMachineTile) self;
            AxleBlock.AxleMaterial selfMaterial = selfKt.getAxleMaterial();
            // 自身无效，则返回
            if (selfMaterial == AxleBlock.AxleMaterial.INVALID) {
                return;
            }

            BlockPos beginPos;
            Direction.Axis axis = blockState.getValue(AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            Direction negativeSide = positiveSide.getOpposite();
            // 正方向进行重新合并
            TileEntity tile = level.getBlockEntity(selfPos.relative(positiveSide));
            if (testAxisAndAxleMaterial(tile, axis, selfMaterial)) {
                beginPos = ((KtMachineTile)tile).getMainBodyPosition();
                if (searchAndCombineAxles(level, beginPos, selfPos, axis) < 0) {
                    event.setCanceled(true);
                    return;
                }
            }
            // 负方向进行重新合并
            beginPos = selfPos.relative(negativeSide);
            tile = level.getBlockEntity(beginPos);
            if (testAxisAndAxleMaterial(tile, axis, selfMaterial)) {
                if (searchAndCombineAxles(level, beginPos, axis) < 0) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKtMachineAxleInsert(AxleInsertEvent event) {

        BlockState blockState = event.getState();
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity self = level.getBlockEntity(selfPos);
        if (self instanceof KtMachineTile && ((KtMachineTile) self).isKtValid()) {
            final BlockPos beginPos;
            Direction.Axis axis = blockState.getValue(AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            // 寻找正方向轴的起点，否则以自身位置为起点
            TileEntity tile = level.getBlockEntity(selfPos.relative(positiveSide));
            if (tile instanceof KtMachineTile && ((KtMachineTile) tile).getAxis() == axis) {
                beginPos = ((KtMachineTile)tile).getMainBodyPosition();
            }
            else beginPos = selfPos;
            // 进行搜索和组合
            if (searchAndCombineAxles(level, beginPos, axis) < 0) {
                event.setCanceled(true);
            }
        }

    }

    private static int searchAndCombineAxles(IWorld level, final BlockPos beginPos, Direction.Axis axis) {
        return searchAndCombineAxles(level, beginPos, null, axis);
    }

    private static int searchAndCombineAxles(IWorld level, final BlockPos beginPos, @Nullable final BlockPos endPos, Direction.Axis axis) {
        KtMachineTile[] axles = new KtMachineTile[AXLE_MAX_LENGTH];
        Direction negativeSide = AxisHelper.getAxisPositiveDirection(axis).getOpposite();
        BlockPos.Mutable pos = beginPos.mutable();
        TileEntity tile = level.getBlockEntity(pos);
        if (tile instanceof KtMachineTile) {
            KtMachineTile beginKtTile = (KtMachineTile) tile;
            AxleBlock.AxleMaterial beginMaterial = beginKtTile.getAxleMaterial();
            if (beginMaterial == AxleBlock.AxleMaterial.INVALID) {
                return 1;
            }
            // 从起点向负方向搜索
            int count = 0;
            while (testAxisAndAxleMaterial(tile, axis, beginMaterial) && !pos.equals(endPos)) {
                if (count >= beginMaterial.maxConnect) {
                    return -1;
                }
                axles[count++] = (KtMachineTile) tile;
                tile = level.getBlockEntity(pos.move(negativeSide));
            }

            // 开始进行组合
            long inertia = 0;
            for (int i = 0; i < count; ++i) {
                axles[i].setMainBodyPosition(beginPos);
                axles[i].setDirty(true);
                inertia += axles[i].selfInertia;
            }
            long sumInertia = inertia;
            axles[0].getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(axis)).ifPresent(body -> {
                body.setInertia(sumInertia);
                body.setOmega(0);
                body.setAngle(0);
            });

            return count;
        }
        return -1;
    }
}
