package photontech.block.kinetic;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.capability.kinetic.IRotateBody;

public class HalfAxleTile extends KtMachineTile {
    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn, initInertia);
    }

    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn, initInertia, needAxle);
    }

    @Override
    public boolean isKtValidSide(Direction side) {
        return side != null && side == this.getBlockState().getValue(BlockStateProperties.FACING);
    }

}
