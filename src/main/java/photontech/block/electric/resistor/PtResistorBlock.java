package photontech.block.electric.resistor;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.axle.AxleBlock;

import javax.annotation.Nullable;

public class PtResistorBlock extends AxleBlock {
    final double initResistance;

    public PtResistorBlock(double length, double width, double resistance) {
        super(length, width);
        this.initResistance = resistance;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return super.createTileEntity(state, world);
    }
}
