package photontech.block.heater.solid;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import photontech.utils.capability.fluid.PtMultiFluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;
import static photontech.utils.PtConstants.*;

public class PtBurningItemHeaterBlock extends Block {

    public PtBurningItemHeaterBlock() {
        super(Properties.of(Material.STONE).strength(2).lightLevel(state -> 14).noOcclusion());
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(HORIZONTAL_FACING, Direction.EAST)
                .setValue(LIT, false)
                .setValue(HOLDING_INPUT, false)
        );
    }

    @Override
    public void stepOn(World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world.getBlockState(pos).getValue(LIT)) {
            if (!entity.fireImmune() && entity instanceof LivingEntity) {
                entity.setSecondsOnFire(8);
                entity.hurt(DamageSource.IN_FIRE, 2.0F);
            }
        }
        super.stepOn(world, pos, entity);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(LIT) ? super.getLightValue(state, world, pos) : 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PtBurningItemHeaterTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING).add(LIT).add(HOLDING_INPUT);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {

            PtBurningItemHeaterTile heater = (PtBurningItemHeaterTile) worldIn.getBlockEntity(pos);
            if (this.useWithFlit(heater, player, handIn)) {
                return ActionResultType.SUCCESS;
            }
            if (this.useWithWaterBucket(heater, player, handIn)) {
                return ActionResultType.SUCCESS;
            }

            assert heater != null;
            NetworkHooks.openGui((ServerPlayerEntity) player, heater, heater.getBlockPos());
        }
        return ActionResultType.SUCCESS;
    }

    private boolean useWithFlit(PtBurningItemHeaterTile heater, PlayerEntity player, Hand hand) {
        ItemStack holdItem = player.getItemInHand(hand);
        if (holdItem.getItem() == Items.FLINT_AND_STEEL) {
            holdItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            heater.setIgnited(true);
            return true;
        }
        return false;
    }

    private boolean useWithWaterBucket(PtBurningItemHeaterTile heater, PlayerEntity player, Hand hand) {
        ItemStack holdItem = player.getItemInHand(hand);
        if (holdItem.getItem() == Items.WATER_BUCKET) {
            if (heater.isIgnited()) {
                FluidUtil.interactWithFluidHandler(player, hand, new PtMultiFluidTank());
                heater.setIgnited(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRemove(BlockState state, @Nonnull World level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock()) {
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }
        PtBurningItemHeaterTile heater = (PtBurningItemHeaterTile) level.getBlockEntity(pos);
        if (heater != null) {
            IItemHandler handler = heater.getItemHandler();
            int size = handler.getSlots();
            for (int i = 0; i < size; ++i) {
                Block.popResource(level, pos, handler.getStackInSlot(i).copy());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
