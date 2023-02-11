package photontech.block.hydraulic;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import photontech.init.PtTileEntities;

import static net.minecraft.block.HorizontalFaceBlock.FACE;
import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class HydraulicLeverTile extends HydraulicPumpMachine {

    public HydraulicLeverTile() {
        super(PtTileEntities.HYDRAULIC_LEVER.get());
    }

    @Override
    public void tick() {
        if (isServerSide()) {
            assert level != null;
            if (isPowered()) {
                this.setOutput(1280);
            } else this.setOutput(0);
        }
    }


    public Direction getOutputSide() {
        BlockState state = getBlockState();
        switch (state.getValue(FACE)) {
            case FLOOR: return Direction.DOWN;
            case CEILING: return Direction.UP;
            default: return state.getValue(HORIZONTAL_FACING).getOpposite();
        }
    }

    public boolean isPowered() {
        return getBlockState().getValue(POWERED);
    }
}
