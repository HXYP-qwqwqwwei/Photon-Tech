package photontech.utils.capability.heat;

import photontech.utils.capability.ISaveLoad;

public interface IHeatReservoir extends ISaveLoad {

    float getTemperature();

    int extractHeat(int maxHeat, boolean simulate);

    int acceptHeat(int maxHeat, boolean simulate);

    float getCapacity();

    int getHeat();

    float getHeatTransferRate();

    void setHeat(int heat);

    void setCapacity(float capacity);

    static void heatExchange(IHeatReservoir h1, IHeatReservoir h2, float rate) {
        float dT = h1.getTemperature() - h2.getTemperature();

        if (Math.abs(dT) < 0.005F) {
            return;
        }

        if (rate < 0) {
            rate = Math.min(h1.getHeatTransferRate(), h2.getHeatTransferRate());
        }

        heatTransfer(h1, h2, rate);
        heatTransfer(h2, h1, rate);

    }

    static void heatExchange(IHeatReservoir h1, IHeatReservoir h2) {
        heatExchange(h1, h2, -1F);
    }


    static void heatTransfer(IHeatReservoir from, IHeatReservoir to, float rate) {
        float dT = from.getTemperature() - to.getTemperature();
        if (dT <= 0.005F) {
            return;
        }
        // assert dT > 0
        float heatCanTransfer = (from.getCapacity() * to.getCapacity()) / (from.getCapacity() + to.getCapacity()) * dT;
        int dH = (int) Math.min(dT * rate, heatCanTransfer);
        dH = Math.max(dH, 1);

        int extracted = from.extractHeat(dH, true);
        int accepted = to.acceptHeat(extracted, true);
        dH = Math.min(extracted, accepted);

        from.extractHeat(dH, false);
        to.acceptHeat(dH, false);
    }

}
