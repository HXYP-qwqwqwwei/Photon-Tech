package photontech.utils.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.data.PtDataManager;
import photontech.utils.IMixinWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ChainedUpdatingMachine extends TileEntity implements Comparable<ChainedUpdatingMachine> {
    public static final String CURRENT_ID = "CurrentID";
    public static final String NEW_ID = "NewID";
    private final boolean[] isValidSide;
    protected int currentID;
    protected int newID;

    public ChainedUpdatingMachine(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.isValidSide = initValidDirections();
    }

    public void update() {
        this.setID(this.newID);
    }

    private boolean upToDate() {
        return currentID == newID;
    }

    public void setNewID(int newID) {
        this.newID = newID;
    }

    @Nonnull
    public abstract Object getCapData();

    public abstract Capability<?> getUpdateCap();

    protected abstract void setID(int id);

    protected void removeDatas() {
        if (level != null && !level.isClientSide){
            this.getDataManager(level).remove(this.currentID);
        }
    }

    @Nonnull
    public abstract PtDataManager<?> getDataManager(IWorld world);

    public int getID() {
        return currentID;
    }

    @Override
    public int compareTo(ChainedUpdatingMachine o) {
        return this.currentID - o.currentID;
    }

    public ChainedUpdatingMachine getNewestNeighbor() {
        ChainedUpdatingMachine newestMachine = this;
        if (level == null) return newestMachine;
        for (Direction side : Direction.values()) {
            if (isValidSide[side.ordinal()]) {
                TileEntity tile = level.getBlockEntity(this.worldPosition.relative(side));
                if (tile instanceof ChainedUpdatingMachine && tile.getCapability(getUpdateCap(), side.getOpposite()).isPresent()) {
                    ChainedUpdatingMachine neighbor = (ChainedUpdatingMachine) tile;
                    if (newestMachine.compareTo(neighbor) < 0) {
                        newestMachine = neighbor;
                    }
                }
            }
        }
        return newestMachine;
    }

    protected boolean[] initValidDirections() {
        return new boolean[]{true, true, true, true, true, true};
    }

    public boolean isValidSide(Direction side) {
        return isValidSide[side.ordinal()];
    }

    public List<ChainedUpdatingMachine> neighborsNeedUpdate() {
        List<ChainedUpdatingMachine> machines = new ArrayList<>();
        ChainedUpdatingMachine newestNeighbor = getNewestNeighbor();
        if (this.compareTo(newestNeighbor) < 0) {
            machines.add(this);
        }
        if (level == null) return machines;

        for (Direction side : Direction.values()) {
            if (isValidSide[side.ordinal()]) {
                TileEntity tile = level.getBlockEntity(this.worldPosition.relative(side));
                if (tile instanceof ChainedUpdatingMachine && tile.getCapability(getUpdateCap(), side.getOpposite()).isPresent()) {
                    ChainedUpdatingMachine neighbor = (ChainedUpdatingMachine) tile;
                    if (neighbor.compareTo(newestNeighbor) < 0) {
                        machines.add(neighbor);
                        neighbor.setNewID(newestNeighbor.currentID);
                    }
                }
            }
        }
        return machines;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == getUpdateCap() && (side == null || isValidSide[side.ordinal()])) {
            return LazyOptional.of(this::getCapData).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void onLoad() {
        if (!this.upToDate() && level != null && !level.isClientSide) {
            ((IMixinWorld) level).updateChainedMachine(this);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt(CURRENT_ID, this.currentID);
        nbt.putInt(NEW_ID, this.newID);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, CompoundNBT nbt) {
        this.currentID = nbt.getInt(CURRENT_ID);
        this.newID = nbt.getInt(NEW_ID);
        super.load(state, nbt);
    }

    @Override
    public void setRemoved() {
        this.removeDatas();
        super.setRemoved();
    }
}
