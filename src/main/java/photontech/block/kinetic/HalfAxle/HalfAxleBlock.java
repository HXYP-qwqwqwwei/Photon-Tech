package photontech.block.kinetic.HalfAxle;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.AxleMaterial;
import photontech.block.kinetic.DirectionalKtRotatingBlock;
import photontech.block.kinetic.HalfAxleTile;
import photontech.block.kinetic.IAxleBlock;
import photontech.init.PtTileEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

}
