package photontech.block.axle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import static net.minecraft.state.properties.BlockStateProperties.*;

import javax.annotation.Nullable;

public class AxleBlock extends Block {

    private final VoxelShape[] shapes;

    public AxleBlock() {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.shapes = this.initShapes();
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(AXIS, Direction.Axis.X)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    }

    private VoxelShape[] initShapes() {
        VoxelShape[] shapes = new VoxelShape[3];
        shapes[0] = Block.box(0, 6, 6, 16, 10, 10);
        shapes[1] = Block.box(6, 0, 6, 10, 16, 10);
        shapes[2] = Block.box(6, 6, 0, 10, 10, 16);
        return shapes;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        Direction.Axis axis = blockState.getValue(AXIS);
        return shapes[axis.ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getNearestLookingDirection().getAxis());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AxleTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
