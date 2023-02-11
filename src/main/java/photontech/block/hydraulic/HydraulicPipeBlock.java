package photontech.block.hydraulic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import photontech.init.PtCapabilities;
import photontech.utils.block.PipeLikeBlock;

import javax.annotation.Nonnull;

public class HydraulicPipeBlock extends PipeLikeBlock {
    public HydraulicPipeBlock() {
        super(Thickness.SIZE_4X, Properties.of(Material.STONE).strength(2).noOcclusion());
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(EAST, false)
                        .setValue(WEST, false)
                        .setValue(SOUTH, false)
                        .setValue(NORTH, false)
                        .setValue(UP, false)
                        .setValue(DOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(UP).add(DOWN);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HydraulicPipeTile();
    }

    @Override
    protected Direction[] getValidDirections() {
        return Direction.values();
    }

    @Nonnull
    @Override
    protected Capability<?> getConnectCapability() {
        return PtCapabilities.HYDRAULIC_PIPE;
    }
}
