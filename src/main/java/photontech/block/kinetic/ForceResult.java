package photontech.block.kinetic;

public class ForceResult {
    public static final ForceResult RESULT = new ForceResult();
    // 是否为阻力
    public boolean isResistant;
    public int force;

    public ForceResult setForce(int force) {
        this.force = force;
        return this;
    }

    public ForceResult setResistant(boolean resistant) {
        isResistant = resistant;
        return this;
    }

    private ForceResult(){}
}
