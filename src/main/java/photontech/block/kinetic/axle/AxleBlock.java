package photontech.block.kinetic.axle;

import net.minecraft.block.Block;

public class AxleBlock extends KtRotatingBlock {

    public enum AxleMaterial {
        INVALID(0, 0),
        WOOD(64, 4),
        IRON(512, 8),
        STEEL(512, 16);

        public final long initInertia;
        public final int maxConnect;

        AxleMaterial(long initInertia, int maxConnect) {
            this.initInertia = initInertia;
            this.maxConnect = maxConnect;
        }
    }

    public final AxleMaterial material;

    public AxleBlock(AxleMaterial material) {
        super(16, 4, material.initInertia);
        this.material = material;
    }

    public static AxleMaterial getMaterial(Block block) {
        if (block instanceof AxleBlock) {
            return ((AxleBlock) block).material;
        }
        return AxleMaterial.INVALID;
    }
}
