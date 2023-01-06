package photontech.block.kinetic;

import net.minecraft.block.Block;

public interface IAxleBlock {

    AxleMaterial getMaterial();

    static AxleMaterial getMaterial(Block block) {
        if (block instanceof IAxleBlock) {
            return ((IAxleBlock) block).getMaterial();
        }
        return AxleMaterial.INVALID;
    }
}
