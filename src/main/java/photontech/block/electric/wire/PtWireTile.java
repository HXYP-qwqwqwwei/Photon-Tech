package photontech.block.electric.wire;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.electric.EtMachineTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.world_data.EtTransmissionLineData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class PtWireTile extends EtMachineTile {
    public static final String ID = "Id";
    protected int id = -1;
    public final double capacity;
    public final double overloadEtCurrent;


    public PtWireTile(double capacity, double overloadEtCurrent) {
        super(PtTileEntities.WIRE.get());
        this.capacity = capacity;
        this.overloadEtCurrent = overloadEtCurrent;
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState blockState = this.getBlockState();
            int maxID = this.id;
            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity tile = level.getBlockEntity(this.worldPosition.relative(side));
                    if (tile instanceof PtWireTile) {
                        maxID = Math.max(((PtWireTile) tile).id, maxID);
                    }
                }
            }
            if (this.id != maxID) {
                EtTransmissionLineData.get(this.level).remove(this.id);
                this.id = maxID;
                EtTransmissionLineData.get(this.level).put(this.id, null);
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
        nbt.putInt(ID, this.id);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        this.id = nbt.getInt(ID);
        super.load(state, nbt);
    }

    @Override
    public void setRemoved() {
        if (level != null && !level.isClientSide){
            EtTransmissionLineData.get(this.level).remove(this.id);
        }
        super.setRemoved();
    }
}
