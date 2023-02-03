package photontech.data;

import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import photontech.utils.data.electric.DCWireCapacitor;
import photontech.utils.data.electric.ICapacitor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class DCWireDataManager extends PtDataManagerWithRefCnt<ICapacitor> {
    public static final String NAME = "etsystem";

    public DCWireDataManager() {
        super(NAME);
    }

    @Nonnull
    @Override
    public Supplier<ICapacitor> getSupplier() {
        return () -> DCWireCapacitor.create(1);
    }


    public static DCWireDataManager getData(IWorld worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(DCWireDataManager::new, NAME);
    }

}
