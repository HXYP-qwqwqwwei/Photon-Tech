package photontech.block.kinetic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;
import static net.minecraft.state.properties.BlockStateProperties.FACING;

public abstract class DirectionalKtRotatingBlock extends KtRotatingBlock {


    public DirectionalKtRotatingBlock(double length, double width, double offset, long initInertia) {
        super(length, width, offset, initInertia);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    protected VoxelShape[] initShapes(double length, double width, double offset) {
        VoxelShape[] shapes = new VoxelShape[6];
        double maxX = 8 + 0.5*width;
        double minX = 8 - 0.5*width;
        shapes[Direction.EAST.ordinal()] = Block.box(16 - length + offset, minX, minX, 16 + offset, maxX, maxX);
        shapes[Direction.WEST.ordinal()] = Block.box(offset, minX, minX, length + offset, maxX, maxX);
        shapes[Direction.UP.ordinal()] = Block.box(minX, 16 - length + offset, minX, maxX, 16 + offset, maxX);
        shapes[Direction.DOWN.ordinal()] = Block.box(minX, offset, minX, maxX, length + offset, maxX);
        shapes[Direction.SOUTH.ordinal()] = Block.box(minX, minX, 16 - length + offset, maxX, maxX, 16 + offset);
        shapes[Direction.NORTH.ordinal()] = Block.box(minX, minX, offset, maxX, maxX, length + offset);
        return shapes;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction clickedFace = context.getClickedFace();
        boolean reverse = context.getPlayer() != null && context.getPlayer().isShiftKeyDown();
        return this.defaultBlockState()
                .setValue(FACING, reverse ? clickedFace : clickedFace.getOpposite())
                .setValue(AXIS, clickedFace.getAxis());
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return getShapeWithAxle(blockState, reader, pos, context);
    }

    @Nonnull
    public VoxelShape getShapeWithAxle(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        Direction facing = blockState.getValue(FACING);
        TileEntity te = reader.getBlockEntity(pos);
        if (te instanceof KtMachineTile) {
            if (!((KtMachineTile) te).getAxleBlockState().is(Blocks.AIR)) {
                return VoxelShapes.or(shapes[facing.ordinal()], this.getAxleShape(blockState));
            }
        }
        return shapes[facing.ordinal()];
    }

    public VoxelShape getAxleShape(BlockState blockState) {
        Direction facing = blockState.getValue(FACING);
        return axleShapes[facing.ordinal()];
    }

    public VoxelShape[] initAxleShapes() {
        return this.initShapes(16, 4, 0);
    }

}
