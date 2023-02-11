package photontech.block.kinetic.brake;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import photontech.init.PtBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;
import static net.minecraft.state.properties.BlockStateProperties.FACING;
import static photontech.utils.PtConstants.BlockStateProperties.AXIS_ROTATED;

public class BrakePadControllerBlock extends Block {
    private final VoxelShape[] shapes;

    public BrakePadControllerBlock() {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.shapes = this.initShapes();
        this.registerDefaultState(
                this.getStateDefinition().any().setValue(FACING, Direction.EAST).setValue(AXIS_ROTATED, false)
        );
    }

    @SuppressWarnings("all")
    private VoxelShape[] initShapes() {
        VoxelShape coreShape = Block.box(6, 6, 6, 10, 10, 10);
        VoxelShape[] shapes = new VoxelShape[12];
        shapes[Direction.EAST.ordinal()*2] = Stream.of(Block.box(9, 5, 12, 16, 11, 16), Block.box(9, 5, 0, 16, 11, 4), Block.box(7, 5, 0, 9, 11, 16)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.EAST.ordinal()*2+1] = Stream.of(Block.box(9, 0, 5, 16, 4, 11), Block.box(9, 12, 5, 16, 16, 11), Block.box(7, 0, 5, 9, 16, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.WEST.ordinal()*2] = Stream.of(Block.box(0, 5, 0, 7, 11, 4), Block.box(0, 5, 12, 7, 11, 16), Block.box(7, 5, 0, 9, 11, 16)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.WEST.ordinal()*2+1] = Stream.of(Block.box(0, 0, 5, 7, 4, 11), Block.box(0, 12, 5, 7, 16, 11), Block.box(7, 0, 5, 9, 16, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.SOUTH.ordinal()*2] = Stream.of(Block.box(0, 5, 9, 4, 11, 16), Block.box(12, 5, 9, 16, 11, 16), Block.box(0, 5, 7, 16, 11, 9)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.SOUTH.ordinal()*2+1] = Stream.of(Block.box(5, 12, 9, 11, 16, 16), Block.box(5, 0, 9, 11, 4, 16), Block.box(5, 0, 7, 11, 16, 9)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.NORTH.ordinal()*2] = Stream.of(Block.box(12, 5, 0, 16, 11, 7), Block.box(0, 5, 0, 4, 11, 7), Block.box(0, 5, 7, 16, 11, 9)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.NORTH.ordinal()*2+1] = Stream.of(Block.box(5, 12, 0, 11, 16, 7), Block.box(5, 0, 0, 11, 4, 7), Block.box(5, 0, 7, 11, 16, 9)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.UP.ordinal()*2] = Stream.of(Block.box(0, 9, 5, 4, 16, 11), Block.box(12, 9, 5, 16, 16, 11), Block.box(0, 7, 5, 16, 9, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.UP.ordinal()*2+1] = Stream.of(Block.box(5, 9, 12, 11, 16, 16), Block.box(5, 9, 0, 11, 16, 4), Block.box(5, 7, 0, 11, 9, 16)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.DOWN.ordinal()*2] = Stream.of(Block.box(12, 0, 5, 16, 7, 11), Block.box(0, 0, 5, 4, 7, 11), Block.box(0, 7, 5, 16, 9, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        shapes[Direction.DOWN.ordinal()*2+1] = Stream.of(Block.box(5, 0, 0, 11, 7, 4), Block.box(5, 0, 12, 11, 7, 16), Block.box(5, 7, 0, 11, 9, 16)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
        for (int i = 0; i < 12; ++i) {
            shapes[i] = VoxelShapes.or(shapes[i], coreShape);
        }
        return shapes;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BrakePadControllerTile();
    }

    @Override
    @Nonnull
    @SuppressWarnings("all")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        TileEntity te = worldIn.getBlockEntity(pos);
        VoxelShape pipeShape = VoxelShapes.empty();
        // 连接所需要的额外管道的碰撞箱
        if (te instanceof BrakePadControllerTile) {
            BrakePadControllerTile brakePadController = (BrakePadControllerTile) te;
            if (brakePadController.isConnected()) {
                Block pipeBlock = PtBlocks.HYDRAULIC_PIPE.get();
                BlockState pipeState = pipeBlock.defaultBlockState().setValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(brakePadController.getBackSide()), true);
                pipeShape = pipeBlock.getShape(pipeState, worldIn, pos, context);
            }
        }
        return VoxelShapes.or(pipeShape, this.shapes[state.getValue(FACING).ordinal()*2 + (state.getValue(AXIS_ROTATED) ? 1 : 0)]);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING).add(AXIS_ROTATED));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace().getOpposite();
        BlockState facingState = context.getLevel().getBlockState(pos.relative(facing));
        if (facingState.getBlock() instanceof BrakeDiscBlock) {
            Direction.Axis discAxis = facingState.getValue(AXIS);
            return this.defaultBlockState().setValue(FACING, facing).setValue(AXIS_ROTATED, shouldRotate(facing, discAxis));
        }
        return null;
    }

    private boolean shouldRotate(Direction facing, Direction.Axis discAxis) {
        return (discAxis == Direction.Axis.Y || (discAxis == Direction.Axis.Z && facing.getAxis() == Direction.Axis.Y));
    }

    @Nonnull
    @Override
    @SuppressWarnings("all")
    public BlockState updateShape(BlockState currentBlockState, Direction updateSide, BlockState updateBlockState, IWorld level, BlockPos currentPos, BlockPos updatePos) {
        return this.canSurvive(currentBlockState, level, currentPos) ? super.updateShape(currentBlockState, updateSide, updateBlockState, level, currentPos, updatePos) : Blocks.AIR.defaultBlockState();
    }


    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState blockState, IWorldReader world, BlockPos pos) {
        BlockState facingState = world.getBlockState(pos.relative(blockState.getValue(FACING)));
        if (facingState.getBlock() instanceof BrakeDiscBlock) {
            Direction.Axis discAxis = facingState.getValue(AXIS);
            Direction facing = blockState.getValue(FACING);
            return blockState.getValue(AXIS_ROTATED) == shouldRotate(facing, discAxis);
        }
        return false;
    }


    @Override
    public boolean propagatesSkylightDown(@Nonnull BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return false;
    }
}
