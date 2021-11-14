package photontech.block.electric.wire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;
import photontech.block.electric.IConductiveBlock;
import photontech.utils.block.PipeLikeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtWireBlock extends PipeLikeBlock implements IConductiveBlock {

    public static final int BSIZE = 16;
    public static final int BZERO = 0;
    public static final int NSHAPES = 1 << 6;


    public final double resistor;
    protected final VoxelShape[] shapes = new VoxelShape[NSHAPES];

    public PtWireBlock(Thickness thickness, double resistor) {
        super(thickness, Properties.of(Material.WOOL).noOcclusion().strength(3));
        this.resistor = resistor >= 0 ? resistor : 0;
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
    protected Direction[] getValidDirections() {
        return Direction.values();
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos pos, Direction direction) {
        BlockState blockState = world.getBlockState(pos.relative(direction));
        return blockState.getBlock() instanceof IConductiveBlock;
    }

}
