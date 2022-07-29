package photontech.block.kinetic;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class FullAxleTile extends KtMachineTile {
    public FullAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn, initInertia);
    }

    public FullAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn, initInertia, needAxle);
    }

    @Override
    public boolean isKtValidSide(Direction side) {
        return side != null && side.getAxis() == this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

}
