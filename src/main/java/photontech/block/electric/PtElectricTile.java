package photontech.block.electric;

import net.minecraft.tileentity.TileEntityType;
import photontech.utils.tileentity.PtMachineTile;

public class PtElectricTile extends PtMachineTile {

    public PtElectricTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            
        }
    }
}
