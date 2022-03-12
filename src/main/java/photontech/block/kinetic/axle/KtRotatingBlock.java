package photontech.block.kinetic.axle;

import net.minecraft.block.BlockRenderType;
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
import photontech.block.AxisAlignedBlock;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.init.PtTileEntities;
import photontech.utils.helper_functions.AxisHelper;

import static net.minecraft.state.properties.BlockStateProperties.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KtRotatingBlock extends AxisAlignedBlock {
    protected final long initInertia;

    public KtRotatingBlock(double length, double width, long initInertia) {
        this(length, width, 0, initInertia);
    }

    public KtRotatingBlock(double length, double width, double offset, long initInertia) {
        super(length, width, offset);
        this.initInertia = initInertia;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new KtMachineTile(PtTileEntities.AXLE_TILE.get(), initInertia);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            KtMachineTile axle = (KtMachineTile) worldIn.getBlockEntity(pos);
            if (axle != null && !player.isShiftKeyDown()) {
                ItemStack itemStack = player.getItemInHand(handIn);
                if (itemStack.getItem() == PtItems.WRENCH.get()) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() + 1F));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() - 1F));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.IRON_INGOT) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getInertia()));
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.GOLD_INGOT) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getOmega()));
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }


    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(@Nonnull BlockState blockState, @Nonnull IBlockReader blockReader, @Nonnull BlockPos blockPos) {
        return 0.5F;
    }
}
