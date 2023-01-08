package photontech.utils.helperfunctions;

import photontech.block.crucible.PtCrucibleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class CrucibleConnections {

    private final BlockState crucibleBlock;

    public static CrucibleConnections getInstance(BlockState state) {
        if (state.getBlock() instanceof PtCrucibleBlock) {
            return new CrucibleConnections(state);
        }
        return null;
    }

    private CrucibleConnections(BlockState crucible) {
        this.crucibleBlock = crucible;
    }

    public boolean connectedTo(Direction direction) {
        return !crucibleBlock.getValue(PtCrucibleBlock.PROPERTY_BY_DIRECTION.get(direction));
    }

}
