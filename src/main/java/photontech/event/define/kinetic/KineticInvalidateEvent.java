package photontech.event.define.kinetic;

import photontech.block.kinetic.KineticMachine;

public class KineticInvalidateEvent extends KineticMachineEvent {
    public KineticInvalidateEvent(KineticMachine machine) {
        super(machine);
    }
}
