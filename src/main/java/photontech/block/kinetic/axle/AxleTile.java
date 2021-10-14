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
import photontech.utils.capability.kinetic.IRigidBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper.AxleHelper;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleTile extends PtMachineTile {


    public AxleTile() {
        super(PtTileEntities.AXLE_TILE.get());
        this.eastRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
        this.upRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
        this.southRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
    }


    public void updateAxis(Direction.Axis axis) {

    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            Direction.Axis axis = this.getBlockState().getValue(AXIS);
            Direction direction = AxleHelper.getAxisPositiveDirection(axis);

            this.getRotateBodyCap(direction).ifPresent(from -> {
                from.updateAngle();
//                LogManager.getLogger().info(from.getOmega());

                TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction));
                if (tileEntity != null) {
                    LazyOptional<IRigidBody> otherBody = tileEntity.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite());
                    otherBody.ifPresent(to -> IRigidBody.kineticTransfer(from, to));
                }
                tileEntity = level.getBlockEntity(this.worldPosition.relative(direction.getOpposite()));
                if (tileEntity != null) {
                    LazyOptional<IRigidBody> otherBody = tileEntity.getCapability(PtCapabilities.RIGID_BODY, direction);
                    otherBody.ifPresent(to -> IRigidBody.kineticTransfer(from, to));
                }
            });
            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }


    public float getAngle(Direction direction) {
        return this.getRotateBodyCap(direction).orElse(PtRotateBody.create(0)).getAngle();
    }


    @Override
    protected LazyOptional<PtRotateBody> getRotateBodyCap(@Nullable Direction side) {
        if (side != null && side.getAxis() == this.getBlockState().getValue(AXIS)) {
            return super.getRotateBodyCap(AxleHelper.getAxisPositiveDirection(side.getAxis()));
        }
        return LazyOptional.empty();
    }

}
