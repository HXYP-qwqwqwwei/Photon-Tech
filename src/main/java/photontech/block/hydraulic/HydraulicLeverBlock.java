package photontech.block.hydraulic;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class HydraulicLeverBlock extends HorizontalFaceBlock {
    protected static final VoxelShape NORTH_AABB = Block.box(5.0D, 4.0D, 10.0D, 11.0D, 12.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 6.0D);
    protected static final VoxelShape WEST_AABB = Block.box(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 11.0D);
    protected static final VoxelShape UP_AABB_Z = Block.box(5.0D, 0.0D, 4.0D, 11.0D, 6.0D, 12.0D);
    protected static final VoxelShape UP_AABB_X = Block.box(4.0D, 0.0D, 5.0D, 12.0D, 6.0D, 11.0D);
    protected static final VoxelShape DOWN_AABB_Z = Block.box(5.0D, 10.0D, 4.0D, 11.0D, 16.0D, 12.0D);
    protected static final VoxelShape DOWN_AABB_X = Block.box(4.0D, 10.0D, 5.0D, 12.0D, 16.0D, 11.0D);

    public HydraulicLeverBlock() {
        super(Properties.of(Material.DECORATION).strength(1).sound(SoundType.METAL).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(FACE, AttachFace.WALL));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, POWERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HydraulicLeverTile();
    }

    @SuppressWarnings("all")
    public VoxelShape getShape(BlockState blockState, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        switch(blockState.getValue(FACE)) {
            case FLOOR:
                switch(blockState.getValue(FACING).getAxis()) {
                    case X:
                        return UP_AABB_X;
                    case Z:
                    default:
                        return UP_AABB_Z;
                }
            case WALL:
                switch(blockState.getValue(FACING)) {
                    case EAST:
                        return EAST_AABB;
                    case WEST:
                        return WEST_AABB;
                    case SOUTH:
                        return SOUTH_AABB;
                    case NORTH:
                    default:
                        return NORTH_AABB;
                }
            case CEILING:
            default:
                switch(blockState.getValue(FACING).getAxis()) {
                    case X:
                        return DOWN_AABB_X;
                    case Z:
                    default:
                        return DOWN_AABB_Z;
                }
        }
    }

    @SuppressWarnings("all")
    public boolean canSurvive(BlockState state, IWorldReader reader, BlockPos pos) {
        return canAttach(reader, pos, getConnectedDirection(state).getOpposite());
    }


    public static boolean canAttach(IWorldReader reader, BlockPos pos, Direction side) {
        BlockPos blockpos = pos.relative(side);
        return !reader.getBlockState(blockpos).is(Blocks.AIR);
    }


    @Override
    @SuppressWarnings("all")
    public BlockState updateShape(BlockState currentBlockState, Direction updateSide, BlockState updateBlockState, IWorld level, BlockPos currentPos, BlockPos updatePos) {
        return currentBlockState;
    }

    @Override
    @SuppressWarnings("all")
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide) {
//            blockState.cycle(POWERED);
            return ActionResultType.SUCCESS;
        } else {
            BlockState blockstate = this.pull(blockState, world, pos);
            world.playSound(null, pos, blockstate.getValue(POWERED) ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.3F, 0.5F);
            return ActionResultType.CONSUME;
        }

    }

    public BlockState pull(BlockState blockState, World world, BlockPos pos) {
        blockState = blockState.cycle(POWERED);
        world.setBlock(pos, blockState, 3);
        return blockState;
    }

}
