package photontech.block.magnet.permanent;

import photontech.block.magnet.MagnetTile;
import photontech.init.PtTileEntities;

public class PermanentMagnet extends MagnetTile {

    public PermanentMagnet(double magneticFluxDencity) {
        super(PtTileEntities.PERMANENT_MAGNET.get(), magneticFluxDencity);
    }

    @Override
    public void tick() {

    }


}
