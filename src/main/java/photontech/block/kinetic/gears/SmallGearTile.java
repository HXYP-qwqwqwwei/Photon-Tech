package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import photontech.init.PtTileEntities;
import photontech.utils.helper.fuctions.AxisHelper;

public class SmallGearTile extends GearTile {
    public SmallGearTile(long initInertia) {
        super(PtTileEntities.SMALL_GEARS_TILEENTITY.get(), initInertia, 1);
    }

    @Override
    public ConnectCondition[] getConnectConditions() {
        Direction[] sides = AxisHelper.getVerticalDirections(this.getAxis());
        ConnectCondition[] conditions = new ConnectCondition[8];
        int p = 0;
        for (int i = 0; i < 4; ++i) {
            int j = (i + 1) % 4;
            conditions[p++] = ConnectCondition.of(this.worldPosition.relative(sides[i]), 1, this.getOffset(), this.getAxis());
            conditions[p++] = ConnectCondition.of(this.worldPosition.relative(sides[i]).relative(sides[j]), 2, this.getOffset(), this.getAxis());
        }
        return conditions;
    }
}
