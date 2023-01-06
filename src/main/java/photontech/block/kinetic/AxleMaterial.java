package photontech.block.kinetic;

public enum AxleMaterial {
    INVALID(0, 0),
    WOOD(2, 4),
    IRON(16, 8),
    STEEL(16, 16);

    public final long initInertia;
    public final int maxConnect;

    AxleMaterial(long initInertia, int maxConnect) {
        this.initInertia = initInertia;
        this.maxConnect = maxConnect;
    }
}
