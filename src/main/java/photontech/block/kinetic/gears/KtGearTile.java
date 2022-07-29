package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.KtMachineTile;
import photontech.event.pt_events.KtEvent;
import photontech.init.PtTileEntities;
import photontech.utils.PtConstants;

public class KtGearTile extends FullAxleTile {

    protected int circumferenceLevel;

    public KtGearTile(long initInertia, int circumferenceLevel) {
        super(PtTileEntities.GEARS_TILEENTITY.get(), initInertia, true);
        this.circumferenceLevel = Math.max(circumferenceLevel, 1);
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

//    @Override
//    public KtEvent.KtCreateEvent createKtCreateEvent() {
//        return new KtEvent.KtGearCreateEvent(this);
//    }

    public int getCircumferenceLevel() {
        return circumferenceLevel;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.worldPosition, this.worldPosition.offset(1, 1, 1));
    }
}
