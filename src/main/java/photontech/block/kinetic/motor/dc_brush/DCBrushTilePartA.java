package photontech.block.kinetic.motor.dc_brush;

import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtTileEntities;

public class DCBrushTilePartA extends AxleTile {
    public DCBrushTilePartA(long initInertia) {
        super(PtTileEntities.DC_BRUSH_TILE_PART_A.get(), initInertia);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide()) {
            super.tick();
        }
    }
}
