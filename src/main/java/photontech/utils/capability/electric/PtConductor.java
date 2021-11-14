package photontech.utils.capability.electric;

public class PtConductor implements IPtCapacitor{
    protected double capacity;
    protected double resistor;
    protected long positiveCharge;
    protected long negativeCharge;

    @Override
    public double getHv() {
        return positiveCharge / capacity;
    }

    @Override
    public double getLv() {
        return negativeCharge / capacity;
    }

    @Override
    public double getC() {
        return capacity;
    }

    @Override
    public double getR() {
        return resistor;
    }

    @Override
    public long charge(long q) {
        if (q > 0) {
            this.positiveCharge += q;
        }
        else {
            this.negativeCharge += q;
        }
        return q;
    }

    @Override
    public long discharge(long q) {
        long stored;
        if (q > 0) {
            stored = this.positiveCharge;
            this.setPositiveCharge(stored - q);
            return Math.min(stored, q);
        }
        else {
            stored = this.negativeCharge;
            this.setNegativeCharge(stored - q);
            return Math.max(stored, q);
        }
    }

    public void setPositiveCharge(long q) {
        this.positiveCharge = q > 0 ? q : 0;
    }

    public void setNegativeCharge(long q) {
        this.negativeCharge = negativeCharge < 0 ? q : 0;
    }
}
