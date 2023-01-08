package photontech.block.kinetic;

public enum ResistType {
    NO_RESIST(0, 0),
    AXLE(0, 16),
    SMALL_GEAR(0, 64),
    LARGE_GEAR(0, 256),
    NORMAL_MACHINE(32, 0),
    FAN(0, 1024)
    ;

    public final int initResist;
    // 阻力系数
    public final int resistConstant;
    ResistType(int initResist, int resistConstant) {
        this.initResist = initResist;
        this.resistConstant = resistConstant;
    }
}
