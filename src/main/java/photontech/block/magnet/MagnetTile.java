package photontech.block.magnet;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.capability.magnet.IMagnet;
import photontech.utils.capability.magnet.PtMagnet;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MagnetTile extends PtMachineTile {
    protected LazyOptional<IMagnet> NPole;
    protected LazyOptional<IMagnet> SPole;

    public MagnetTile(TileEntityType<?> tileEntityTypeIn, double B0) {
        super(tileEntityTypeIn);
        this.NPole = LazyOptional.of(() -> PtMagnet.create(IMagnet.MagneticPole.N, B0));
        this.SPole = LazyOptional.of(() -> PtMagnet.create(IMagnet.MagneticPole.S, B0));
    }

}
