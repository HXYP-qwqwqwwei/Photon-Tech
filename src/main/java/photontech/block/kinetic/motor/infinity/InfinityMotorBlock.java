package photontech.block.kinetic.motor.infinity;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KtRotatingBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfinityMotorBlock extends KtRotatingBlock {
    public InfinityMotorBlock() {
        super(12, 16, 2, 1);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinityMotorTile(initInertia);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderShape(@Nonnull BlockState blockState) {
        return BlockRenderType.MODEL;
    }


}
