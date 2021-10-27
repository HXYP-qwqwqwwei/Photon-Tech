package photontech.block.kinetic.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleTile extends PtMachineTile {

    private double selfInertia;
    Direction.Axis currentAxis = Direction.Axis.X;
    LazyOptional<IRotateBody> mainBody = LazyOptional.of(() -> PtVariableRotateBody.of(PtRotateBody.create(100)));

    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
    }


    private void checkAndUpdateAxis() {
        Direction.Axis newAxis = this.getBlockState().getValue(AXIS);
        if (this.currentAxis != newAxis) {
            this.currentAxis = newAxis;
            this.departBody(true);
        }
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {

            this.checkAndUpdateAxis();
            this.combineBody();

            mainBody.ifPresent(body -> {
                body.updateAngle(level.getGameTime());
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
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.currentAxis = Direction.Axis.byName(nbt.getString("CurrentAxis"));
        this.mainBody.ifPresent(body -> body.load(nbt.getCompound("MainBody")));
    }

    public float getAngle(Direction direction) {
        return this.mainBody.orElse(PtRotateBody.create(0)).getAngle();
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.RIGID_BODY) {
            if (side != null && side.getAxis() == this.currentAxis) return this.mainBody.cast();
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    /**
     * depart selfBody from mainBody, called when tile remove/AXIS change/other's combine request.
     * if there are AxleTile on both sides, divide their mainBody into two pieces
     * @param rebuild - if true, create new RotateBody for this tile.
     */
    public void departBody(boolean rebuild) {
        assert this.level != null;
        this.mainBody.ifPresent(body -> {
            float omega = body.getOmega();
            float angle = body.getAngle();
            body.setInertia(body.getInertia() - 100);

            TileEntity tile;
            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
            BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());

            PtRotateBody newBody = PtRotateBody.create(0);
            newBody.setOmega(omega);
            newBody.setAngle(angle);
            int step = 0;
            while ((tile = level.getBlockEntity(pos.move(direction))) instanceof AxleTile) {
                body.setInertia(body.getInertia() - 100);
                tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {
                    ((PtVariableRotateBody) other).set(newBody);
                    newBody.setInertia(newBody.getInertia() + 100);
                });
                if (++step >= 64) break;
            }
            if (rebuild) {
                ((PtVariableRotateBody)body).set(PtRotateBody.create(100));
                body.setOmega(omega);
                body.setAngle(angle);
            }
        });
    }

    /**
     * combine RotateBody with nearby Axle.
     * this method will only check the axis positive direction.
     * if tile A is not combined with this, call A.departBody()
     * then combine with A.
     */
    protected void combineBody() {
        assert this.level != null;
        this.mainBody.ifPresent(body -> {
            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
            TileEntity tile = level.getBlockEntity(this.worldPosition.relative(direction));
            if (tile instanceof AxleTile) {
                tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {

                    // FIXME why output is 0?
                    LogManager.getLogger().info(other.getOmega());
                    LogManager.getLogger().info(other.getInertia());

                    PtVariableRotateBody r1 = (PtVariableRotateBody) body;
                    PtVariableRotateBody r2 = (PtVariableRotateBody) other;
                    if (r1.get() != r2.get()) {

                        IRotateBody.kineticTransfer(r1.get(), r2.get());
                        ((AxleTile) tile).departBody(true);
                        r1.setInertia(r1.getInertia() + r2.getInertia());
                        r2.set(r1.get());
                    }
                });
            }
        });
    }

    @Override
    public void setRemoved() {
        this.departBody(false);
        super.setRemoved();
    }
}
