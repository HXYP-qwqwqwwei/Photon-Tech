package photontech.block.kinetic.motor.infinity;

import photontech.block.kinetic.ResistType;
import photontech.block.kinetic.motor.ActiveKineticMachine;
import photontech.init.PtTileEntities;

public class InfinityMotorTile extends ActiveKineticMachine {
    public InfinityMotorTile(long initInertia) {
        super(PtTileEntities.INFINITY_MOTOR.get(), initInertia, ResistType.NO_RESIST);
    }

    @Override
    public void tick() {
        if (isServerSide() && this.isActive()) {
            this.setOutput(10);
        }
        super.tick();
    }
}
