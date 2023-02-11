package photontech.utils.data;

public interface SaveLoadableWithRefCnt extends SaveLoadable {
    String REF_CNT = "RefCnt";

    boolean isNoRef();

    void plusRef();

    void minusRef();
}
