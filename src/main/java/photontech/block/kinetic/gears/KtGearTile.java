package photontech.block.kinetic.gears;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.KtMachineTile;
import photontech.event.pt.KtEvent;

public abstract class KtGearTile extends FullAxleTile {

    protected int radius;

    public KtGearTile(TileEntityType<?> tileEntityTypeIn, long initInertia, int radius) {
        super(tileEntityTypeIn, initInertia, true);
        this.radius = Math.max(radius, 1);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            KtMachineTile mainKt = this.getMainKtTile();
            long gt = level.getGameTime();
            if (mainKt.flags + 1 == gt) {
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeEvent(this));
                mainKt.setDirty(true);
            }
        }
        super.tick();
    }

    public abstract BlockPos[] getSearchPositions();

    public int getRadius() {
        return radius;
    }

    public int getTeethNumber() {
        return this.radius << 4; // radius * 16
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition, this.worldPosition.offset(1, 1, 1));
    }
}
