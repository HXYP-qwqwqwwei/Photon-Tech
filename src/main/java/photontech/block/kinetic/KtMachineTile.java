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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.kinetic.axle.FullAxleBlock;
import photontech.init.PtCapabilities;
import photontech.item.ktblockitem.FullAxleBlockItem;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper_functions.AxisHelper;
import photontech.utils.helper_functions.MutableFloat;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class KtMachineTile extends PtMachineTile {

    public static final String SELF_INERTIA = "SelfInertia";
    public static final String ANGLE = "Angle";
    public static final String MAIN_BODY_POSITION = "MainBodyPosition";
    public static final String MAIN_BODY = "MainBody";
    public static final String NEED_AXLE = "NeedAxle";
    public static final String AXLE_BLOCK_STATE = "AxleBlockState";

    public long selfInertia;
    protected BlockPos mainBodyPosition = BlockPos.ZERO;
    protected final MutableFloat angle = new MutableFloat(0);
    protected final LazyOptional<IRotateBody> mainBody;
    protected BlockState axleBlockState = Blocks.AIR.defaultBlockState();
    protected boolean needAxle;

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        this(tileEntityTypeIn, initInertia, false);
    }

    public KtMachineTile(TileEntityType<?> tileEntityTypeIn, long initInertia, boolean needAxle) {
        super(tileEntityTypeIn);
        this.selfInertia = initInertia;
        mainBody = LazyOptional.of(() -> PtRotateBody.create(initInertia));
        this.needAxle = needAxle;
        this.setColdDown(5);
    }

    public Direction.Axis getAxis() {
        return this.getBlockState().getValue(BlockStateProperties.AXIS);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            if (this.worldPosition.equals(mainBodyPosition)) {
                mainBody.ifPresent(body -> {
                    body.updateAngle();
                    this.angle.value = body.getAngle();
                    this.setDirty(true);
                });
            }
            this.getMainBody().ifPresent(body -> IRotateBody.kineticTransferWithEnv(body, 0.1));
            this.updateIfDirty();
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putLong(SELF_INERTIA, this.selfInertia);
        nbt.putFloat(ANGLE, this.angle.value);
        nbt.putLong(MAIN_BODY_POSITION, this.mainBodyPosition.asLong());
        nbt.putInt(AXLE_BLOCK_STATE, Block.getId(this.axleBlockState));
        nbt.putBoolean(NEED_AXLE, this.needAxle);
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            this.saveCap(mainBody, MAIN_BODY, nbt);
        }
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.selfInertia = nbt.getLong(SELF_INERTIA);
        this.angle.value = nbt.getFloat(ANGLE);
        this.mainBodyPosition = BlockPos.of(nbt.getLong(MAIN_BODY_POSITION));
        this.needAxle = nbt.getBoolean(NEED_AXLE);
        this.axleBlockState = Block.stateById(nbt.getInt(AXLE_BLOCK_STATE));
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            this.loadCap(mainBody, MAIN_BODY, nbt);
        }
    }

    public float getAngle() {
        assert this.level != null;
        TileEntity tile = this.level.getBlockEntity(this.mainBodyPosition);
        if (!(tile instanceof KtMachineTile)) return 0;
        return ((KtMachineTile) tile).angle.value;
    }

    public LazyOptional<IRotateBody> getMainBody() {
        return this.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(this.getAxis()));
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

    public BlockState getAxleBlockState() {
        return this.axleBlockState.getBlock() instanceof FullAxleBlock ? this.axleBlockState.setValue(BlockStateProperties.AXIS, this.getAxis()) : this.axleBlockState;
    }
}
