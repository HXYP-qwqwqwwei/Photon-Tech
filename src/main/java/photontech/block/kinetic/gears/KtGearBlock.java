package photontech.block.kinetic.gears;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KtRotatingBlock;

import javax.annotation.Nullable;

public class KtGearBlock extends KtRotatingBlock {
    public enum GearType {
        SMALL_GEAR,
        LARGE_GEAR
    }

    public static final IGearSupplier[] GEAR_SUPPLIERS;

    static {
        GEAR_SUPPLIERS = new IGearSupplier[GearType.values().length];
        GEAR_SUPPLIERS[GearType.SMALL_GEAR.ordinal()] = KtSmallGearTile::new;
        GEAR_SUPPLIERS[GearType.LARGE_GEAR.ordinal()] = KtLargeGearTile::new;
    }

    private final GearType type;

    public KtGearBlock(long initInertia, GearType type) {
        super(5, 16, 5.5, initInertia);
        this.type = type;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return GEAR_SUPPLIERS[this.type.ordinal()].get(this.initInertia);
    }
}

interface IGearSupplier {
    TileEntity get(long initInertia);
}
