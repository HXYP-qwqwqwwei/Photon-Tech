package photontech.event.define.kinetic;

import net.minecraft.util.Direction;
import photontech.block.kinetic.KineticMachine;

public class KineticActivateEvent extends KineticMachineEvent {

    protected final Direction updateDirection;

    public KineticActivateEvent(KineticMachine machine, Direction updateDirection) {
        super(machine);
        this.updateDirection = updateDirection;
    }

    public Direction getUpdateDirection() {
        return updateDirection;
    }
}
