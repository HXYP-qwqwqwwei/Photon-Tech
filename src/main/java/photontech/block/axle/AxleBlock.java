package photontech.block.axle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AxleBlock extends Block {
    public AxleBlock() {
        super(Properties.of(Material.STONE).strength(2).noOcclusion());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AxleTile();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
