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
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.utils.helper.AxisHelper;

import static net.minecraft.state.properties.BlockStateProperties.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AxleBlock extends Block {

    private final VoxelShape[] shapes;

    public AxleBlock() {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
        this.shapes = this.initShapes();
        this.registerDefaultState(
                this.getStateDefinition().any()
                .setValue(AXIS, Direction.Axis.X)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS));
    }

    private VoxelShape[] initShapes() {
        VoxelShape[] shapes = new VoxelShape[3];
        shapes[0] = Block.box(0, 6, 6, 16, 10, 10);
        shapes[1] = Block.box(6, 0, 6, 10, 16, 10);
        shapes[2] = Block.box(6, 6, 0, 10, 10, 16);
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
        return new AxleTile(100);
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
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
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
