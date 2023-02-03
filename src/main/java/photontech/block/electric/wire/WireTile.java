package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.ElectricMachine;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.data.DCWireDataManager;
import photontech.utils.data.electric.DCWireCapacitor;
import photontech.utils.data.electric.ICapacitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WireTile extends ElectricMachine {
    public static final String ID = "Id";
//    public static final String CAPACITY = "Capacity";
    public static final String OVERLOAD_CURRENT = "OverloadCurrent";
    protected int id = -1;
//    protected double capacity;
    protected double overloadCurrent;


    public WireTile(double overloadCurrent) {
        super(PtTileEntities.WIRE.get());
//        this.capacity = capacity;
        this.overloadCurrent = overloadCurrent;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState blockState = this.getBlockState();
            int maxID = this.id;
            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity tile = level.getBlockEntity(this.worldPosition.relative(side));
                    if (tile instanceof WireTile) {
                        maxID = Math.max(((WireTile) tile).id, maxID);
                    }
                }
            }
            if (this.id != maxID) {
                DCWireDataManager.getData(this.level).remove(this.id);
                this.id = maxID;
                DCWireDataManager.getData(this.level).put(this.id, () -> DCWireCapacitor.create(overloadCurrent));
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public ICapacitor createCapacitor() {
        return DCWireCapacitor.create(overloadCurrent);
    }

    @Nonnull
    protected ICapacitor getCapacitor() {
        return DCWireDataManager.getData(level).getOrCreate(id, this::createCapacitor);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.CONDUCTOR) {
            return LazyOptional.of(this::getCapacitor).cast();
        }
        else return super.getCapability(cap, side);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(ID, this.id);
//        nbt.putDouble(CAPACITY, this.capacity);
        nbt.putDouble(OVERLOAD_CURRENT, this.overloadCurrent);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.id = nbt.getInt(ID);
        super.load(state, nbt);
    }

    public double getOverloadCurrent() {
        return overloadCurrent;
    }

    @Override
    public void setRemoved() {
        if (level != null && !level.isClientSide){
            DCWireDataManager.getData(this.level).remove(this.id);
        }
        super.setRemoved();
    }
}
