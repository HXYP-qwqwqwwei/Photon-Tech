package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
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
    // 保存在世界数据中的一个副本
    protected IEtCapacitor conductor;


    public PtWireTile(double capacity, double overloadEtCurrent) {
        super(PtTileEntities.WIRE.get());
        this.conductor = EtTransmissionLine.create(capacity, overloadEtCurrent);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
//            this.chargeExchangeByDirections(this.getValidDirections());
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            return LazyOptional.of(() -> EtTransmissionLineData.get(this.level).get(conductor.getID())).cast();
        }
        else return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.put("ConductorData", this.conductor.save(new CompoundNBT()));
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.conductor.load(nbt.getCompound("ConductorData"));
        super.load(state, nbt);
    }

    public Direction[] getValidDirections() {
        return Arrays.stream(Direction.values()).filter(direction -> this.getBlockState().getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction))).toArray(Direction[]::new);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            EtTransmissionLineData.get(this.level).put(conductor);
        }
    }

    @Override
    public void setRemoved() {
        if (level != null && !level.isClientSide){
            EtTransmissionLineData.get(this.level).remove(conductor.getID());
        }
        super.setRemoved();
    }
}
