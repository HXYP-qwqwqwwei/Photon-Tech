package photontech.world_data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import photontech.utils.Utils;
import photontech.utils.capability.ISaveLoad;

import javax.annotation.Nonnull;
import java.lang.reflect.Array;
import java.util.*;

public abstract class PtComplexCapabilityData<T extends PtComplexCapabilityData.ISaveLoadWithID> extends WorldSavedData {
    protected PtCyclicList<T> datas = PtCyclicList.create();

    public PtComplexCapabilityData(String name) {
        super(name);
    }

    public abstract void load(@Nonnull CompoundNBT nbt);

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        for (ISaveLoadWithID value : datas.toArray()) {
            if (value != null) listNBT.add(value.save(new CompoundNBT()));
            else listNBT.add(new CompoundNBT());
        }
        nbt.put(this.getId(), listNBT);
        return nbt;
    }

    public T get(int id) {
        this.setDirty();
        return datas.get(id);
    }

    public void put(@Nonnull T value) {
        datas.put(Objects.requireNonNull(value));
        this.setDirty();
    }

    public void remove(int id) {
        datas.remove(id);
        this.setDirty();
    }

    public static interface ISaveLoadWithID extends ISaveLoad {
        int getID();

        void setID(int id);
    }

}

class PtCyclicList<T extends PtComplexCapabilityData.ISaveLoadWithID> {
    private Object[] values;
    private int count = 0;
    private int current = 0;
    public static final int MIN_SIZE = 128;

    protected PtCyclicList(int initSize) {
        this.values = new Object[initSize];
    }

    public static<T extends PtComplexCapabilityData.ISaveLoadWithID> PtCyclicList<T> create() {
        return new PtCyclicList<>(MIN_SIZE);
    }

    public static <T extends PtComplexCapabilityData.ISaveLoadWithID> PtCyclicList<T> create(int initSize) {
        return new PtCyclicList<>(Math.max(MIN_SIZE, initSize));
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) values[index];
    }

    public void remove(int index) {
        if (values[index] != null) {
            values[index] = null;
            count -= 1;
            // 占有率低于50%，则进行整理，并收缩大小
            if (count <= values.length / 2) {
                this.shrink();
            }
        }
    }

    public void put(T value) {
        int index = this.allocIndex();
        if (index < 0) {
            this.grow();
        }
        index = count;
        this.values[index] = value;
        value.setID(index);
        count += 1;
    }

    public void set(int index, T value) {
        this.values[index] = value;
    }

    /**
     * 在数组中找到一个空位
     * 下一次匹配，环状遍历数组，直到找到一个空的位置
     * @return 目标位置的索引，若没有空位返回-1
     */
    protected final int allocIndex() {
        int len = values.length;
        int ret = -1;
        for (int i = 0; i < len; ++i) {
            current = current == len ? 0 : current;
            if (values[current] == null) {
                ret = current;
            }
            current += 1;
        }
        return ret;
    }

    /**
     * 生长 1/4
     */
    protected void grow() {
        int oldSize = values.length;
        int newSize = oldSize + (oldSize << 2);
        Object[] grownValues = new Object[newSize];
        System.arraycopy(values, 0, grownValues, 0, oldSize);
        this.values = grownValues;
    }

    /**
     * 收缩 1/4
     */
    protected void shrink() {
        int oldSize = this.values.length;
        int newSize = oldSize - (oldSize << 2);
        Object[] shrunkValues = new Object[newSize];
        Object[] validValues = Arrays.stream(this.values).filter(Objects::nonNull).toArray();
        for (int i = 0; i < count; ++i) {
            ((PtComplexCapabilityData.ISaveLoadWithID) validValues[i]).setID(i);
        }
        System.arraycopy(validValues, 0, shrunkValues, 0, count);
        this.values = shrunkValues;
    }

    @SuppressWarnings("all")
    public PtComplexCapabilityData.ISaveLoadWithID[] toArray() {
        PtComplexCapabilityData.ISaveLoadWithID[] ret = (PtComplexCapabilityData.ISaveLoadWithID[]) Array.newInstance(PtComplexCapabilityData.ISaveLoadWithID.class, values.length);
        System.arraycopy(values, 0, ret, 0, values.length);
        return ret;
    }
}
