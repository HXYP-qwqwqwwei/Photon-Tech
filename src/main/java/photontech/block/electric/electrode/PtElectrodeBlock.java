package photontech.block.electric.electrode;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.Capability;
import photontech.block.crucible.PtCrucibleBlock;
import photontech.block.electric.IConductiveBlock;
import photontech.init.PtCapabilities;
import photontech.utils.block.PipeLikeBlock;
import photontech.utils.helper.AxisHelper;

import javax.annotation.Nonnull;

public class PtElectrodeBlock extends PipeLikeBlock implements IConductiveBlock {


    public PtElectrodeBlock(Thickness size) {
        super(PipeLikeBlock.Thickness.SIZE_1X, createMainShape(size), Properties.of(Material.STONE).strength(3).noOcclusion());
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(EAST, false)
                        .setValue(WEST, false)
                        .setValue(SOUTH, false)
                        .setValue(NORTH, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EAST).add(WEST).add(SOUTH).add(NORTH);
        super.createBlockStateDefinition(builder);
    }

    /**
     * 创建本体部分的碰撞箱
     * @param thickness 电极粗细
     * @return 本体的VoxelShape
     */
    private static VoxelShape createMainShape(Thickness thickness) {
        return VoxelShapes.or(
                Block.box(thickness.minX, -13, thickness.minX, thickness.maxX, 13, thickness.maxX),
                Block.box(thickness.minX-1, 6.5, thickness.minX-1, thickness.maxX+1, 9.5, thickness.maxX+1)
        );
    }

//    @Override
//    public boolean canConnectTo(IWorld world, BlockPos currentPos, Direction direction) {
//        BlockState blockState = world.getBlockState(currentPos.relative(direction));
//        return blockState.getBlock() instanceof IConductiveBlock;
//    }

    @Override
    protected Direction[] getValidDirections() {
        return AxisHelper.XZ_DIRECTIONS;
    }

    @Nonnull
    @Override
    protected Capability<?> getConnectCapability() {
        return PtCapabilities.CONDUCTOR;
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return this.canSurvive(p_196271_1_, p_196271_4_, p_196271_5_) ? super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState blockState, IWorldReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(Direction.DOWN)).getBlock() instanceof PtCrucibleBlock;
    }
}
