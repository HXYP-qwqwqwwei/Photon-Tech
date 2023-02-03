package photontech.block.kinetic.axle;


import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.AxleMaterial;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.IAxleBlock;
import photontech.block.kinetic.KineticRotatingBlock;
import photontech.init.PtTileEntities;

import javax.annotation.Nullable;

public class FullAxleBlock extends KineticRotatingBlock implements IAxleBlock {

    protected final AxleMaterial material;

    public FullAxleBlock(AxleMaterial material) {
        super(16, 4, material.initInertia);
        this.material = material;
    }

    @Override
    public AxleMaterial getMaterial() {
        return material;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FullAxleTile(PtTileEntities.AXLE_TILE.get(), initInertia);
    }
}
