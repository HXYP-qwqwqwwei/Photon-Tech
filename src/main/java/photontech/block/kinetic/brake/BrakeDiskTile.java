package photontech.block.kinetic.brake;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import photontech.block.kinetic.ResistType;
import photontech.block.kinetic.motor.ActiveKineticMachine;
import photontech.init.PtTileEntities;
import photontech.utils.helper.fuctions.AxisHelper;

import static net.minecraft.state.properties.BlockStateProperties.FACING;

public class BrakeDiskTile extends ActiveKineticMachine {

    public BrakeDiskTile(long initInertia) {
        super(PtTileEntities.BRAKE_DISC.get(), initInertia, ResistType.AXLE);
    }

    @Override
    public void tick() {
        if (isServerSide() && this.isActive()) {
            assert level != null;
            int resist = 0;
            for (Direction side : AxisHelper.getVerticalDirections(getAxis())) {
                TileEntity te = level.getBlockEntity(worldPosition.relative(side));
                if (te instanceof BrakePadControllerTile && te.getBlockState().getValue(FACING).getOpposite() == side) {
                    resist += ((BrakePadControllerTile) te).getOutputResist();
                }
            }
            this.setOutput(0, resist);
        }
        super.tick();
    }
}
