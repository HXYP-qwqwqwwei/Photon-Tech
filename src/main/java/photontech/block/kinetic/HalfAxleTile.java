package photontech.block.kinetic;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class HalfAxleTile extends KineticMachine {
    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn, initInertia);
    }

    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn, initInertia, needAxle, ResistType.AXLE);
    }

    @Override
    public boolean isKtValidSide(Direction side) {
        return side != null && this.isActive() && side == this.getBlockState().getValue(BlockStateProperties.FACING);
    }

}
