package photontech.block.kinetic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import photontech.block.kinetic.axle.FullAxleBlock;
import photontech.event.pt.KtEvent;
import photontech.init.PtCapabilities;
import photontech.item.ktblockitem.FullAxleBlockItem;
import photontech.utils.capability.ISaveLoad;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class KtMachineTile extends MachineTile {

    public static final String MAIN_BODY_POSITION = "MainBodyPosition";
    public static final String MAIN_ROTATING_STATE = "MainRotatingState";
    public static final String NEED_AXLE = "NeedAxle";
    public static final String AXLE_BLOCK_STATE = "AxleBlockState";
    public static final String KT_STATE = "KtState";
    public static final double DOUBLE_PI = Math.PI * 2;

    protected BlockPos mainBodyPosition = BlockPos.ZERO;
    protected BlockState axleBlockState = Blocks.AIR.defaultBlockState();

    public final KtRotatingState rotatingState;
    public final KtReferenceState ktReferenceState;

    protected boolean needAxle;

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        this(tileEntityTypeIn, initInertia, false);
    }

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn);
        this.ktReferenceState = new KtReferenceState(initInertia);
        this.rotatingState = new KtRotatingState();
        this.needAxle = needAxle;
    }

    public Direction.Axis getAxis() {
        return this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {

            if (this.worldPosition.equals(mainBodyPosition)) {
                if (this.ktReferenceState.refKtPos == this.worldPosition.asLong()) {
                    this.rotatingState.updateAngle();
                    this.setDirty(true);
                }
            }
//            this.getMainBody().ifPresent(body -> IRotateBody.kineticTransferWithEnv(body, 0.1));
            this.updateIfDirty();
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putLong(MAIN_BODY_POSITION, this.mainBodyPosition.asLong());
        nbt.putInt(AXLE_BLOCK_STATE, Block.getId(this.axleBlockState));
        nbt.putBoolean(NEED_AXLE, this.needAxle);
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            nbt.put(MAIN_ROTATING_STATE, this.rotatingState.save(new CompoundNBT()));
            nbt.put(KT_STATE, this.ktReferenceState.save(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.mainBodyPosition = BlockPos.of(nbt.getLong(MAIN_BODY_POSITION));
        this.needAxle = nbt.getBoolean(NEED_AXLE);
        this.axleBlockState = Block.stateById(nbt.getInt(AXLE_BLOCK_STATE));
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            this.rotatingState.load(nbt.getCompound(MAIN_ROTATING_STATE));
            this.ktReferenceState.load(nbt.getCompound(KT_STATE));
        }
    }

    public float getAngle() {
        if (!this.mainBodyPosition.equals(this.worldPosition)) {
            KtMachineTile mainKt = this.getMainKtTile();
            // 没有下面这句话客户端会爆栈，不知道为啥
            mainKt.setMainBodyPosition(mainKt.worldPosition);
            return mainKt.getAngle();
        }
        KtMachineTile refKt = this.getRefKtTile();
        this.fixRotatingState(refKt.ktReferenceState,refKt.rotatingState);
        return this.rotatingState.rotatingAngle;
    }


    @Nonnull
    public KtMachineTile getMainKtTile() {
        assert level != null;
        if (this.mainBodyPosition.equals(this.getBlockPos())) return this;
        TileEntity te = level.getBlockEntity(this.mainBodyPosition);
        if (te instanceof KtMachineTile) {
            return (KtMachineTile) te;
        }
        this.initAll();
        return this;
    }

    public KtMachineTile getRefKtTile() {
        assert this.level != null;
        TileEntity te = this.level.getBlockEntity(this.mainBodyPosition);
        if (!(te instanceof KtMachineTile)) return this;
        KtMachineTile kt = (KtMachineTile) te;
        if (kt.ktReferenceState.refKtPos != this.worldPosition.asLong()) {
            TileEntity refTE = level.getBlockEntity(BlockPos.of(kt.ktReferenceState.refKtPos));
            if (refTE instanceof KtMachineTile) {
                return (KtMachineTile) refTE;
            }
        }
        this.initRefState();
        return this;
    }

    public KtRotatingState getRotatingState() {
        return this.getMainKtTile().rotatingState;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        assert level != null;
        if (cap == PtCapabilities.ROTATING_STATE && this.isKtValid()) {
            if (this.isKtValidSide(side)) {
                if (this.mainBodyPosition.equals(this.worldPosition)) return LazyOptional.of(() -> this.rotatingState).cast();
                TileEntity tile = level.getBlockEntity(this.mainBodyPosition);
                if (tile instanceof KtMachineTile) {
                    return LazyOptional.of(() -> ((KtMachineTile)tile).rotatingState).cast();
                }
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    public abstract boolean isKtValidSide(Direction side);

    public BlockPos getMainBodyPosition() {
        return mainBodyPosition;
    }

    public void setMainBodyPosition(BlockPos mainBodyPosition) {
        this.mainBodyPosition = mainBodyPosition;
        this.setDirty(true);
    }

    public void insertAxle(FullAxleBlockItem item) {
        this.axleBlockState = item.getBlock().defaultBlockState().setValue(BlockStateProperties.AXIS, this.getAxis());
        this.mainBodyPosition = this.worldPosition;
        this.setDirty(true);
    }

    public void removeAxle() {
        this.axleBlockState = Blocks.AIR.defaultBlockState();
        MinecraftForge.EVENT_BUS.post(new KtEvent.KtInvalidateEvent(this));
        this.setDirty(true);
    }

    public boolean canAddAxle() {
        return needAxle && axleBlockState.is(Blocks.AIR);
    }

    public IAxleBlockMaterial.AxleMaterial getAxleMaterial() {
        if (this.needAxle) {
            return IAxleBlockMaterial.getMaterial(this.axleBlockState.getBlock());
        }
        return IAxleBlockMaterial.getMaterial(this.getBlockState().getBlock());
    }

    public boolean isKtValid() {
        return !canAddAxle();
    }

    public void departFromMainAxle() {
        this.setMainBodyPosition(this.worldPosition);
    }

    public void initAll() {
        this.departFromMainAxle();
        this.rotatingState.init();
        this.initRefState();
        this.setDirty(true);
    }

    public void initRefState() {
        this.ktReferenceState.init(this.worldPosition);
        this.setDirty(true);
    }


    public void fixRotatingState(KtReferenceState refState, KtRotatingState refRot) {
        if (this.ktReferenceState == refState) return;
        double fixedAngle = (refRot.rotatingAngle + DOUBLE_PI*refRot.rounds) * Math.pow(2, this.ktReferenceState.frequency);
        fixedAngle = (this.ktReferenceState.reversed ^ refState.reversed) ? -fixedAngle : fixedAngle;
        this.rotatingState.rotatingAngle = (float) (fixedAngle + this.ktReferenceState.phase);
        this.rotatingState.formatAngle();
    }

    public void addInertia(long i) {
        this.getMainKtTile().ktReferenceState.sumInertia += i;
    }

    public KtEvent.KtCreateEvent createKtCreateEvent() {
        return new KtEvent.KtCreateEvent(this);
    }

    @OnlyIn(Dist.CLIENT)
    public BlockState getAxleBlockState() {
        return this.axleBlockState.getBlock() instanceof FullAxleBlock ? this.axleBlockState.setValue(BlockStateProperties.AXIS, this.getAxis()) : this.axleBlockState;
    }

    public static class KtRotatingState implements ISaveLoad {

        public static final String ROTATING_ANGLE = "RotatingAngle";
        public static final String ANGULAR_VELOCITY = "AngularVelocity";
        public static final String ROUNDS = "Rounds";
        public static final String AXIAL_LENGTH = "AxialLength";


        public float rotatingAngle = 0;
        public float angularVelocity = 0;
        public int rounds = 0;
        public int axialLength = 1;

        public void init() {
            this.rotatingAngle = 0;
            this.angularVelocity = 0;
            this.axialLength = 1;
        }

        public void updateAngle() {
            this.rotatingAngle += this.angularVelocity * 0.05;
            this.formatAngle();
        }

        private void formatAngle() {
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

        @Override
        public void load(CompoundNBT nbt) {
            this.angularVelocity = nbt.getFloat(ANGULAR_VELOCITY);
            this.rotatingAngle = nbt.getFloat(ROTATING_ANGLE);
            this.rounds = nbt.getInt(ROUNDS);
            this.axialLength = nbt.getInt(AXIAL_LENGTH);
        }

        @Override
        public CompoundNBT save(CompoundNBT nbt) {
            nbt.putFloat(ANGULAR_VELOCITY, this.angularVelocity);
            nbt.putFloat(ROTATING_ANGLE, this.rotatingAngle);
            nbt.putInt(ROUNDS, this.rounds);
            nbt.putInt(AXIAL_LENGTH, this.axialLength);
            return nbt;
        }
    }

    public static class KtReferenceState implements ISaveLoad {

        public static final String INIT_INERTIA = "InitInertia";
        public static final String EXTRA_INERTIA = "SelfInertia";
        public static final String SUM_INERTIA = "SumInertia";
        public static final String EQUIVALENT_INERTIA = "EquivalentInertia";
        public static final String REF_KT_POS = "RefKtPos";
        public static final String FREQUENCY = "Frequency";
        public static final String PHASE = "Phase";
        public static final String REVERSED = "Reversed";

        public long initInertia;
        public long extraInertia = 0;
        public long sumInertia;
        public long equivalentInertia;
        public long refKtPos = 0;
        public int frequency = 0;
        public double phase = 0F;
        public boolean reversed = false;

        public KtReferenceState(long initInertia) {
            this.initInertia = initInertia;
            this.sumInertia = getSelfInertia();
            this.equivalentInertia = this.sumInertia;
        }

        public void init(BlockPos selfPosition) {
            this.sumInertia = getSelfInertia();
            this.frequency = 0;
            this.phase = 0F;
            this.reversed = false;
            this.equivalentInertia = this.sumInertia;
            this.refKtPos = selfPosition.asLong();
        }

        public long getSelfInertia() {
            return extraInertia + initInertia;
        }

        @Override
        public CompoundNBT save(CompoundNBT nbt) {
            nbt.putLong(INIT_INERTIA, this.initInertia);
            nbt.putLong(EXTRA_INERTIA, this.extraInertia);
            nbt.putLong(SUM_INERTIA, this.sumInertia);
            nbt.putLong(EQUIVALENT_INERTIA, this.equivalentInertia);
            nbt.putLong(REF_KT_POS, this.refKtPos);
            nbt.putInt(FREQUENCY, this.frequency);
            nbt.putDouble(PHASE, this.phase);
            nbt.putBoolean(REVERSED, this.reversed);
            return nbt;
        }

        @Override
        public void load(CompoundNBT nbt) {
            this.initInertia = nbt.getLong(INIT_INERTIA);
            this.extraInertia = nbt.getLong(EXTRA_INERTIA);
            this.sumInertia = nbt.getLong(SUM_INERTIA);
            this.equivalentInertia = nbt.getLong(EQUIVALENT_INERTIA);
            this.refKtPos = nbt.getLong(REF_KT_POS);
            this.frequency = nbt.getInt(FREQUENCY);
            this.phase = nbt.getDouble(PHASE);
            this.reversed = nbt.getBoolean(REVERSED);
        }

        @Override
        public String toString() {
            return "KtState{" +
                    "\n\tinitInertia=" + initInertia +
                    "\n\textraInertia=" + extraInertia +
                    "\n\tsumInertia=" + sumInertia +
                    "\n\tequivalentInertia=" + equivalentInertia +
                    "\n\trefKtPos=" + BlockPos.of(refKtPos).toShortString() +
                    "\n\tfrequency=" + frequency +
                    "\n\tphase=" + phase +
                    "\n\treversed=" + reversed +
                    "\n}";
        }
    }
}

