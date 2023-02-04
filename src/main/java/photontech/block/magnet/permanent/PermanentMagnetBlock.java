package photontech.block.magnet.permanent;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.magnet.MagnetBlock;

import javax.annotation.Nullable;

public class PermanentMagnetBlock extends MagnetBlock {
    double magnetFluxDensity;
    public PermanentMagnetBlock(int width, double magneticFluxDensity) {
        super(width);
        this.magnetFluxDensity = magneticFluxDensity;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PermanentMagnet(this.magnetFluxDensity);
    }
}
