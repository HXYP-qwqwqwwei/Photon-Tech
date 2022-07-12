package photontech.block.kinetic.gears;

import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import photontech.block.kinetic.FullAxleTile;
import photontech.event.pt_events.KtEvent;
import photontech.init.PtTileEntities;

public class KtGearTile extends FullAxleTile {

    protected int circumferenceLevel;

    public KtGearTile(long initInertia, int circumferenceLevel) {
        super(PtTileEntities.GEARS_TILEENTITY.get(), initInertia, true);
        this.circumferenceLevel = Math.max(circumferenceLevel, 1);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            if (flags == 1) {
                LogManager.getLogger().info("flags == 1, " + "game time: " + level.getGameTime());
                MinecraftForge.EVENT_BUS.post(new KtEvent.KtGearSynchronizeEvent(this));
                flags = 0;
                this.setDirty(true);
            }
        }
        super.tick();
    }

    @Override
    public KtEvent.KtCreateEvent createKtCreateEvent() {
        return new KtEvent.KtGearCreateEvent(this);
    }

    public int getCircumferenceLevel() {
        return circumferenceLevel;
    }

}
