package photontech.utils.capability.kinetic;

public interface IRotate {

    float getOmega();

    int getKinetic();

    float getInertia();

    default void transmission(IRotate from, IRotate to, float ration) {
        float k1 = from.getKinetic();
        float k2 = to.getKinetic();


    }

}
