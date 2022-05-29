package photontech.utils.capability.kinetic;

public class ForceMoment implements IForce {
    public float force;
    public final float length;

    private ForceMoment(float f, float len) {
        this.force = f;
        this.length = len;
    }

    static ForceMoment create(float f, float len) {
        return new ForceMoment(f, len);
    }


}


