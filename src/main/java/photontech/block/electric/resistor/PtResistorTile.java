package photontech.block.electric.resistor;

import net.minecraft.tileentity.TileEntityType;
import photontech.block.electric.ElectricMachine;

public class PtResistorTile extends ElectricMachine {
    final double initResistance;

    public PtResistorTile(TileEntityType<?> tileEntityTypeIn, double resistance) {
        super(tileEntityTypeIn);
        this.initResistance = resistance;
    }

    @Override
    public void tick() {

    }
}
