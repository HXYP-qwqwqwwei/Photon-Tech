package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.block.electric.PtElectricMachineTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.EtTransmissionLine;
import photontech.utils.capability.electric.IEtCapacitor;
import photontech.world_data.EtTransmissionLineData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class PtWireTile extends PtElectricMachineTile {
    public static final String ID = "Id";
    protected int id = -1;
    protected final double capacity;
    protected final double overloadEtCurrent;


    public PtWireTile(double capacity, double overloadEtCurrent) {
        super(PtTileEntities.WIRE.get());
        this.capacity = capacity;
        this.overloadEtCurrent = overloadEtCurrent;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            if (id == -1) {
                EtTransmissionLineData data = EtTransmissionLineData.get(level);
                this.id = data.getNextID();
                data.put(id, EtTransmissionLine.create(capacity, overloadEtCurrent));
            }

        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            return LazyOptional.of(() -> EtTransmissionLineData.get(this.level).get(this.id)).cast();
        }
        else return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
//        nbt.put("ConductorData", this.conductor.save(new CompoundNBT()));
        nbt.putInt(ID, this.id);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
//        this.conductor.load(nbt.getCompound("ConductorData"));
        this.id = nbt.getInt(ID);
        super.load(state, nbt);
    }

    public Direction[] getValidDirections() {
        return Arrays.stream(Direction.values()).filter(direction -> this.getBlockState().getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction))).toArray(Direction[]::new);
    }

    @Override
    public void setRemoved() {
        if (level != null && !level.isClientSide){
            EtTransmissionLineData.get(this.level).remove(this.id);
        }
        super.setRemoved();
    }
}
