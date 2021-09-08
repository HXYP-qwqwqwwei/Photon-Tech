package photontech.block.crucible;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;
import photontech.utils.block.IFluidTankBlock;

public class PtCrucibleBlock extends SixWayBlock implements IFluidTankBlock {
    private final VoxelShape[] shapes;
    protected final float overloadTemp;
    protected final float heatTranferRate;

    public PtCrucibleBlock(float overloadTemp, float heatTranferRate) {
        super(0, Properties.of(Material.STONE).strength(5).noOcclusion());
        this.shapes = this.makeShapes();
        this.overloadTemp = overloadTemp;
        this.heatTranferRate = heatTranferRate;
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(EAST, true)
                .setValue(WEST, true)
                .setValue(SOUTH, true)
                .setValue(NORTH, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EAST).add(WEST).add(SOUTH).add(NORTH);
        super.createBlockStateDefinition(builder);
    }

    protected VoxelShape[] makeShapes() {
        VoxelShape bottom = Block.box(0, 0, 0, 16, 2, 16);

        VoxelShape east = Block.box(14, 0, 0, 16, 16, 16);
        VoxelShape west = Block.box(0, 0, 0, 2, 16, 16);
        VoxelShape south = Block.box(0, 0, 14, 16, 16, 16);
        VoxelShape north = Block.box(0, 0, 0, 16, 16, 2);

        VoxelShape southEast = VoxelShapes.or(south, east);
        VoxelShape southWest = VoxelShapes.or(south, west);
        VoxelShape eastWest = VoxelShapes.or(east, west);
        VoxelShape northEast = VoxelShapes.or(north, east);
        VoxelShape northWest = VoxelShapes.or(north, west);
        VoxelShape southNorth = VoxelShapes.or(south, north);

        VoxelShape missEast = VoxelShapes.or(west, southNorth);
        VoxelShape missWest = VoxelShapes.or(east, southNorth);
        VoxelShape missSouth = VoxelShapes.or(eastWest, north);
        VoxelShape missNorth = VoxelShapes.or(eastWest, south);

        VoxelShape all = VoxelShapes.or(eastWest, southNorth);

        VoxelShape[] allVoxelShape = new VoxelShape[] {
                VoxelShapes.empty(), east, west, eastWest,
                south, southEast, southWest, missNorth,
                north, northEast, northWest, missSouth,
                southNorth, missWest, missEast, all
        };

        for (int i = 0; i < 16; ++i) {
            allVoxelShape[i] = VoxelShapes.or(allVoxelShape[i], bottom);
        }

        return allVoxelShape;
    }

    private boolean isConnectableCrucible(BlockState blockState) {
        return this == blockState.getBlock();
    }

    @Override
    protected int getAABBIndex(BlockState state) {
        int index = 0;
        boolean[] directions = {state.getValue(NORTH), state.getValue(SOUTH), state.getValue(WEST), state.getValue(EAST)};
        for (boolean direct : directions) {
            index <<= 1;
            if (direct) {
                index |= 1;
            }
        }
        return index;
    }

    private boolean canConnectTo(IWorld world, BlockPos pos) {
        return this.isConnectableCrucible(world.getBlockState(pos));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos currentPos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(EAST, !canConnectTo(world, currentPos.east()))
                .setValue(WEST, !canConnectTo(world, currentPos.west()))
                .setValue(SOUTH, !canConnectTo(world, currentPos.south()))
                .setValue(NORTH, !canConnectTo(world, currentPos.north()));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shapes[getAABBIndex(state)];
    }

    @Override
    @Nonnull
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return this.defaultBlockState()
                .setValue(EAST, !canConnectTo(worldIn, currentPos.east()))
                .setValue(WEST, !canConnectTo(worldIn, currentPos.west()))
                .setValue(SOUTH, !canConnectTo(worldIn, currentPos.south()))
                .setValue(NORTH, !canConnectTo(worldIn, currentPos.north()));
    }

    // Tile Entity
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PtCrucibleTileEntity(this.overloadTemp, this.heatTranferRate);
    }

    @Nonnull
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {

            if (useWithMilkBucket(worldIn, pos, player, handIn)) {
                return ActionResultType.SUCCESS;
            }
            else if (tryGetMilkBucket(worldIn, pos, player, handIn)) {
                return ActionResultType.SUCCESS;
            }
            else if (FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, null)) {
                worldIn.sendBlockUpdated(pos, state, state, Constants.BlockFlags.BLOCK_UPDATE);
                return ActionResultType.SUCCESS;
            }

            PtCrucibleTileEntity crucible = (PtCrucibleTileEntity)worldIn.getBlockEntity(pos);
            assert crucible != null;

            // GUI
            NetworkHooks.openGui((ServerPlayerEntity) player, crucible, crucible.getBlockPos());

        }
        return ActionResultType.SUCCESS;
    }

}
