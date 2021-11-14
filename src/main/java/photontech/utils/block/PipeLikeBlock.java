package photontech.utils.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PipeLikeBlock extends SixWayBlock {
    public static final int BSIZE = 16;
    public static final int BZERO = 0;
    public static final int NSHAPES = 1 << 6;

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
        this.makeShapes(thickness, null);
    }

    public PipeLikeBlock(Thickness thickness, VoxelShape addedShape, Properties properties) {
        super(0, properties);
        this.makeShapes(thickness, addedShape);
    }

    abstract public boolean canConnectTo(IWorld world, BlockPos currentPos, Direction direction);

    abstract protected Direction[] getValidDirections();

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    /**
     * 构造管道型方块的碰撞箱
     * @param thickness 管道粗细参数。
     * @param addedShape 管道的基础碰撞箱，在管道具有一部分固定碰撞箱时使用。
     */
    protected void makeShapes(Thickness thickness, @Nullable VoxelShape addedShape) {
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
            if (addedShape != null) {
                shapes[index] = VoxelShapes.or(shapes[index], addedShape);
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
    public VoxelShape getShape(@Nonnull BlockState blockState, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
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
        IWorld world = context.getLevel();
        BlockPos currentPos = context.getClickedPos();
        BlockState state = this.defaultBlockState();
        for (Direction direction : this.getValidDirections()) {
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), canConnectTo(world, currentPos, direction));
        }
        return state;
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld world, BlockPos currentPos, BlockPos p_196271_6_) {
        BlockState state = this.defaultBlockState();
        for (Direction direction : this.getValidDirections()) {
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), canConnectTo(world, currentPos, direction));
        }
        return state;
    }


}
