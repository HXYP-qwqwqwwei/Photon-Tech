package photontech.utils.helper.fuctions;

public class PtMath {
    public static int log2Int(double i) {
        return (int) Math.round(Math.log(i) / Math.log(2));
    }

    public static int pow2Int(int p) {
        return (int) Math.pow(2, p);
    }
}
