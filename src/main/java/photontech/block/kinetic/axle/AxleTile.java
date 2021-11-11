package photontech.block.kinetic.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.capability.kinetic.PtVariableRotateBody;
import photontech.utils.helper.AxisHelper;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleTile extends PtMachineTile {

    public long selfInertia;
    Direction.Axis currentAxis = Direction.Axis.X;
    LazyOptional<IRotateBody> mainBody;

    public AxleTile(TileEntityType<?> tileEntityTypeIn, long initInertia) {
        super(tileEntityTypeIn);
        this.selfInertia = initInertia;
        mainBody = LazyOptional.of(() -> PtVariableRotateBody.of(PtRotateBody.create(initInertia)));
    }


    private void checkAndUpdateAxis() {
        Direction.Axis newAxis = this.getBlockState().getValue(AXIS);
        if (this.currentAxis != newAxis) {
            this.departBody();
            this.currentAxis = newAxis;
        }
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {

            long time = level.getGameTime();
            this.checkAndUpdateAxis();

            if (time % 5 == 0) {
                this.combineBody();
            }

            mainBody.ifPresent(body -> {
                body.updateAngle(time);
            });

            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("CurrentAxis", this.currentAxis.getName());
        this.mainBody.ifPresent(body -> nbt.put("MainBody", body.save(new CompoundNBT())));
        nbt.putLong("SelfInertia", this.selfInertia);
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.currentAxis = Direction.Axis.byName(nbt.getString("CurrentAxis"));
        this.mainBody.ifPresent(body -> body.load(nbt.getCompound("MainBody")));
        this.selfInertia = nbt.getLong("SelfInertia");
    }

    public float getAngle(Direction direction) {
        return this.mainBody.orElse(PtRotateBody.create(0)).getAngle();
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.RIGID_BODY) {
            if (side != null && side.getAxis() == this.getBlockState().getValue(AXIS)) return this.mainBody.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    /**
     * depart selfBody from mainBody, called when tile remove or AXIS change.
     * this method will only check the axis positive direction.
     */
    protected void departBody() {
        assert this.level != null;
        this.mainBody.ifPresent(body -> {
            float omega = body.getOmega();
            float angle = body.getAngle();
            ((PtVariableRotateBody)body).set(PtVariableRotateBody.of(PtRotateBody.create(this.selfInertia)));
            body.setOmega(omega);
            body.setAngle(angle);

            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
            BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());

            TileEntity tile;
            if ((tile = level.getBlockEntity(pos.move(direction))) instanceof AxleTile) {
                AxleTile axle = (AxleTile) tile;
                tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {
                    float otherOmega = other.getOmega();
                    ((PtVariableRotateBody) other).set(PtRotateBody.create(axle.selfInertia));
                    other.setOmega(otherOmega);
                    axle.combineBody();
                });
            }
        });
    }

    /**
     * combine RotateBody with nearby Axle.
     * this method will only check the axis positive direction.
     */
    protected void combineBody() {
        assert this.level != null;
        this.mainBody.ifPresent(body -> {

            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);

            byte step = 0;
            if (!(level.getBlockEntity(this.worldPosition.relative(direction.getOpposite())) instanceof AxleTile)) {
                body.setInertia(this.selfInertia);
                BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
                TileEntity tile;
                while ((tile = level.getBlockEntity(pos.move(direction))) instanceof AxleTile) {
                    PtVariableRotateBody r1 = (PtVariableRotateBody) body;
                    PtVariableRotateBody r2 = (PtVariableRotateBody) tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).orElse(PtVariableRotateBody.of(PtRotateBody.create(0)));
                    if (r2.getInertia() == 0) break;

                    IRotateBody.kineticTransfer(r1.get(), r2.get());
                    r1.setInertia(r1.getInertia() + ((AxleTile) tile).selfInertia);
                    r2.set(r1.get());
                    if (++step >= 16) break;
                }
            }
        });
    }

    @Override
    public void setRemoved() {
        this.departBody();
        super.setRemoved();
    }
}
