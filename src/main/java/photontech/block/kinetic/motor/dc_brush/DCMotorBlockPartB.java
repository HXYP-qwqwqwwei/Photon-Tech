package photontech.block.kinetic.motor.dc_brush;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.axle.KtRotatingBlock;

import javax.annotation.Nullable;

public class DCMotorBlockPartB extends KtRotatingBlock {

    public DCMotorBlockPartB() {
        super(16, 16, 20);
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DCBrushTilePartB(initInertia);
    }
}
