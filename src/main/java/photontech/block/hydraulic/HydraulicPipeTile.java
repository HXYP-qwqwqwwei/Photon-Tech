package photontech.block.hydraulic;

import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import photontech.data.HydraulicDataManager;
import photontech.data.PtDataManager;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.HydraulicPipe;
import photontech.utils.data.Hydraulic;
import photontech.utils.tileentity.ChainedUpdatingMachine;

import javax.annotation.Nonnull;

public class HydraulicPipeTile extends ChainedUpdatingMachine {

    public HydraulicPipeTile() {
        super(PtTileEntities.HYDRAULIC_PIPE.get());
    }

    public void pressurize(int pressure) {
        this.getCapData().pressurize(pressure);
    }

    public void depressurize() {
        this.getCapData().depressurize();
    }

    public int getPressure() {
        return this.getCapData().getPressure();
    }


    @Nonnull
    @Override
    public Hydraulic getCapData() {
        return HydraulicDataManager.getData(level).getOrCreate(currentID, this::createHydraulicPipe);
    }

    protected Hydraulic createHydraulicPipe() {
        return HydraulicPipe.create();
    }

    @Override
    public Capability<?> getUpdateCap() {
        return PtCapabilities.HYDRAULIC_PIPE;
    }

    @Override
    protected void setID(int id) {
        HydraulicDataManager.getData(this.level).remove(this.currentID);
        this.currentID = id;
        HydraulicDataManager.getData(this.level).put(this.currentID, this::createHydraulicPipe);
        this.setChanged();
    }

    @Nonnull
    @Override
    public PtDataManager<?> getDataManager(IWorld world) {
        return HydraulicDataManager.getData(world);
    }
}
