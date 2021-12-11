package photontech.block.kinetic.axle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
//import photontech.event.PtExtraThread;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.init.PtTileEntities;
import photontech.utils.helper.AxisHelper;

import static net.minecraft.state.properties.BlockStateProperties.*;
import static net.minecraft.util.Direction.Axis.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AxleBlock extends Block {

    private final VoxelShape[] shapes;

    public AxleBlock(double length, double width) {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.shapes = this.initShapes(length, width);
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(AXIS, X)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    }

    private VoxelShape[] initShapes(double length, double width) {
        VoxelShape[] shapes = new VoxelShape[3];
        double maxX = 8 + 0.5*width;
        double minX = 8 - 0.5*width;
        shapes[X.ordinal()] = Block.box(0, minX, minX, length, maxX, maxX);
        shapes[Y.ordinal()] = Block.box(minX, 0, minX, maxX, length, maxX);
        shapes[Z.ordinal()] = Block.box(minX, minX, 0, maxX, maxX, length);
        return shapes;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        Direction.Axis axis = blockState.getValue(AXIS);
        return shapes[axis.ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AxleTile(PtTileEntities.AXLE_TILE.get(), 100);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            AxleTile axle = (AxleTile) worldIn.getBlockEntity(pos);
            if (axle != null) {
                ItemStack itemStack = player.getItemInHand(handIn);
                if (itemStack.getItem() == PtItems.WRENCH.get()) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() + 0.1F));
//                    PtExtraThread.submitTask(() ->
//                    );
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() - 0.1F));
//                    PtExtraThread.submitTask(() ->
//                    );
                    axle.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() - 0.1F));
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
}
