package photontech.utils;

import photontech.utils.tileentity.ChainedUpdatingMachine;

public interface IMixinWorld {
    void updateChainedMachine(ChainedUpdatingMachine machine);
}
