package photontech.block.kinetic.motor.dc_brush;

import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtTileEntities;

public class DCBrushTilePartB extends AxleTile {

    public DCBrushTilePartB(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_B.get(), initInertia);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide()) {
            super.tick();
        }
    }
}
