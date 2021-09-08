package photontech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtModifiedGlassBlock extends SixWayBlock {

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public PtModifiedGlassBlock() {
        super(0, Properties.of(Material.STONE)
                .strength(1)
                .sound(SoundType.GLASS)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion()
                .isSuffocating((state, level, pos) -> false));

        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(EAST, true)
                        .setValue(WEST, true)
                        .setValue(SOUTH, true)
                        .setValue(NORTH, true)
                        .setValue(UP, true)
                        .setValue(DOWN, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(UP).add(DOWN);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(EAST, this.canHideFace(world, pos.east()))
                .setValue(WEST, this.canHideFace(world, pos.west()))
                .setValue(SOUTH, this.canHideFace(world, pos.south()))
                .setValue(NORTH, this.canHideFace(world, pos.north()))
                .setValue(UP, this.canHideFace(world, pos.above()))
                .setValue(DOWN, this.canHideFace(world, pos.below()));
    }

    private boolean canHideFace(IWorld world, BlockPos pos) {
        return !(world.getBlockState(pos).getBlock() instanceof PtModifiedGlassBlock);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Nonnull
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return this.defaultBlockState()
                .setValue(EAST, this.canHideFace(worldIn, currentPos.east()))
                .setValue(WEST, this.canHideFace(worldIn, currentPos.west()))
                .setValue(SOUTH, this.canHideFace(worldIn, currentPos.south()))
                .setValue(NORTH, this.canHideFace(worldIn, currentPos.north()))
                .setValue(UP, this.canHideFace(worldIn, currentPos.above()))
                .setValue(DOWN, this.canHideFace(worldIn, currentPos.below()));
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0F;
    }
}
