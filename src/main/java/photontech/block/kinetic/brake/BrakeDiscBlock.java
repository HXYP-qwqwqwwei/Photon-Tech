package photontech.block.kinetic.brake;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KineticRotatingBlock;

import javax.annotation.Nullable;

public class BrakeDiscBlock extends KineticRotatingBlock {
    public BrakeDiscBlock(long initInertia) {
        super(4, 16, initInertia);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BrakeDiskTile(initInertia);
    }
}
