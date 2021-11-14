package photontech.utils.capability.electric;

public interface IPtCapacitor {
    long INF = Long.MAX_VALUE;

    /**
     * 获取高电势。
     * @return 高电势值
     */
    double getHv();

    /**
     * 获取低电势。
     * @return 低电势值
     */
    double getLv();

    /**
     * 获取电容。
     * @return 电容值
     */
    double getC();

    /**
     * 获取电阻。
     * @return 电阻值
     */
    double getR();

    /**
     * 充电。
     * @return 实际输入的电荷量
     */
    long charge(long q);

    /**
     * 放电。
     * @return 实际放出的电荷量
     */
    long discharge(long q);


}
