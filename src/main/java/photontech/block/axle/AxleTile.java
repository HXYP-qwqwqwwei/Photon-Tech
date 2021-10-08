package photontech.block.axle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtTileEntities;
import photontech.utils.capability.kinetic.IRegidBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;

public class AxleTile extends PtMachineTile {

    protected LazyOptional<IRegidBody> regidBody = LazyOptional.of(() -> PtRotateBody.create(100));
    IRegidBody body = PtRotateBody.create(0);

    public static final float DOUBLE_PI = (float) (2 * Math.PI);
    private float angle = 0F;

    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            this.regidBody.ifPresent(iRegidBody -> {
                iRegidBody.addForceFrom(body, 1);
                iRegidBody.update();
                this.rotate(iRegidBody.getOmega());
                LogManager.getLogger().info(iRegidBody.getOmega());
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
        this.regidBody.ifPresent(iRegidBody -> iRegidBody.load(nbt.getCompound("RotateBody")));
        this.angle = nbt.getFloat("RotateAngle");
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        this.regidBody.ifPresent(iRegidBody -> nbt.put("RotateBody", iRegidBody.save(new CompoundNBT())));
        nbt.putFloat("RotateAngle", this.angle);
        return nbt;
    }

    public float getAngle() {
        return angle;
    }

}
