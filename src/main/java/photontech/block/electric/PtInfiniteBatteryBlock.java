package photontech.block.electric;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class PtInfiniteBatteryBlock extends Block {
    public PtInfiniteBatteryBlock() {
        super(Properties.of(Material.STONE).strength(3).noOcclusion());
    }

}
