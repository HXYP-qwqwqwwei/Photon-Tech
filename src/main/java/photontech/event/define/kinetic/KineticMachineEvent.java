package photontech.event.define.kinetic;

import net.minecraftforge.event.world.BlockEvent;
import photontech.block.kinetic.KineticMachine;

public class KineticMachineEvent extends BlockEvent {

    protected final KineticMachine machine;

    public KineticMachineEvent(KineticMachine machine) {
        super(machine.getLevel(), machine.getBlockPos(), machine.getBlockState());
        this.machine = machine;
    }

    public KineticMachine getMachine() {
        return machine;
    }


}
