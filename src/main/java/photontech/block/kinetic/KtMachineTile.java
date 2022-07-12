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
import photontech.event.pt_events.KtEvent;
import photontech.init.PtCapabilities;
import photontech.item.ktblockitem.FullAxleBlockItem;
import photontech.utils.capability.ISaveLoad;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.KtRotateBody;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class KtMachineTile extends MachineTile {

    public static final String MAIN_BODY_POSITION = "MainBodyPosition";
    public static final String MAIN_BODY = "MainBody";
    public static final String NEED_AXLE = "NeedAxle";
    public static final String AXLE_BLOCK_STATE = "AxleBlockState";
    public static final String KT_STATUE = "KtStatue";

    protected BlockPos mainBodyPosition = BlockPos.ZERO;
    protected final LazyOptional<IRotateBody> mainBody;
    protected BlockState axleBlockState = Blocks.AIR.defaultBlockState();

    public final KtStatue ktStatue;

    protected boolean needAxle;

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        this(tileEntityTypeIn, initInertia, false);
    }

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn);
        this.ktStatue = new KtStatue(initInertia);
        this.mainBody = LazyOptional.of(() -> KtRotateBody.create(initInertia));
        this.needAxle = needAxle;
    }

    public Direction.Axis getAxis() {
        return this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {

            if (this.worldPosition.equals(mainBodyPosition)) {
                if (this.ktStatue.refKtPos == this.worldPosition.asLong()) {
                    mainBody.ifPresent(body -> {
                        body.updateAngle();
                        this.ktStatue.angle = body.getAngle();
                        this.setDirty(true);
                    });
                }
                else {
                    BlockPos refPos = BlockPos.of(this.ktStatue.refKtPos);
                    TileEntity te = level.getBlockEntity(refPos);
                    if (te instanceof KtMachineTile) {
                        this.mainBody.ifPresent(body -> {
                            KtMachineTile kt = (KtMachineTile) te;
                            body.setAngle(this.ktStatue.getFixedAngle(kt.ktStatue));
                        });
                    }
                    else {
                        this.ktStatue.refKtPos = this.worldPosition.asLong();
                        MinecraftForge.EVENT_BUS.post(this.createKtCreateEvent());
                    }
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
            this.saveCap(mainBody, MAIN_BODY, nbt);
            nbt.put(KT_STATUE, this.ktStatue.save(new CompoundNBT()));
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
            this.loadCap(mainBody, MAIN_BODY, nbt);
            this.ktStatue.load(nbt.getCompound(KT_STATUE));
        }
    }

    public float getAngle() {
        return this.ktStatue.getFixedAngle(this.getRefKtStatue());
    }

    public KtStatue getRefKtStatue() {
        assert this.level != null;
        TileEntity te = this.level.getBlockEntity(this.mainBodyPosition);
        if (!(te instanceof KtMachineTile)) return KtStatue.INVALID;
        KtMachineTile kt = (KtMachineTile) te;
        if (kt.ktStatue.refKtPos != this.worldPosition.asLong()) {
            TileEntity refTE = level.getBlockEntity(BlockPos.of(kt.ktStatue.refKtPos));
            if (refTE instanceof KtMachineTile) {
                return ((KtMachineTile) refTE).ktStatue;
            }
        }
        return this.ktStatue;
    }

    public LazyOptional<IRotateBody> getMainBody() {
        return this.getMainKtTile().mainBody;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        assert level != null;
        if (cap == PtCapabilities.RIGID_BODY && this.isKtValid()) {
            if (this.isKtValidSide(side)) {
                if (this.mainBodyPosition.equals(this.worldPosition)) return this.mainBody.cast();
                TileEntity tile = level.getBlockEntity(this.mainBodyPosition);
                if (tile instanceof KtMachineTile) {
                    return ((KtMachineTile)tile).getMainBody().cast();
                }
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    protected abstract boolean isKtValidSide(Direction side);

    public BlockPos getMainBodyPosition() {
        return mainBodyPosition;
    }

    public void setMainBodyPosition(BlockPos mainBodyPosition) {
        this.mainBodyPosition = mainBodyPosition;
    }

    public void insertAxle(FullAxleBlockItem item) {
        this.axleBlockState = item.getBlock().defaultBlockState().setValue(BlockStateProperties.AXIS, this.getAxis());
        this.mainBodyPosition = this.worldPosition;
        this.setDirty(true);
    }

    public void removeAxle() {
        this.axleBlockState = Blocks.AIR.defaultBlockState();
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

    public void initKtStatue() {
        this.setMainBodyPosition(this.getBlockPos());
        this.mainBody.ifPresent(body -> {
            body.setLength(1);
            body.setOmega(0);
            body.setAngle(0);
        });
        this.ktStatue.initStatue(this.worldPosition);
        this.setDirty(true);
    }

    public void addInertia(long i) {
        this.getMainKtTile().ktStatue.sumInertia += i;
    }

    public KtEvent.KtCreateEvent createKtCreateEvent() {
        return new KtEvent.KtCreateEvent(this);
    }

    @Nonnull
    public KtMachineTile getMainKtTile() {
        assert level != null;
        if (this.mainBodyPosition.equals(this.getBlockPos())) return this;
        TileEntity te = level.getBlockEntity(this.mainBodyPosition);
        return te instanceof KtMachineTile ? (KtMachineTile) te : this;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockState getAxleBlockState() {
        return this.axleBlockState.getBlock() instanceof FullAxleBlock ? this.axleBlockState.setValue(BlockStateProperties.AXIS, this.getAxis()) : this.axleBlockState;
    }


    public static class KtStatue implements ISaveLoad {

        public static final String INIT_INERTIA = "InitInertia";
        public static final String EXTRA_INERTIA = "SelfInertia";
        public static final String ANGLE = "Angle";
        public static final String SUM_INERTIA = "SumInertia";
        public static final String EQUIVALENT_INERTIA = "EquivalentInertia";
        public static final String REF_KT_POS = "RefKtPos";
        public static final String FREQUENCY = "Frequency";
        public static final String PHASE = "Phase";
        public static final String REVERSED = "Reversed";

        public static final double PHASE_UNIT = Math.PI / 16;
        public static final KtStatue INVALID = new KtStatue(Long.MAX_VALUE);

        public long initInertia;
        public long extraInertia = 0;
        public long sumInertia;
        public long equivalentInertia;
        public long refKtPos = 0;
        public float angle = 0F;
        public int frequency = 0;
        public int phase = 0;
        public boolean reversed = false;

        public KtStatue(long initInertia) {
            this.initInertia = initInertia;
            this.sumInertia = getSelfInertia();
            this.equivalentInertia = this.sumInertia;
        }

        public void initStatue(BlockPos selfPosition) {
            this.sumInertia = getSelfInertia();
            this.angle = 0;
            this.frequency = 0;
            this.phase = 0;
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
            nbt.putInt(PHASE, this.phase);
            nbt.putFloat(ANGLE, this.angle);
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
            this.phase = nbt.getInt(PHASE);
            this.angle = nbt.getFloat(ANGLE);
            this.reversed = nbt.getBoolean(REVERSED);
        }

//        public float getFixedAngle(float refAngle, boolean isRefReversed) {
//            this.angle = (float) (refAngle * Math.pow(2, this.frequency) + phase * PHASE_UNIT);
//            this.angle = (this.reversed ^ isRefReversed) ? -this.angle : this.angle;
//            return this.angle;
//        }

        public float getFixedAngle(KtStatue refStatue) {
            if (this == refStatue) return this.angle;
            this.angle = (float) (refStatue.angle * Math.pow(2, this.frequency) + phase * PHASE_UNIT);
            this.angle = (this.reversed ^ refStatue.reversed) ? -this.angle : this.angle;
            return this.angle;
        }

        @Override
        public String toString() {
            return "KtStatue{" +
                    "\n\tinitInertia=" + initInertia +
                    "\n\textraInertia=" + extraInertia +
                    "\n\tsumInertia=" + sumInertia +
                    "\n\tequivalentInertia=" + equivalentInertia +
                    "\n\trefKtPos=" + BlockPos.of(refKtPos).toShortString() +
                    "\n\tangle=" + angle +
                    "\n\tfrequency=" + frequency +
                    "\n\tphase=" + phase +
                    "\n\treversed=" + reversed +
                    "\n}";
        }
    }
}


