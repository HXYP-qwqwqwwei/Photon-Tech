package photontech.block.kinetic.motor.dcbrush;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KineticRotatingBlock;

import javax.annotation.Nullable;

public class DCBrushMotorCoilBlock extends KineticRotatingBlock {

    public DCBrushMotorCoilBlock() {
        super(16, 16, 20);
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DCBrushMotorCoilTile(initInertia);
    }
}
