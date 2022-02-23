package photontech.event;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtCapabilities;
import photontech.utils.helper.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEventHandler {
    public static final int AXLE_MAX_LENGTH = 8;

    @SubscribeEvent
    public static void onAxlePlaceEvent(@Nonnull BlockEvent.EntityPlaceEvent event) {
        BlockState blockState = event.getState();
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity self = level.getBlockEntity(selfPos);
        if (self instanceof AxleTile) {
            final BlockPos beginPos;
            Direction.Axis axis = blockState.getValue(AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            // 寻找正方向轴的起点，否则以自身位置为起点
            TileEntity tile = level.getBlockEntity(selfPos.relative(positiveSide));
            if (tile instanceof AxleTile && ((AxleTile) tile).getAxis() == axis) {
                beginPos = ((AxleTile)tile).getMainBodyPosition();
            }
            else beginPos = selfPos;
            // 进行搜索和组合
            if (searchAndCombineAxles(level, beginPos, axis) <= 0) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAxleRemoveEvent(BlockEvent.BreakEvent event) {
        BlockState blockState = event.getState();
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        TileEntity self = level.getBlockEntity(selfPos);
        if (self instanceof AxleTile) {
            BlockPos beginPos;
            Direction.Axis axis = blockState.getValue(AXIS);
            Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
            Direction negativeSide = positiveSide.getOpposite();
            // 正方向进行重新合并
            TileEntity tile = level.getBlockEntity(selfPos.relative(positiveSide));
            if (tile instanceof AxleTile && ((AxleTile) tile).getAxis() == axis) {
                beginPos = ((AxleTile)tile).getMainBodyPosition();
                if (searchAndCombineAxles(level, beginPos, selfPos, axis) <= 0) {
                    event.setCanceled(true);
                    return;
                }
            }
            // 负方向进行重新合并
            beginPos = selfPos.relative(negativeSide);
            tile = level.getBlockEntity(beginPos);
            if (tile instanceof AxleTile && ((AxleTile) tile).getAxis() == axis) {
                if (searchAndCombineAxles(level, beginPos, axis) <= 0) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private static int searchAndCombineAxles(IWorld level, final BlockPos beginPos, Direction.Axis axis) {
        return searchAndCombineAxles(level, beginPos, null, axis);
    }

    private static int searchAndCombineAxles(IWorld level, final BlockPos beginPos, @Nullable final BlockPos endPos, Direction.Axis axis) {
        AxleTile[] axles = new AxleTile[AXLE_MAX_LENGTH];
        Direction negativeSide = AxisHelper.getAxisPositiveDirection(axis).getOpposite();
        BlockPos.Mutable pos = beginPos.mutable();
        TileEntity tile = level.getBlockEntity(pos);
        // 从起点向负方向搜索
        int count = 0;
        while (tile instanceof AxleTile && ((AxleTile) tile).getAxis() == axis && !pos.equals(endPos)) {
            if (count >= AXLE_MAX_LENGTH) {
                return -1;
            }
            axles[count++] = (AxleTile) tile;
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
}
