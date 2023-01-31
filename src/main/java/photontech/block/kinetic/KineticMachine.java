package photontech.block.kinetic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import photontech.block.kinetic.axle.FullAxleBlock;
import photontech.event.define.kinetic.KineticInvalidateEvent;
import photontech.event.define.kinetic.KineticPlaceEvent;
import photontech.init.PtCapabilities;
import photontech.item.ktblockitem.FullAxleBlockItem;
import photontech.utils.helper.fuctions.PtMath;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class KineticMachine extends MachineTile {

    public static final String NEED_AXLE = "NeedAxle";
    public static final String AXLE_BLOCK_STATE = "AxleBlockState";
    public static final String KINETIC_STATE = "KineticState";
    public static final String IS_INVALID = "IsInvalid";

    public static final Logger LOGGER = LogManager.getLogger();

    protected BlockState axleBlockState = Blocks.AIR.defaultBlockState();

    private boolean needAxle;
    private boolean isInvalid = false;

protected final KineticState state;

    public transient boolean expired = false;

    public KineticMachine(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        this(tileEntityTypeIn, initInertia, false, ResistType.NO_RESIST);
    }

    public KineticMachine(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle, ResistType type) {
        this(tileEntityTypeIn, initInertia, needAxle, type.initResist, type.resistConstant);
    }

    public KineticMachine(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle, int initResist, int fluidResistConstant) {
        super(tileEntityTypeIn);
        this.state = new KineticState(initInertia);
        this.needAxle = needAxle;
    }

    public void axialReset() {
        this.state.setAngularVelocity(this.getAngularVelocity());
        this.state.setRotatingAngle(this.getAngle());
        this.state.axialReset(this.worldPosition);
        this.setDirty(true);
    }

    public void primaryReset() {
        if (!this.isTerminal()) return;
        KineticMachine tm = this.getTerminal();
        tm.state.setAngularVelocity(tm.getAngularVelocity());
        tm.state.setRotatingAngle(tm.getAngle());
        tm.state.primaryReset(tm.worldPosition);
        tm.setDirty(true);
    }

    public void kineticInvalidate() {
        this.isInvalid = true;
    }


    @Override
    public void tick() {
        if (isServerSide()) {
//            this.applyAllForce();

            if (isPrimary()) {
                // 更新角度
                this.state.updateAngle();

                // 结算合力（摩擦阻力和动力）
                long inertia = this.state.getInertia();
                float av = this.state.getAngularVelocity();
                ForceResult forceResult = this.state.getFinalForce(av);
                float acc = 0.05F * forceResult.force / inertia;
                if (forceResult.isResistant) {
                    if (av <= 0) av = Math.min(0, av + acc);
                    else av = Math.max(0, av - acc);
                } else av += acc;

                this.state.setAngularVelocity(av);
                this.state.clearAllForce();

                this.setDirty(true);
            }
            this.updateIfDirty();
        }
    }

    /**
     * @return true if success
     */
    public boolean axialConnectTo(KineticMachine parent) {
        if (this.state.axialConnectTo(parent.getTerminal().state, this.getAxleMaterial().maxConnect)) {
            this.setDirty(true);
            return true;
        }
        return false;
    }

    public void gearCombine(KineticMachine parent, int frequencyLevel, float phase, boolean reversed) {
        this.getTerminal().state.gearCombine(parent.getTerminal().state, frequencyLevel, phase, reversed);
        // 计算等效的转动惯量
        int frequency = PtMath.pow2Int(frequencyLevel);
        KineticMachine primary = parent.getPrimary();
        primary.state.inertia += this.getAxialSumInertia() * frequency;
    }

    public boolean samePrimary(KineticMachine machine) {
        return this.getTerminal().state.getPrimaryPos().equals(machine.getTerminal().state.getPrimaryPos());
    }


    @Override
    public boolean isPrimary() {
        return this.isActive() && this.state.isPrimaryAt(this.worldPosition);
    }

    public boolean isTerminal() {
        return this.isActive() && this.state.isTerminalAt(this.worldPosition);
    }

    public Direction.Axis getAxis() {
        return this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

    public int getLength() {
        return state.axialLength;
    }

    public long getAxialSumInertia() {
        return this.getTerminal().state.getAxialSumInertia();
    }

    public float getAngularVelocity() {
        KineticMachine terminal = this.getTerminal();
        KineticMachine primary = terminal.getPrimary();
        return primary.state.getAngularVelocity() * terminal.getFrequency() * (terminal.reversed() ? -1 : 1);
    }

    public long getInertia() {
        KineticMachine terminal = this.getTerminal();
        KineticMachine primary = terminal.getPrimary();
        return primary.state.getInertia() / terminal.getFrequency();
    }

    public int getFrequency() {
        return PtMath.pow2Int(this.getTerminal().state.getFrequencyLevel());
    }

    public int getFreqLevel() {
        return this.getTerminal().state.getFrequencyLevel();
    }

    public boolean reversed() {
        return this.getTerminal().state.isReversed();
    }

    public long getMomentum() {
        return this.getPrimary().state.getMomentum() * (this.reversed() ? -1 : 1);
    }

//    public void setAngularMomentum(long L) {
//        this.state.momentum = L;
//    }

    public void setAngularVelocity(float av) {
        KineticMachine terminal = this.getTerminal();
        KineticMachine primary = terminal.getPrimary();
        primary.state.setAngularVelocity(av / terminal.getFrequency() * (terminal.reversed() ? -1 : 1));
    }

    public float getAngle() {
        if (!this.isActive()) return 0F;
        KineticMachine terminal = this.getTerminal();
        KineticMachine primary = terminal.getPrimary();
        terminal.fixRotatingState(primary.state);
        return terminal.state.getRotatingAngle();
    }

    public float getPhase() {
        return this.getTerminal().state.getPhase();
    }

    public void setPhase(float phase) {
        this.getTerminal().state.setPhase(phase);
    }

    @Nonnull
    public KineticMachine getTerminal() {
        assert level != null;
        if (this.state.isTerminalAt(this.getBlockPos())) return this;
        TileEntity te = level.getBlockEntity(this.state.getTerminalPos());
        if (te instanceof KineticMachine) {
            return (KineticMachine) te;
        }
        if (!level.isClientSide) {
            LOGGER.warn("Invalid terminal at {}.", this.state.getTerminalPos());
            this.state.axialReset(this.worldPosition);
            this.state.setAngularVelocity(0F);
        }
        return this;
    }

    public KineticMachine getPrimary() {
        assert this.level != null;
        KineticMachine terminal = this.getTerminal();
        BlockPos primaryPos = terminal.state.getPrimaryPos();
        if (!primaryPos.equals(terminal.worldPosition)) {
            TileEntity primary = level.getBlockEntity(primaryPos);
            if (primary instanceof KineticMachine) {
                return (KineticMachine) primary;
            }
            else if (!level.isClientSide){
                LOGGER.warn("Invalid primary machine at {}", primaryPos);
                terminal.state.primaryReset(terminal.worldPosition);
            }
        }
        return terminal;
    }


    public AxleMaterial getAxleMaterial() {
        if (this.needAxle) {
            return IAxleBlock.getMaterial(this.axleBlockState.getBlock());
        }
        return IAxleBlock.getMaterial(this.getBlockState().getBlock());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        assert level != null;
        if (cap == PtCapabilities.KINETIC_STATE && this.isActive()) {
            if (this.isKtValidSide(side)) {
                if (this.state.isTerminalAt(this.worldPosition)) return LazyOptional.of(() -> this.state).cast();
                TileEntity tile = level.getBlockEntity(this.state.getTerminalPos());
                if (tile instanceof KineticMachine) {
                    return LazyOptional.of(() -> ((KineticMachine)tile).state).cast();
                }
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }


    public abstract boolean isKtValidSide(Direction side);

    public boolean isActive() {
        return !(this.isInvalid || canAddAxle());
    }

    public boolean canAddAxle() {
        return needAxle && axleBlockState.is(Blocks.AIR);
    }

    public void insertAxle(FullAxleBlockItem item) {
        this.axleBlockState = item.getBlock().defaultBlockState().setValue(BlockStateProperties.AXIS, this.getAxis());
        this.state.setExtraInertia(IAxleBlock.getMaterial(item.getBlock()).initInertia);
//        this.terminalPos = this.worldPosition;
        this.setDirty(true);
    }

    public void removeAxle() {
        this.popItems(new ItemStack(this.axleBlockState.getBlock().asItem()));
        this.axleBlockState = Blocks.AIR.defaultBlockState();
        this.state.setExtraInertia(0L);
        MinecraftForge.EVENT_BUS.post(new KineticInvalidateEvent(this));
        this.setDirty(true);
    }

    public void fixRotatingState(KineticState refState) {
        if (this.state == refState) return;
        float fixedAngle = refState.getRotatingAngle() * this.getFrequency();
        fixedAngle = (this.state.isReversed() ^ refState.isReversed()) ? -fixedAngle : fixedAngle;
        this.state.rotatingAngle = fixedAngle + this.state.getPhase();
        this.state.formatAngle();
    }


    protected void applyAllForce(int force, int resist, int resConstant) {
        KineticMachine terminal = this.getTerminal();
        KineticMachine primary = terminal.getPrimary();
        int frequency = terminal.getFrequency();
        KineticState primaryState = primary.state;

        primaryState.addForce(force * frequency);
        primaryState.addResist(resist * frequency);
        primaryState.addResConstant(resConstant * frequency * frequency);
    }


    public KineticPlaceEvent createKtCreateEvent() {
        return new KineticPlaceEvent(this);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        // FIXME
        nbt.putInt(AXLE_BLOCK_STATE, Block.getId(this.axleBlockState));
        nbt.putBoolean(NEED_AXLE, this.needAxle);
        nbt.putBoolean(IS_INVALID, this.isInvalid);
        nbt.put(KINETIC_STATE, this.state.save(new CompoundNBT()));
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.axleBlockState = Block.stateById(nbt.getInt(AXLE_BLOCK_STATE));
        this.needAxle = nbt.getBoolean(NEED_AXLE);
        this.isInvalid = nbt.getBoolean(IS_INVALID);
        this.state.load(nbt.getCompound(KINETIC_STATE));
    }


    @OnlyIn(Dist.CLIENT)
    public BlockState getAxleBlockState() {
        return this.axleBlockState.getBlock() instanceof FullAxleBlock ? this.axleBlockState.setValue(BlockStateProperties.AXIS, this.getAxis()) : this.axleBlockState;
    }

}


