package photontech.block.kinetic;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import photontech.utils.data.ISaveLoad;

import javax.annotation.Nonnull;

public class KineticState implements ISaveLoad {
    public static final double DOUBLE_PI = Math.PI * 2;

    public static final String ROTATING_ANGLE = "RotatingAngle";
    public static final String ROUNDS = "Rounds";
    public static final String AXIAL_LENGTH = "AxialLength";
    public static final String INIT_INERTIA = "InitInertia";
    public static final String EXTRA_INERTIA = "SelfInertia";
    public static final String AXIAL_SUM_INERTIA = "AxialSumInertia";
    public static final String INERTIA = "Inertia";
    public static final String PRIMARY_POS = "PrimaryPos";
    public static final String TERMINAL_POS = "TerminalPos";
    public static final String FREQUENCY_LEVEL = "FrequencyLevel";
    public static final String PHASE = "Phase";
    public static final String REVERSED = "Reversed";
    public static final String ANGULAR_VELOCITY = "AngularVelocity";

    // rotating
    protected float rotatingAngle = 0;
    protected float angularVelocity = 0;
    protected int rounds = 0;
    protected int axialLength = 1;

    // inertia
    protected long initInertia;
    protected long extraInertia = 0;
    protected long axialSumInertia;
    protected long inertia;

    // reference
    protected long primaryPos = 0;
    protected long terminalPos = 0;
    protected float phase = 0F;
    protected int frequencyLevel = 0;
    protected boolean reversed = false;

    // No need to save/load
    protected transient int force = 0;
    protected transient int resist = 0;
    protected transient int resConstant = 0;

    public KineticState(long initInertia) {
        this.initInertia = initInertia;
    }

    public void angularReset() {
//        this.rotatingAngle = (float) (- DOUBLE_PI + Math.random() * DOUBLE_PI * 2);
        this.rounds = 0;
    }

    public void primaryReset(BlockPos pos) {
        this.primaryPos = pos.asLong();
        this.frequencyLevel = 0;
        this.inertia = this.axialSumInertia;
        this.phase = 0;
        this.reversed = false;
        this.angularReset();
    }

    public void axialReset(BlockPos pos) {
        this.terminalPos = this.primaryPos = pos.asLong();
        this.axialSumInertia = this.getSelfInertia();
        this.axialLength = 1;
        this.primaryReset(pos);
    }

    // REFERENCE
    public BlockPos getPrimaryPos() {
        return BlockPos.of(this.primaryPos);
    }

    public BlockPos getTerminalPos() {
        return BlockPos.of(this.terminalPos);
    }

    // ROTATING
    public void updateAngle() {
        this.rotatingAngle += this.getAngularVelocity() * 0.05;
        this.formatAngle();
    }

    /**
     * 角度规约为[-2pi, +2pi]
     */
    public void formatAngle() {
        if (this.rotatingAngle > DOUBLE_PI) {
            rounds += rotatingAngle / DOUBLE_PI;
            this.rotatingAngle -= ((int) (rotatingAngle / DOUBLE_PI)) * DOUBLE_PI;
        }
        if (this.rotatingAngle < -DOUBLE_PI) {
            rounds += rotatingAngle / DOUBLE_PI;
            this.rotatingAngle = -this.rotatingAngle;
            this.rotatingAngle -= ((int) (rotatingAngle / DOUBLE_PI)) *  DOUBLE_PI;
            this.rotatingAngle = -this.rotatingAngle;
        }
    }

    public float getAngularVelocity() {
        return this.angularVelocity;
    }

