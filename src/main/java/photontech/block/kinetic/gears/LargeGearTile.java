package photontech.block.kinetic.gears;

import net.minecraft.util.Direction;
import photontech.init.PtTileEntities;
import photontech.utils.helper.fuctions.AxisHelper;

public class LargeGearTile extends GearTile {
    public LargeGearTile(long initInertia) {
        super(PtTileEntities.LARGE_GEARS_TILEENTITY.get(), initInertia, 2);
    }

    @Override
    public ConnectCondition[] getConnectConditions() {
        Direction.Axis axis = this.getAxis();
        Direction[] verticalSides = AxisHelper.getVerticalDirections(axis);
        ConnectCondition[] states = new ConnectCondition[4+8];
        int p = 0;
        Direction positiveSide = AxisHelper.getAxisPositiveDirection(axis);
        for (int i = 0; i < 4; ++i) {
            // 共面平行小齿轮
            int j = (i + 1) % 4;
            states[p++] = ConnectCondition.of(this.worldPosition.relative(verticalSides[i]).relative(verticalSides[j]), 1, this.getOffset(), axis);

            boolean isPositive = AxisHelper.isAxisPositiveDirection(verticalSides[i]);
            // 正方向正交大齿轮
            states[p++] = ConnectCondition.of(this.worldPosition.relative(positiveSide).relative(verticalSides[i]), 2, 0, verticalSides[i].getAxis(), !isPositive);
            // 负方向正交大齿轮
            states[p++] = ConnectCondition.of(this.worldPosition.relative(positiveSide.getOpposite()).relative(verticalSides[i]), 2, 0, verticalSides[i].getAxis(), isPositive);
        }
        return states;
    }
}
