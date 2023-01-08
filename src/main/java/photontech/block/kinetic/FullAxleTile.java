package photontech.block.kinetic;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class FullAxleTile extends KtMachineTile {
    public FullAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn, initInertia, false, ResistType.AXLE);
    }

    public FullAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle, ResistType type) {
        super(tileEntityTypeIn, initInertia, needAxle, type);
    }

    @Override
    public boolean isKtValidSide(Direction side) {
        return side != null && side.getAxis() == this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

}
