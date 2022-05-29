package photontech.block.electric.infiniteBattery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import photontech.block.AxisAlignedBlock;
import photontech.block.electric.IConductiveBlock;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class PtInfiniteBatteryBlock extends AxisAlignedBlock implements IConductiveBlock {

    public PtInfiniteBatteryBlock() {
        super(16, 8, 0, Properties.of(Material.STONE).noOcclusion().lightLevel(state -> 7));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PtInfiniteBatteryTile(10.0);
    }
}
