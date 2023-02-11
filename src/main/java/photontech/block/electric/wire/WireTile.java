package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import photontech.data.DCWireDataManager;
import photontech.data.PtDataManager;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.data.electric.ElectricCapacitor;
import photontech.utils.data.electric.DCWireCapacitor;
import photontech.utils.tileentity.ChainedUpdatingMachine;

import javax.annotation.Nonnull;

public class WireTile extends ChainedUpdatingMachine {

    public static final String OVERLOAD_CURRENT = "OverloadCurrent";
    protected double overloadCurrent;


    public WireTile(double overloadCurrent) {
        super(PtTileEntities.WIRE.get());
        this.overloadCurrent = overloadCurrent;
    }

    @Override
    public Capability<?> getUpdateCap() {
        return PtCapabilities.CONDUCTOR;
    }

    @Override
    protected void setID(int id) {
        DCWireDataManager.getData(this.level).remove(this.currentID);
        this.currentID = id;
        DCWireDataManager.getData(this.level).put(this.currentID, this::createCapacitor);
        this.setChanged();
    }


    @Nonnull
    @Override
    public PtDataManager<?> getDataManager(IWorld world) {
        return DCWireDataManager.getData(world);
    }


    public ElectricCapacitor createCapacitor() {
        return DCWireCapacitor.create(overloadCurrent);
    }

    @Nonnull
    @Override
    public ElectricCapacitor getCapData() {
        return DCWireDataManager.getData(level).getOrCreate(currentID, this::createCapacitor);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putDouble(OVERLOAD_CURRENT, this.overloadCurrent);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.overloadCurrent = nbt.getDouble(OVERLOAD_CURRENT);
        super.load(state, nbt);
    }

    public double getOverloadCurrent() {
        return overloadCurrent;
    }

}
