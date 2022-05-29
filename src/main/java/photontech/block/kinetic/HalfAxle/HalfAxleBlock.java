package photontech.block.kinetic.HalfAxle;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.HalfAxleTile;
import photontech.block.kinetic.IAxleBlockMaterial;
import photontech.block.kinetic.DirectionalKtRotatingBlock;
import photontech.block.kinetic.KtMachineTile;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.init.PtTileEntities;
import photontech.utils.helper_functions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;
import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class HalfAxleBlock extends DirectionalKtRotatingBlock implements IAxleBlockMaterial {

    protected final AxleMaterial material;

    public HalfAxleBlock(IAxleBlockMaterial.AxleMaterial material) {
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
                    axle.getCapability(PtCapabilities.RIGID_BODY, state.getValue(FACING)).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() + 1F));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, state.getValue(FACING)).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() - 1F));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.IRON_INGOT) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, state.getValue(FACING)).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getInertia()));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.GOLD_INGOT) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, state.getValue(FACING)).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getOmega()));
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;

    }

}
