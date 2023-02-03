package photontech.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import photontech.utils.PtNBTUtils;
import photontech.utils.data.ISaveLoad;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PtDataManager<T extends ISaveLoad> extends WorldSavedData {
    public static final String DATAS = "Datas";
    public static final String NEXT_ID = "NextID";

    protected Map<Long, T> datas = new HashMap<>();
    private int nextID = 0;

    PtDataManager(String name) {
        super(name);
    }

    @Nonnull
    public abstract Supplier<T> getSupplier();

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        this.datas = PtNBTUtils.loadMap(nbt, DATAS, getSupplier());
        this.nextID = nbt.getInt(NEXT_ID);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        PtNBTUtils.saveMap(nbt, DATAS, this.datas);
        nbt.putInt(NEXT_ID, this.nextID);
        return nbt;
    }

    public T get(long id) {
        this.setDirty();
        return datas.get(id);
    }

    @Nonnull
    public T getOrCreate(long id, Supplier<T> supplier) {
        return datas.computeIfAbsent(id, ignored -> supplier.get());
    }


    public void put(long id, T val) {
        datas.put(id, val);
        this.setDirty();
    }

    public void remove(long id) {
        datas.remove(id);
        this.setDirty();
    }

    public int allocateID() {
        this.setDirty();
        return this.nextID++;
    }

    public int getSize() {
        return datas.size();
    }

}
