package photontech.block.electric.wire;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.utils.block.PipeLikeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtWireBlock extends PipeLikeBlock {

    public static final int BSIZE = 16;
    public static final int BZERO = 0;
    public static final int NSHAPES = 1 << 6;


    public final double resistivity;
    protected final VoxelShape[] shapes = new VoxelShape[NSHAPES];

    public PtWireBlock(Thickness thickness, double resistivity) {
        super(thickness, Properties.of(Material.WOOL).noOcclusion().strength(3));
        this.resistivity = resistivity >= 0 ? resistivity : 0;
        this.registerDefaultState(
                this.getStateDefinition().any()
                        .setValue(EAST, false)
                        .setValue(WEST, false)
                        .setValue(SOUTH, false)
                        .setValue(NORTH, false)
                        .setValue(UP, false)
                        .setValue(DOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(UP).add(DOWN);
        super.createBlockStateDefinition(builder);
    }

    @Nonnull
    @Override
    protected Capability<?> getConnectCapability() {
        return PtCapabilities.CONDUCTOR;
    }

    @Override
    protected Direction[] getValidDirections() {
        return Direction.values();
    }

//    @Override
//    public boolean canConnectTo(IWorld world, BlockPos pos, Direction direction) {
//        BlockState blockState = world.getBlockState(pos.relative(direction));
//        return blockState.getBlock() instanceof IConductiveBlock;
//    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        double d = this.thickness.maxX - this.thickness.minX;
        double S = d*d / 256;
        double R = resistivity / S;
        return new PtWireTile(1000.0, R);
    }

    @Nonnull
    @Override
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            PtWireTile wire = (PtWireTile) worldIn.getBlockEntity(pos);
            if (wire != null) {
                ItemStack itemStack = player.getItemInHand(handIn);
                if (itemStack.getItem() == Items.IRON_INGOT) {
                    LogManager.getLogger().info(wire.getI());
                    return ActionResultType.SUCCESS;
                }
                if (itemStack.getItem() == Items.GOLD_INGOT) {
                    wire.getCapability(PtCapabilities.CONDUCTOR).ifPresent(self -> {
                        LogManager.getLogger().info("Q = " + self.getQ());
                        LogManager.getLogger().info("U = " + self.getU());
                        LogManager.getLogger().info("R = " + self.getR());
                    });
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }
}
