package photontech.block.kinetic;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import photontech.block.AxisAlignedBlock;
import photontech.init.PtCapabilities;
import photontech.init.PtItems;
import photontech.utils.helperfunctions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

public abstract class KtRotatingBlock extends AxisAlignedBlock {
    protected final long initInertia;
    protected final VoxelShape[] axleShapes;

    public KtRotatingBlock(double length, double width, long initInertia) {
        this(length, width, 0, initInertia);
    }

    public KtRotatingBlock(double length, double width, double offset, long initInertia) {
        super(length, width, offset);
        this.initInertia = initInertia;
        this.axleShapes = this.initAxleShapes();
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

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
                    axle.getMainKtTile().getRefKtTile().rotatingState.angularVelocity += 1F;
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
                    axle.getMainKtTile().getRefKtTile().rotatingState.angularVelocity -= 1F;
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.IRON_INGOT) {
                    LogManager.getLogger().info(axle.getMainKtTile().getRefKtTile().referenceState.toString());
                    LogManager.getLogger().info(axle.getMainKtTile().getRefKtTile().forceState.toString());
                    LogManager.getLogger().info(axle.getMainBodyPosition().toString());
                    LogManager.getLogger().info(axle.getAngle());
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

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return getShapeWithAxle(blockState, reader, pos, context);
    }

    @Nonnull
    public VoxelShape getShapeWithAxle(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        TileEntity te = reader.getBlockEntity(pos);
        if (te instanceof KtMachineTile) {
            if (!((KtMachineTile) te).getAxleBlockState().is(Blocks.AIR)) {
                return VoxelShapes.or(super.getShape(blockState, reader, pos, context), this.getAxleShape(blockState));
            }
        }
        return super.getShape(blockState, reader, pos, context);
    }

    public VoxelShape getAxleShape(BlockState blockState) {
        Direction.Axis axis = blockState.getValue(AXIS);
        return axleShapes[axis.ordinal()];
    }

    public VoxelShape[] initAxleShapes() {
        return this.initShapes(16, 4, 0);
    }

}
