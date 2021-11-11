package photontech.utils.capability.kinetic;

public class PtWorkingBody extends PtRotateBody {
    public enum WorkingMode {
        RESISTANT,
        KINETIC_OUTPUT,
        KINETIC_RECIPE
    }
    
    private int convertedKineticEnergy = 0;
    private final WorkingMode mode;
    
    protected PtWorkingBody(long inertia, WorkingMode mode) {
        super(inertia);
        this.mode = mode;
    }

    @Override
    public void setOmega(float omega) {
//        switch (this.mode) {
//            case RESISTANT:
//                break;
//            case KINETIC_OUTPUT:
//            case KINETIC_RECIPE:
//        }
//        this.convertedKineticEnergy += this.inertia * omega * omega;
    }

    public int extractAllEnergy(double efficiency) {
        int ret = convertedKineticEnergy;
        this.convertedKineticEnergy = 0;
        return ret;
    }

}