    public long getMomentum() {
        return (long) (this.angularVelocity * this.inertia);
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getPhase() {
        return phase;
    }

    public int getFrequencyLevel() {
        return frequencyLevel;
    }

    public boolean isReversed() {
        return reversed;
    }

    public float getRotatingAngle() {
        return rotatingAngle;
    }

    public void setRotatingAngle(float rotatingAngle) {
        this.rotatingAngle = rotatingAngle;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    // INERTIA
    public long getSelfInertia() {
        return this.initInertia + this.extraInertia;
    }

    public long getAxialSumInertia() {
        return axialSumInertia;
    }

    public long getInertia() {
        return this.inertia;
    }

    public void setExtraInertia(long extraInertia) {
        this.extraInertia = extraInertia;
    }

    // FORCE
    public void clearAllForce() {
        this.force = 0;
        this.resist = 0;
        this.resConstant = 0;
    }

    public void addResist(int resist) {
        this.resist += resist;
    }

    public void addResConstant(int resConstant) {
        this.resConstant += resConstant;
    }

    public void addForce(int force) {
        this.force += force;
    }

    public ForceResult getFinalForce(float av) {
        int fluidResist = Math.max(1, (int)(0.001F * av * av * this.resConstant));
        int resist = this.resist + fluidResist;
        if (this.force <= 0) {
            if (-this.force <= resist) {
                return ForceResult.RESULT.setResistant(true).setForce(resist + force);
            }
            else return ForceResult.RESULT.setResistant(false).setForce(resist + force);
        }
        else {
            if (this.force <= resist) {
                return ForceResult.RESULT.setResistant(true).setForce(resist - force);
            }
            else return ForceResult.RESULT.setResistant(false).setForce(force - resist);
        }
    }


    public boolean axialConnectTo(KineticState parent, int maxLen) {
        if (parent == this) return false;
        if (parent.axialLength >= maxLen) return false;
        parent.axialLength += 1;
        parent.axialSumInertia += this.getSelfInertia();
        this.terminalPos = parent.terminalPos;
        this.primaryPos = parent.primaryPos;
        return true;
    }


    public void gearCombine(KineticState parent, int freqLevel, float phase, boolean reversed) {
        this.primaryPos = parent.primaryPos;
        this.reversed = reversed;
        this.phase = phase;
        this.frequencyLevel = freqLevel;
    }

    public boolean isPrimaryAt(BlockPos pos) {
        return this.primaryPos == pos.asLong();
    }

    public boolean isTerminalAt(BlockPos pos) {
        return this.terminalPos == pos.asLong();
    }


    @Override
    public void load(CompoundNBT nbt) {
        // rotating
        this.rotatingAngle = nbt.getFloat(ROTATING_ANGLE);
        this.angularVelocity = nbt.getFloat(ANGULAR_VELOCITY);
        this.rounds = nbt.getInt(ROUNDS);
        this.axialLength = nbt.getInt(AXIAL_LENGTH);

        // inertia
        this.initInertia = nbt.getLong(INIT_INERTIA);
        this.extraInertia = nbt.getLong(EXTRA_INERTIA);
        this.axialSumInertia = nbt.getLong(AXIAL_SUM_INERTIA);
        this.inertia = nbt.getLong(INERTIA);

        // reference
        this.primaryPos = nbt.getLong(PRIMARY_POS);
        this.terminalPos = nbt.getLong(TERMINAL_POS);
        this.phase = nbt.getFloat(PHASE);
        this.frequencyLevel = nbt.getInt(FREQUENCY_LEVEL);
        this.reversed = nbt.getBoolean(REVERSED);
    }

    @Nonnull
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        // rotating
        nbt.putFloat(ROTATING_ANGLE, this.rotatingAngle);
        nbt.putFloat(ANGULAR_VELOCITY, this.angularVelocity);
        nbt.putInt(ROUNDS, this.rounds);
        nbt.putInt(AXIAL_LENGTH, this.axialLength);

        // inertia
        nbt.putLong(INIT_INERTIA, this.initInertia);
        nbt.putLong(EXTRA_INERTIA, this.extraInertia);
        nbt.putLong(AXIAL_SUM_INERTIA, this.axialSumInertia);
        nbt.putLong(INERTIA, this.inertia);

        // reference
        nbt.putLong(PRIMARY_POS, this.primaryPos);
        nbt.putLong(TERMINAL_POS, this.terminalPos);
        nbt.putFloat(PHASE, this.phase);
        nbt.putInt(FREQUENCY_LEVEL, this.frequencyLevel);
        nbt.putBoolean(REVERSED, this.reversed);
        return nbt;
    }

    @Override
    public String toString() {
        return "KineticState{" +
                "\n    rotatingAngle=" + rotatingAngle +
                "\n    angularVelocity=" + getAngularVelocity() +
                "\n    rounds=" + rounds +
                "\n    axialLength=" + axialLength +
                "\n    initInertia=" + initInertia +
                "\n    extraInertia=" + extraInertia +
                "\n    axialSumInertia=" + axialSumInertia +
                "\n    inertia=" + inertia +
                "\n    primaryPos=" + primaryPos +
                "\n    terminalPos=" + terminalPos +
                "\n    phase=" + phase +
                "\n    frequencyLevel=" + frequencyLevel +
                "\n    reversed=" + reversed +
                "\n    force=" + force +
                "\n    resist=" + resist +
                "\n    resConstant=" + resConstant +
                "\n}";
    }
}
