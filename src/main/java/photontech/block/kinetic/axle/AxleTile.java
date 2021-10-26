package photontech.block.kinetic.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper.AxisHelper;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleTile extends PtMachineTile {

    Direction.Axis currentAxis = Direction.Axis.X;

    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
        this.eastRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
        this.upRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
        this.southRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
    }


    public void checkAndUpdateAxis() {
        Direction.Axis newAxis = this.getBlockState().getValue(AXIS);
        if (this.currentAxis == newAxis) {
            return;
        }
        Direction newDirection = AxisHelper.getAxisPositiveDirection(newAxis);
        Direction oldDirection = AxisHelper.getAxisPositiveDirection(this.currentAxis);
        // kinetic energy : old -> new
        this.getRotateBodyCap(oldDirection).ifPresent(oldBody -> {
            // update axis in case getRotateBodyCap() will get right cap
            this.currentAxis = newAxis;
            this.getRotateBodyCap(newDirection).ifPresent(newBody -> {
                newBody.setInertia(oldBody.getInertia());
                oldBody.setOmega(0);
                oldBody.setAngle(0);
                newBody.setAngle(0);
            });
        });
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {

            this.checkAndUpdateAxis();

            for (Direction direction : AxisHelper.getAxisDirections(this.currentAxis)) {
                this.getRotateBodyCap(direction).ifPresent(from -> {
                    from.updateAngle();

                    TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction));
                    if (tileEntity != null) {
                        LazyOptional<IRotateBody> otherBody = tileEntity.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite());
                        otherBody.ifPresent(to -> {
                            IRotateBody.kineticTransfer(from, to);
                        });
                    }
                });
            }

            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        nbt.putString("CurrentAxis", this.currentAxis.getName());
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.currentAxis = Direction.Axis.byName(nbt.getString("CurrentAxis"));
    }

    public float getAngle(Direction direction) {
        return this.getRotateBodyCap(direction).orElse(PtRotateBody.create(0)).getAngle();
    }


    @Override
    protected LazyOptional<PtRotateBody> getRotateBodyCap(@Nullable Direction side) {
        if (side != null && side.getAxis() == this.currentAxis) {
            return super.getRotateBodyCap(AxisHelper.getAxisPositiveDirection(this.currentAxis));
        }
        return LazyOptional.empty();
    }

}
