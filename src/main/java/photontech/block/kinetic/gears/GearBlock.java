package photontech.block.kinetic.gears;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import photontech.block.kinetic.KineticRotatingBlock;

import javax.annotation.Nullable;

public class GearBlock extends KineticRotatingBlock {
    public enum GearType {
        SMALL_GEAR,
        LARGE_GEAR
    }

    public static final IGearSupplier[] GEAR_SUPPLIERS;

    static {
        GEAR_SUPPLIERS = new IGearSupplier[GearType.values().length];
        GEAR_SUPPLIERS[GearType.SMALL_GEAR.ordinal()] = SmallGearTile::new;
        GEAR_SUPPLIERS[GearType.LARGE_GEAR.ordinal()] = LargeGearTile::new;
    }

    private final GearType type;

    public GearBlock(long initInertia, GearType type) {
        super(5, 16, initInertia);
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
