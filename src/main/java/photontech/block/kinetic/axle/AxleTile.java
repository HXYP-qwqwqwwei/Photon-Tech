package photontech.block.kinetic.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper.AxisHelper;
import photontech.utils.helper.MutableFloat;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AxleTile extends PtMachineTile {

    public long selfInertia;
    protected BlockPos mainBodyPosition = this.worldPosition;
    protected final MutableFloat angle = new MutableFloat(0);
    protected final LazyOptional<IRotateBody> mainBody;

    public AxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn);
        this.selfInertia = initInertia;
        mainBody = LazyOptional.of(() -> PtRotateBody.create(initInertia));
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
        nbt.putLong("SelfInertia", this.selfInertia);
        nbt.putFloat("Angle", this.angle.value);
        nbt.putLong("MainBodyPosition", this.mainBodyPosition.asLong());
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            this.saveCap(mainBody, "MainBody", nbt);
        }
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.selfInertia = nbt.getLong("SelfInertia");
        this.angle.value = nbt.getFloat("Angle");
        this.mainBodyPosition = BlockPos.of(nbt.getLong("MainBodyPosition"));
        if (this.mainBodyPosition.equals(this.worldPosition)) {
            this.loadCap(mainBody, "MainBody", nbt);
        }
    }

    public float getAngle() {
        assert this.level != null;
        TileEntity tile = this.level.getBlockEntity(this.mainBodyPosition);
        if (!(tile instanceof AxleTile)) return 0;
        return ((AxleTile) tile).angle.value;
    }

    protected LazyOptional<IRotateBody> getMainBody() {
        return this.getCapability(PtCapabilities.RIGID_BODY, AxisHelper.getAxisPositiveDirection(this.getAxis()));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        assert level != null;
        if (cap == PtCapabilities.RIGID_BODY) {
            if (side != null && side.getAxis() == this.getBlockState().getValue(BlockStateProperties.AXIS)) {
                if (this.mainBodyPosition.equals(this.worldPosition)) return this.mainBody.cast();
                TileEntity tile = level.getBlockEntity(this.mainBodyPosition);
                if (!(tile instanceof AxleTile)) throw new RuntimeException("There is no axle at target position");
                return tile.getCapability(cap, side);
            }
            else return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    public BlockPos getMainBodyPosition() {
        return mainBodyPosition;
    }

    public void setMainBodyPosition(BlockPos mainBodyPosition) {
        this.mainBodyPosition = mainBodyPosition;
    }
}
