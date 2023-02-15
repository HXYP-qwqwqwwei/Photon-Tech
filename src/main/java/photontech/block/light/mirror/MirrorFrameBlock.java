package photontech.block.light.mirror;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class MirrorFrameBlock extends Block {
    private final VoxelShape[] shapes;

    public MirrorFrameBlock() {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(FACING, Direction.DOWN)
        );
        this.shapes = makeShapes();
    }

    protected VoxelShape[] makeShapes() {
        VoxelShape[] shapes = new VoxelShape[6];
        shapes[Direction.DOWN.ordinal()] = Block.box(1, 0, 1, 15, 14, 15);
        shapes[Direction.UP.ordinal()] = Block.box(1, 2, 1, 15, 16, 15);
        shapes[Direction.EAST.ordinal()] = Block.box(2, 1, 1, 16, 15, 15);
        shapes[Direction.WEST.ordinal()] = Block.box(0, 1, 1, 14, 15, 15);
        shapes[Direction.SOUTH.ordinal()] = Block.box(1, 1, 2, 15, 15, 16);
        shapes[Direction.NORTH.ordinal()] = Block.box(1, 1, 0, 15, 15, 14);
        return shapes;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MirrorFrameTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof MirrorFrameTile) {
                if (((MirrorFrameTile) te).installMirror(itemStack)) {
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    @SuppressWarnings("all")
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return shapes[state.getValue(FACING).ordinal()];
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getShadeBrightness(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return 0.5F;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }
}
