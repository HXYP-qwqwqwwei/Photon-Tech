package photontech.block.kinetic;

import net.minecraft.block.Block;
import photontech.block.kinetic.axle.FullAxleBlock;

public interface IAxleBlockMaterial {

    enum AxleMaterial {
        INVALID(0, 0),
        WOOD(2, 4),
        IRON(16, 8),
        STEEL(16, 16);

        public final long initInertia;
        public final int maxConnect;

        AxleMaterial(long initInertia, int maxConnect) {
            this.initInertia = initInertia;
            this.maxConnect = maxConnect;
        }
    }

    AxleMaterial getMaterial();

    static AxleMaterial getMaterial(Block block) {
        if (block instanceof IAxleBlockMaterial) {
            return ((IAxleBlockMaterial) block).getMaterial();
        }
        return FullAxleBlock.AxleMaterial.INVALID;
    }
}
