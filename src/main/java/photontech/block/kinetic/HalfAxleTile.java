package photontech.block.kinetic;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.helper_functions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HalfAxleTile extends KtMachineTile {
    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn, initInertia);
    }

    public HalfAxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn, initInertia, needAxle);
    }

    @Override
    protected boolean isKtValidSide(Direction side) {
        return side != null && side == this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public LazyOptional<IRotateBody> getMainBody() {
        return this.getCapability(PtCapabilities.RIGID_BODY, this.getBlockState().getValue(BlockStateProperties.FACING));
    }
}
