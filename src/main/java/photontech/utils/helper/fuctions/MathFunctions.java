package photontech.utils.helper.fuctions;

import net.minecraft.util.math.BlockPos;

public class MathFunctions {
    public static int log2Int(double i) {
        return (int) Math.round(Math.log(i) / Math.log(2));
    }

    public static int pow2Int(int p) {
        return (int) Math.pow(2, p);
    }

    public static int squareInt(int i) {
        return i * i;
    }

    public static int distSqrInt(BlockPos p1, BlockPos p2) {
        return squareInt(p1.getX() - p2.getX())
                + squareInt(p1.getY() - p2.getY())
                + squareInt(p1.getZ() - p2.getZ());
    }
}
