package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import photontech.init.PtTileEntities;
import photontech.utils.helperfunctions.AxisHelper;

public class KtSmallGearTile extends KtGearTile {
    public KtSmallGearTile(long initInertia) {
        super(PtTileEntities.SMALL_GEARS_TILEENTITY.get(), initInertia, 1);
    }

    @Override
    public BlockPos[] getSearchPositions() {
        Direction[] sides = AxisHelper.getVerticalDirections(this.getAxis());
        BlockPos[] searchPoses = new BlockPos[8];
        int p = 0;
        for (int i = 0; i < 4; ++i) {
            int j = (i + 1) % 4;
            searchPoses[p++] = this.worldPosition.relative(sides[i]).relative(sides[j]);
        }
        for (int i = 0; i < 4; ++i) {
            searchPoses[p++] = this.worldPosition.relative(sides[i]);
        }
        return searchPoses;
    }
}
