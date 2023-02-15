package photontech.block.heater.photon;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtTileEntities;
import photontech.utils.tileentity.PhotonInstrument;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PhotonHeaterTile extends MachineTile implements PhotonInstrument {

    public PhotonHeaterTile() {
        super(PtTileEntities.PHOTON_HEATER_TILEENTITY.get());
        this.heatReservoir = LazyOptional.of(() -> this.createHeatReservoir(ENVIRONMENT_TEMPERATURE, 9999, 100, 100));

    }

    @Override
    public void tick() {
        this.heatExchangeWithEnvironment(this.getHeatReservoir());
    }

    @Override
    public void acceptPhotonPackFrom(@Nonnull PhotonPack pack, @Nullable Vector3d injectionVector) {
        this.heatReservoir.ifPresent(reservoir -> {
            reservoir.acceptHeat(pack.extractEnergy(photonLevel -> true), false);
        });
    }

    @Override
    public void radiatePhotonPackTo(@Nonnull PhotonPack pack, @Nullable Vector3d ejectionVector) {

    }
}
