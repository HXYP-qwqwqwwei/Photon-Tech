package photontech.utils.data;

public interface ISaveLoadWithRefCnt extends ISaveLoad {
    boolean isNoRef();

    void plusRef();

    void minusRef();
}
