package photontech.data;

import photontech.utils.data.SaveLoadableWithRefCnt;

import java.util.function.Supplier;

public abstract class PtDataManagerWithRefCnt<T extends SaveLoadableWithRefCnt> extends PtDataManager<T> {
    PtDataManagerWithRefCnt(String name) {
        super(name);
    }

    public void put(int id, Supplier<T> supplier) {
        T val = this.get(id);
        if (val != null) {
            val.plusRef();
        }
        else super.put(id, supplier.get());
        this.setDirty();
    }

    /**
     * @deprecated use {@link PtDataManagerWithRefCnt#put(int, Supplier)} instead
     */
    @Override
    @Deprecated
    public void put(long id, T val) {
        super.put(id, val);
    }

    @Override
    public void remove(long id) {
        T val = this.get(id);
        if (val != null) {
            val.minusRef();
            if (val.isNoRef()) {
                super.remove(id);
            }
        }
        this.setDirty();
    }

}
