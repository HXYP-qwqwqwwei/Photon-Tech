package photontech.block.magnet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.AxisAlignedBlock;
import photontech.utils.helper_functions.AxisHelper;

import javax.annotation.Nullable;

import java.util.Objects;

import static photontech.utils.PtConstants.BlockStateProperties.*;

public abstract class MagnetBlock extends AxisAlignedBlock {
    public MagnetBlock(int width) {
        super(14, width, 1);
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(REVERSED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(REVERSED));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(REVERSED, !AxisHelper.isAxisPositiveDirection(context.getClickedFace()));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
