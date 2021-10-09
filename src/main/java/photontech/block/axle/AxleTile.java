package photontech.block.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.kinetic.IRigidBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper.AxleHelper;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AxleTile extends PtMachineTile {

    protected LazyOptional<IRigidBody> rigidBody = LazyOptional.of(() -> PtRotateBody.create(100));

    public static final float DOUBLE_PI = (float) (2 * Math.PI);
    private float angle = 0F;

    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            for (Direction direction : Direction.values()) {
                if (AxleHelper.connectedTo(this.level, this.getBlockState(), this.worldPosition, direction)) {
                    AxleTile otherAxle = (AxleTile) level.getBlockEntity(this.worldPosition.relative(direction));
                    if (otherAxle != null) {
                        LazyOptional<IRigidBody> otherBody = otherAxle.getCapability(PtCapabilities.RIGID_BODY);
                        otherBody.ifPresent(from -> this.rigidBody.ifPresent(to -> IRigidBody.kineticTransfer(to, from)));
                    }
                }
            }
            this.rigidBody.ifPresent(iRigidBody -> {
                this.rotate(iRigidBody.getOmega());
            });
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    private void rotate(float omega) {
        this.angle += omega;
        if (this.angle > DOUBLE_PI) {
            this.angle -= ((int) (angle / DOUBLE_PI)) * DOUBLE_PI;
        }
        if (this.angle < -DOUBLE_PI) {
            this.angle += ((int) (angle / DOUBLE_PI)) *  DOUBLE_PI;
        }
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.rigidBody.ifPresent(iRigidBody -> iRigidBody.load(nbt.getCompound("RotateBody")));
        this.angle = nbt.getFloat("RotateAngle");
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.rigidBody.ifPresent(iRigidBody -> nbt.put("RotateBody", iRigidBody.save(new CompoundNBT())));
        nbt.putFloat("RotateAngle", this.angle);
        return nbt;
    }

    public float getAngle() {
        return angle;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PtCapabilities.RIGID_BODY) {
            return this.rigidBody.cast();
        }
        return super.getCapability(cap, side);
    }
}
