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
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;
import static photontech.utils.PtConstants.BlockStateProperties.*;

public abstract class DirectionalKineticRotatingBlock extends KineticRotatingBlock {


    public DirectionalKineticRotatingBlock(double length, double width, double offset, long initInertia) {
        super(length, width, offset, initInertia);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.X).setValue(REVERSED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(REVERSED));
    }

    @Override
    @SuppressWarnings("all")
    protected VoxelShape[] initShapes(double length, double width, double offset) {
        VoxelShape[] shapes = new VoxelShape[6];
        double maxX = 8 + 0.5*width;
        double minX = 8 - 0.5*width;
        shapes[Direction.Axis.X.ordinal()*2] = Block.box(16 - length + offset, minX, minX, 16 + offset, maxX, maxX);
        shapes[Direction.Axis.X.ordinal()*2 + 1] = Block.box(offset, minX, minX, length + offset, maxX, maxX);
        shapes[Direction.Axis.Y.ordinal()*2] = Block.box(minX, 16 - length + offset, minX, maxX, 16 + offset, maxX);
        shapes[Direction.Axis.Y.ordinal()*2 + 1] = Block.box(minX, offset, minX, maxX, length + offset, maxX);
        shapes[Direction.Axis.Z.ordinal()*2] = Block.box(minX, minX, 16 - length + offset, maxX, maxX, 16 + offset);
        shapes[Direction.Axis.Z.ordinal()*2 + 1] = Block.box(minX, minX, offset, maxX, maxX, length + offset);
        return shapes;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction clickedFace = context.getClickedFace();
        boolean reverse = context.getPlayer() != null && context.getPlayer().isShiftKeyDown();
        return this.defaultBlockState()
                .setValue(AXIS, clickedFace.getAxis())
                .setValue(REVERSED, reverse ^ !AxisHelper.isAxisPositiveDirection(clickedFace.getOpposite()));
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return getShapeWithAxle(blockState, reader, pos, context);
    }

    @Nonnull
    public VoxelShape getShapeWithAxle(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        Direction.Axis axis = blockState.getValue(AXIS);
        boolean reversed = blockState.getValue(REVERSED);
        TileEntity te = reader.getBlockEntity(pos);
        if (te instanceof KineticMachine) {
            if (!((KineticMachine) te).getAxleBlockState().is(Blocks.AIR)) {
                return VoxelShapes.or(shapes[axis.ordinal()*2 + (reversed ? 1 : 0)], this.getAxleShape(blockState));
            }
        }
        return shapes[axis.ordinal()*2 + (reversed ? 1 : 0)];
    }

    @Override
    public VoxelShape getAxleShape(BlockState blockState) {
        Direction.Axis axis = blockState.getValue(AXIS);
        boolean reversed = blockState.getValue(REVERSED);
        return axleShapes[axis.ordinal() * 2 + (reversed ? 1 : 0)];
    }

    public VoxelShape[] initAxleShapes() {
        return this.initShapes(16, 4, 0);
    }

}
