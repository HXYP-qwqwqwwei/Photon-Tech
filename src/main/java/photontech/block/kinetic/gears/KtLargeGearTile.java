package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import photontech.init.PtTileEntities;
import photontech.utils.helperfunctions.AxisHelper;

public class KtLargeGearTile extends KtGearTile {
    public KtLargeGearTile(long initInertia) {
        super(PtTileEntities.LARGE_GEARS_TILEENTITY.get(), initInertia, 2);
    }

    @Override
    public BlockPos[] getSearchPositions() {
        Direction[] sides = AxisHelper.getVerticalDirections(this.getAxis());
        BlockPos[] searchPoses = new BlockPos[4];
        int p = 0;
        for (int i = 0; i < 4; ++i) {
            int j = (i + 1) % 4;
            searchPoses[p++] = this.worldPosition.relative(sides[i]).relative(sides[j]);
        }
        return searchPoses;
    }
}
