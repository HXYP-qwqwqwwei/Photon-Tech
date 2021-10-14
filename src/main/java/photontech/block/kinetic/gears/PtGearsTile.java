package photontech.block.kinetic.gears;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import photontech.block.kinetic.axle.AxleTile;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.kinetic.IRigidBody;
import photontech.utils.capability.kinetic.PtRotateBody;
import photontech.utils.helper.AxleHelper;
import photontech.utils.tileentity.PtMachineTile;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

public class PtGearsTile extends PtMachineTile {

    public static final Direction[] DIRECTIONS = { Direction.EAST, Direction.NORTH };

    public PtGearsTile() {
        super(PtTileEntities.GEARS_TILEENTITY.get());
        this.eastRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
        this.northRotateBody = LazyOptional.of(() -> PtRotateBody.create(100));
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            for (Direction direction : DIRECTIONS) {
                this.getRotateBodyCap(direction).ifPresent(from -> {
                    from.updateAngle();

                    TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction));
                    if (tileEntity != null) {
                        LazyOptional<IRigidBody> otherBody = tileEntity.getCapability(PtCapabilities.RIGID_BODY, direction.getOpposite());
                        otherBody.ifPresent(to -> IRigidBody.kineticTransfer(from, to));
                    }
                });
            }

            this.getRotateBodyCap(Direction.EAST).ifPresent(from -> {
                from.updateAngle();
                this.getRotateBodyCap(Direction.NORTH).ifPresent(to -> {
                    to.updateAngle();
                    IRigidBody.kineticTransfer(from, to, (float) (0.125 * Math.PI), true);
//                    IRigidBody.kineticTransfer(from, to);
                });
            });

            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public float getAngle(Direction side) {
        return this.getRotateBodyCap(side).orElse(PtRotateBody.create(0)).getAngle();
    }

}
