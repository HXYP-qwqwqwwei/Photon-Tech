package photontech.block.electric;

import net.minecraft.tileentity.TileEntityType;
import photontech.utils.helper.MutableDouble;
import photontech.utils.tileentity.MachineTile;

public abstract class ElectricMachine extends MachineTile {

    public ElectricMachine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

}
