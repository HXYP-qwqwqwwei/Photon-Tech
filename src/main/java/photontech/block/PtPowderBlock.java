package photontech.block;

import net.minecraft.block.SandBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class PtPowderBlock extends SandBlock {
    public PtPowderBlock() {
        super(0, Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND).harvestTool(ToolType.AXE));
    }
}
