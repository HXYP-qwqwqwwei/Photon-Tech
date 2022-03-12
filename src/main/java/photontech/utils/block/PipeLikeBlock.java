/**
 * 类似于管道的方块
 * 这类方块拥有向周围连接的能力
 * Thickness决定其连接部分的粗细（碰撞箱）
 * 能连接的方向由getValidDirections定义
 * 连接的条件由canConnectTo定义
 */

package photontech.utils.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PipeLikeBlock extends SixWayBlock {
    public static final int BSIZE = 16;
    public static final int BZERO = 0;
    public static final int NSHAPES = 1 << 6;
    protected final Thickness thickness;

    public enum Thickness {
        SIZE_1X(7, 9),
        SIZE_4X(6, 10),
        SIZE_9X(5, 11),
        SIZE_16X(4, 12);

        public final int minX;
        public final int maxX;

        Thickness(int minX, int maxX) {
            this.minX = minX;
            this.maxX = maxX;
        }
    }

    protected VoxelShape[] shapes = new VoxelShape[NSHAPES];

    public PipeLikeBlock(Thickness thickness, Properties properties) {
        super(0, properties);
        this.thickness = thickness;
        this.makeShapes(thickness, null);
    }

    public PipeLikeBlock(Thickness thickness, VoxelShape addedShape, Properties properties) {
        super(0, properties);
        this.thickness = thickness;
        this.makeShapes(thickness, addedShape);
    }

    public final boolean canConnectTo(IWorld world, BlockPos currentPos, Direction direction) {
        TileEntity tile = world.getBlockEntity(currentPos.relative(direction));
        return tile != null && tile.getCapability(this.getConnectCapability(), direction.getOpposite()).isPresent();
    }

    abstract protected Direction[] getValidDirections();

    @Nonnull
    protected abstract Capability<?> getConnectCapability();

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    /**
     * 构造管道型方块的碰撞箱
     * @param thickness 管道粗细参数。
     * @param baseShape 管道的基础碰撞箱，在管道具有一部分固定碰撞箱时使用。
     */
    protected void makeShapes(Thickness thickness, @Nullable VoxelShape baseShape) {
        int minX = thickness.minX;
        int maxX = thickness.maxX;

        VoxelShape core = Block.box(minX, minX, minX, maxX, maxX, maxX);

        VoxelShape[] sixWayShapes = new VoxelShape[6];
        sixWayShapes[Direction.EAST.ordinal()] = Block.box(maxX, minX, minX, BSIZE, maxX, maxX);
        sixWayShapes[Direction.WEST.ordinal()] = Block.box(BZERO, minX, minX, minX, maxX, maxX);
        sixWayShapes[Direction.SOUTH.ordinal()] = Block.box(minX, minX, maxX, maxX, maxX, BSIZE);
        sixWayShapes[Direction.NORTH.ordinal()] = Block.box(minX, minX, BZERO, maxX, maxX, minX);
        sixWayShapes[Direction.UP.ordinal()] = Block.box(minX, maxX, minX, maxX, BSIZE, maxX);
        sixWayShapes[Direction.DOWN.ordinal()] = Block.box(minX, BZERO, minX, maxX, minX, maxX);

        for (int index = 0; index < NSHAPES; ++index) {
            shapes[index] = core;
            if (baseShape != null) {
                shapes[index] = VoxelShapes.or(shapes[index], baseShape);
            }
            for (int directionNumber = 0; directionNumber < 6; ++directionNumber) {
                if ((index & (1 << directionNumber)) != 0) {
                    shapes[index] = VoxelShapes.or(shapes[index], sixWayShapes[directionNumber]);
                }
            }
        }
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState blockState, @Nonnull IBlockReader blockReader, @Nonnull BlockPos pos, @Nonnull ISelectionContext selectionContext) {
        return this.shapes[this.getAABBIndex(blockState)];
    }

    @Override
    protected int getAABBIndex(@Nonnull BlockState blockState) {
        int index = 0;
        for (Direction direction : this.getValidDirections()) {
            if (blockState.getValue(PROPERTY_BY_DIRECTION.get(direction))) {
                index |= 1 << direction.ordinal();
            }
        }
        return index;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        IWorld level = context.getLevel();
        BlockPos currentPos = context.getClickedPos();
        BlockState state = this.defaultBlockState();
        for (Direction direction : this.getValidDirections()) {
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), canConnectTo(level, currentPos, direction));
        }
        return state;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState currentBlockState, @Nonnull Direction updateSide, @Nonnull BlockState updateBlockState, @Nonnull IWorld level, @Nonnull BlockPos currentPos, @Nonnull BlockPos updatePos) {
        return currentBlockState.setValue(PROPERTY_BY_DIRECTION.get(updateSide), canConnectTo(level, currentPos, updateSide));
    }


}
