package photontech.block.magnet.permanent;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.magnet.MagnetBlock;

import javax.annotation.Nullable;

public class PermanentMagnetBlock extends MagnetBlock {
    double B0;
    public PermanentMagnetBlock(int width, double B0) {
        super(width);
        this.B0 = B0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PermanentMagnetTile(this.B0);
    }
}
