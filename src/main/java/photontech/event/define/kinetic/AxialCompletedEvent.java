package photontech.event.define.kinetic;

import photontech.block.kinetic.KineticMachine;

public class AxialCompletedEvent extends KineticMachineEvent {

    public AxialCompletedEvent(KineticMachine machine) {
        super(machine.getTerminal());
    }

}
