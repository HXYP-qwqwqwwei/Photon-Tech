package photontech.block.kinetic.motor.dcbrush;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import photontech.block.kinetic.DirectionalKineticRotatingBlock;
import photontech.init.PtItems;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DCBrushMotorCommutatorBlock extends DirectionalKineticRotatingBlock {

    public DCBrushMotorCommutatorBlock() {
        super(10, 10, 0, 10);
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DCBrushMotorCommutatorTile(initInertia);
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            DCBrushMotorCommutatorTile partA = (DCBrushMotorCommutatorTile) worldIn.getBlockEntity(pos);
            if (partA != null) {
                ItemStack itemStack = player.getItemInHand(handIn);
                if (itemStack.getItem() == PtItems.DC_MOTOR_BRUSH.get()) {
                    Direction.Axis axis = partA.getAxis();
                    Direction.Axis brushAxis = hit.getDirection().getAxis();
                    if (axis != brushAxis && partA.brushAxis == null) {
                        partA.setBrushAxis(brushAxis);
                        partA.setDirty(true);
                        updateNeighbors(state, worldIn, pos, AxisHelper.getVerticalDirections(axis));
                        return ActionResultType.SUCCESS;
                    }
                }
//                if (itemStack.getItem() == PtItems.WRENCH.get()) {
//                    partA.getCapability(PtCapabilities.ROTATING_STATE, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() + 1F));
//                    return ActionResultType.SUCCESS;
//                }
//                if (itemStack.getItem() == PtItems.PROTRACTOR.get()) {
//                    partA.getCapability(PtCapabilities.ROTATING_STATE, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> iRigidBody.setOmega(iRigidBody.getOmega() - 1F));
//                    return ActionResultType.SUCCESS;
//                }
//                if (itemStack.getItem() == Items.IRON_INGOT) {
////                    partA.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getInertia()));
//                    LogManager.getLogger().info("I=" + partA.I.value);
//                    return ActionResultType.SUCCESS;
//                }
//                if (itemStack.getItem() == Items.GOLD_INGOT) {
//                    partA.getCapability(PtCapabilities.ROTATING_STATE, AxisHelper.getAxisPositiveDirection(state.getValue(AXIS))).ifPresent(iRigidBody -> LogManager.getLogger().info(iRigidBody.getOmega()));
//                    return ActionResultType.SUCCESS;
//                }
            }
        }
        return ActionResultType.FAIL;
    }

    @SuppressWarnings("deprecation")
    protected static void updateNeighbors(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, Direction[] updateSides) {
        for (Direction direction : updateSides) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighbor = worldIn.getBlockState(neighborPos);
            BlockState updatedNeighbor = neighbor.getBlock().updateShape(neighbor, direction.getOpposite(), state, worldIn, neighborPos, pos);
            worldIn.setBlock(neighborPos, updatedNeighbor, 1);
            worldIn.sendBlockUpdated(neighborPos, neighbor, updatedNeighbor, Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

//    @Nonnull
//    @Override
//    public VoxelShape getShape(BlockState blockState, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
//        TileEntity te = reader.getBlockEntity(pos);
//        if (te instanceof KtMachineTile) {
//            if (!((KtMachineTile) te).getAxleBlockState().is(Blocks.AIR)) {
//                return VoxelShapes.or(super.getShape(blockState, reader, pos, context), this.getAxleShape(blockState));
//            }
//        }
//        return super.getShape(blockState, reader, pos, context);
//    }
//
//    public VoxelShape getAxleShape(BlockState blockState) {
//        Direction facing = blockState.getValue(FACING);
//        return axleShapes[facing.ordinal()];
//    }
}
