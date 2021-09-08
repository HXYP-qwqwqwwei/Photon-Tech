package photontech.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import net.minecraft.block.AbstractBlock.Properties;

public class PtNormalBlock extends Block {
    public PtNormalBlock(Properties properties) {
        super(properties);
    }

    public PtNormalBlock() {
        super(Properties.of(Material.STONE).strength(5));
    }
}
