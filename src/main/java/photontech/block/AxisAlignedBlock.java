package photontech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;
import static net.minecraft.util.Direction.Axis.*;

public abstract class AxisAlignedBlock extends Block {
    private final VoxelShape[] shapes;

    public AxisAlignedBlock(double length, double width, double offset) {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.shapes = this.initShapes(length, width, offset);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(AXIS, X)
        );
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    }

    private VoxelShape[] initShapes(double length, double width, double offset) {
        VoxelShape[] shapes = new VoxelShape[3];
        double maxX = 8 + 0.5*width;
        double minX = 8 - 0.5*width;
        shapes[X.ordinal()] = Block.box(offset, minX, minX, length + offset, maxX, maxX);
        shapes[Y.ordinal()] = Block.box(minX, offset, minX, maxX, length + offset, maxX);
        shapes[Z.ordinal()] = Block.box(minX, minX, offset, maxX, maxX, length + offset);
        return shapes;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        Direction.Axis axis = blockState.getValue(AXIS);
        return shapes[axis.ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

}
