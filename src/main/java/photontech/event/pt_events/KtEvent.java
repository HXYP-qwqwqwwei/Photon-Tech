package photontech.event.pt_events;

import net.minecraft.util.Direction;
import net.minecraftforge.event.world.BlockEvent;
import photontech.block.kinetic.KtMachineTile;
import photontech.block.kinetic.gears.KtGearTile;

public class KtEvent extends BlockEvent {

    protected final KtMachineTile selfKt;

    public KtEvent(KtMachineTile selfKt) {
        super(selfKt.getLevel(), selfKt.getBlockPos(), selfKt.getBlockState());
        this.selfKt = selfKt;
    }

    public KtMachineTile getSelfKt() {
        return selfKt;
    }

    public static class KtInvalidateEvent extends KtEvent {
        public KtInvalidateEvent(KtMachineTile selfKt) {
            super(selfKt);
        }
    }

    public static class KtActiveEvent extends KtEvent {

        protected final Direction updateDirection;

        public KtActiveEvent(KtMachineTile selfKt, Direction updateDirection) {
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
        public KtCreateEvent(KtMachineTile selfKt) {
            super(selfKt);
        }

    }

    public static class KtAxialCombinedEvent extends KtEvent {
        public KtAxialCombinedEvent(KtMachineTile selfKt) {
            super(selfKt.getMainKtTile());
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

        public KtGearSynchronizeEvent(KtGearTile selfKt) {
            super(selfKt);
        }

        public KtGearTile getGearKt() {
            return (KtGearTile) selfKt;
        }

    }

    public static class KtGearSynchronizeNotifyEvent extends KtEvent {

        public KtGearSynchronizeNotifyEvent(KtMachineTile selfKt) {
            super(selfKt.getMainKtTile());
        }

    }


}
