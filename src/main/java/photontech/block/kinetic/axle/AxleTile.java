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

    protected static class AxleUpdateFlag {
        Direction fromDirection;
        boolean shouldUpdate = false;
    }

    Direction.Axis currentAxis = Direction.Axis.X;
    LazyOptional<IRotateBody> mainBody = LazyOptional.of(() -> PtVariableRotateBody.of(PtRotateBody.create(100)));
    public boolean shouldUpdateBody = true;

    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
    }


    private void checkAndUpdateAxis() {
        Direction.Axis newAxis = this.getBlockState().getValue(AXIS);
        if (this.currentAxis != newAxis) {
            this.currentAxis = newAxis;
            mainBody.ifPresent(body -> {
                body.setAngle(0);
                body.setOmega(0);
            });
        }
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {

            this.checkAndUpdateAxis();

            mainBody.ifPresent(body -> {
                body.updateAngle(level.getGameTime());
                for (Direction direction : AxisHelper.getAxisDirections(this.currentAxis)) {
                    TileEntity tile = level.getBlockEntity(this.worldPosition.relative(direction));
                    if (tile != null && tile.getBlockState().getBlock() instanceof AxleBlock) {
                        Direction.Axis axis = tile.getBlockState().getValue(AXIS);
                        if (this.currentAxis == axis) {

                            tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {
                                PtVariableRotateBody r1 = (PtVariableRotateBody) body;
                                PtVariableRotateBody r2 = (PtVariableRotateBody) other;
                                if (r1.get() != r2.get()) {
                                    IRotateBody.kineticTransfer(r1, r2);
                                    r1.setInertia(r1.getInertia() + r2.getInertia());
                                    r2.set(r1.get());
                                }
                            });

                        }
                    }
                }
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
            for (Direction validDirection : AxisHelper.getAxisDirections(this.currentAxis)) {
                if (side == validDirection) return this.mainBody.cast();
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    protected void departBody() {
        assert level != null;
        this.mainBody.ifPresent(body -> {
            body.setInertia(body.getInertia() - 100);

            assert this.level != null;
            TileEntity tile;
            Direction direction = AxisHelper.getAxisPositiveDirection(this.currentAxis);
            BlockPos.Mutable pos = new BlockPos.Mutable(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());

            PtRotateBody anotherBody = PtRotateBody.create(0);
            anotherBody.setOmega(body.getOmega());
            while ((tile = level.getBlockEntity(pos.move(direction))) instanceof AxleTile) {
                body.setInertia(body.getInertia() - 100);
                tile.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite()).ifPresent(other -> {
                    ((PtVariableRotateBody) other).set(anotherBody);
                    anotherBody.setInertia(anotherBody.getInertia() + 100);
                });
            }
        });
    }

    @Override
    public void setRemoved() {
        this.departBody();
        super.setRemoved();
    }
}
