package photontech.event.pt;

import net.minecraft.util.Direction;
import net.minecraftforge.event.world.BlockEvent;
import photontech.block.kinetic.KineticMachine;
import photontech.block.kinetic.gears.GearTile;

public class KtEvent extends BlockEvent {

    protected final KineticMachine machine;

    public KtEvent(KineticMachine machine) {
        super(machine.getLevel(), machine.getBlockPos(), machine.getBlockState());
        this.machine = machine;
    }

    public KineticMachine getMachine() {
        return machine;
    }

    public static class KtInvalidateEvent extends KtEvent {
        public KtInvalidateEvent(KineticMachine selfKt) {
            super(selfKt);
        }
    }

    public static class KtActiveEvent extends KtEvent {

        protected final Direction updateDirection;

        public KtActiveEvent(KineticMachine selfKt, Direction updateDirection) {
            super(selfKt);
            this.updateDirection = updateDirection;
        }

        public Direction getUpdateDirection() {
            return updateDirection;
        }
    }

    /**
     * Kt机器创建事件，当动能机器创建时（放置，激活）触发。
     * 用于处理合并后的cap合并问题。
     */
    public static class KtCreateEvent extends KtEvent {
        public KtCreateEvent(KineticMachine selfKt) {
            super(selfKt);
        }

    }

    public static class KtAxialCombinedEvent extends KtEvent {
        public KtAxialCombinedEvent(KineticMachine selfKt) {
            super(selfKt.getTerminal());
        }
    }

//    public static class KtGearCreateEvent extends KtCreateEvent {
//
//        public KtGearCreateEvent(KtGearTile gearsKt) {
//            super(gearsKt);
//        }
//
//        public KtGearTile getGearKt() {
//            return (KtGearTile) selfKt;
//        }
//    }

    public static class KtGearSynchronizeEvent extends KtEvent {

        public KtGearSynchronizeEvent(GearTile selfKt) {
            super(selfKt);
        }

        public GearTile getGearKt() {
            return (GearTile) machine;
        }

    }

    public static class KtGearSynchronizeNotifyEvent extends KtEvent {

        public KtGearSynchronizeNotifyEvent(KineticMachine selfKt) {
            super(selfKt.getTerminal());
        }

    }


}
