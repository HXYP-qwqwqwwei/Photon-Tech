package photontech.item;

import net.minecraft.util.ActionResultType;

public interface IInteractiveItem<T> {
    boolean fits(Object target);

    default void broadcast() {}

    default void withdraw() {}

    ActionResultType tryInteract(T target);
}
