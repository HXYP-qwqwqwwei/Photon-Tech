package photontech.block.kinetic.motor;

import net.minecraft.tileentity.TileEntityType;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.ResistType;

public class ActiveKineticMachine extends FullAxleTile {

    public ActiveKineticMachine(TileEntityType<?> tileEntityTypeIn, long initInertia, ResistType type) {
        super(tileEntityTypeIn, initInertia, true, type);
    }

    protected void setOutput(int force) {
//        this.state.setOutputForce(force);
    }
}
