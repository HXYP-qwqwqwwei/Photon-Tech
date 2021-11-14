package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.axle.AxleBlock;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class DCMotorBlockPartA extends AxleBlock {

    public DCMotorBlockPartA() {
        super(16, 16);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(FACING, Direction.EAST)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite())
                .setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DCBrushTilePartA(200);
    }
}
