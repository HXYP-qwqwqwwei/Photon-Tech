package photontech.event.define.kinetic;

import photontech.block.kinetic.KineticMachine;

public class AxialCombinedEvent extends KineticMachineEvent {
    public AxialCombinedEvent(KineticMachine machine) {
        super(machine.getTerminal());
    }
}
