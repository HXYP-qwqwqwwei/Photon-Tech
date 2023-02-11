package photontech.block.electric.infiniteBattery;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.AxisAlignedBlock;
import photontech.block.electric.Conductive;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nullable;

import static photontech.utils.PtConstants.BlockStateProperties.REVERSED;

public class InfiniteBatteryBlock extends AxisAlignedBlock implements Conductive {

    public InfiniteBatteryBlock() {
        super(16, 8, 0, Properties.of(Material.STONE).noOcclusion().lightLevel(state -> 7));
        this.registerDefaultState(this.getStateDefinition().any().setValue(REVERSED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(REVERSED);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    @SuppressWarnings("all")
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(REVERSED, !AxisHelper.isAxisPositiveDirection(context.getClickedFace()));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfiniteBatteryTile(10.0);
    }
}
