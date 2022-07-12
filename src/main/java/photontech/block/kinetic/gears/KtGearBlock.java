package photontech.block.kinetic.gears;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KtRotatingBlock;

import javax.annotation.Nullable;

public class KtGearBlock extends KtRotatingBlock {
    public KtGearBlock(long initInertia) {
        super(5, 16, 5.5, initInertia);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new KtGearTile(this.initInertia, 1);
    }
}
