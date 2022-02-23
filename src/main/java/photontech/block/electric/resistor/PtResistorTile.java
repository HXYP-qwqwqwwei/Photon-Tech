package photontech.block.electric.resistor;

import net.minecraft.tileentity.TileEntityType;
import photontech.block.electric.PtElectricMachineTile;

public class PtResistorTile extends PtElectricMachineTile {
    final double initResistance;

    public PtResistorTile(TileEntityType<?> tileEntityTypeIn, double resistance) {
        super(tileEntityTypeIn);
        this.initResistance = resistance;
    }

    @Override
    public void tick() {

    }
}
