package photontech.utils.tileentity;

import photontech.utils.data.item.PtIOLimitedItemHandler;
import photontech.utils.data.item.PtItemStackHandler;

import java.util.function.Predicate;

public interface IItemHandlerTile {

    PtItemStackHandler getItemHandler();

    default PtItemStackHandler createItemHandler(int slots) {
        return new PtItemStackHandler(slots);
    }

    default PtIOLimitedItemHandler createIOLimitedHandler(Predicate<Integer> insertOK, Predicate<Integer> extractOK) {
        return new PtIOLimitedItemHandler(this.getItemHandler())
                .setInsertOK(insertOK)
                .setExtractOK(extractOK);
    }

}
