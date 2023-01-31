package photontech.event.define.kinetic;

import photontech.block.kinetic.gears.GearTile;

public class GearSynchronizeEvent extends KineticMachineEvent {

    public GearSynchronizeEvent(GearTile gear) {
        super(gear);
    }

    public GearTile getGear() {
        return (GearTile) machine;
    }

}
