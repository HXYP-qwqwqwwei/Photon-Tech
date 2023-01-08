package photontech.block.kinetic.HalfAxle;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.*;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.init.PtTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class HalfAxleBlock extends DirectionalKtRotatingBlock implements IAxleBlock {

    protected final AxleMaterial material;

    public HalfAxleBlock(AxleMaterial material) {
        super(8, 4, 0, material.initInertia >> 1);
        this.material = material;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HalfAxleTile(PtTileEntities.HALF_AXLE_TILE.get(), initInertia);
    }

    @Override
    public AxleMaterial getMaterial() {
        return material;
    }

    @Override
    public float getShadeBrightness(@Nonnull BlockState blockState, @Nonnull IBlockReader blockReader, @Nonnull BlockPos blockPos) {
        return 0.7F;
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            KtMachineTile axle = (KtMachineTile) worldIn.getBlockEntity(pos);
            if (axle != null && !player.isShiftKeyDown()) {
                ItemStack itemStack = player.getItemInHand(handIn);
                if (itemStack.getItem() == PtItems.WRENCH.get()) {
                    axle.getCapability(PtCapabilities.ROTATING_STATE, state.getValue(FACING)).ifPresent(rotatingState -> rotatingState.angularVelocity += 1F);
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
                    axle.getCapability(PtCapabilities.ROTATING_STATE, state.getValue(FACING)).ifPresent(rotatingState -> rotatingState.angularVelocity += 1F);
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.IRON_INGOT) {
                    LogManager.getLogger().info(axle.getMainKtTile().getRefKtTile().referenceState.toString());
                    LogManager.getLogger().info(axle.getMainBodyPosition().toString());
                    LogManager.getLogger().info(axle.getAngle());
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;

    }

}
