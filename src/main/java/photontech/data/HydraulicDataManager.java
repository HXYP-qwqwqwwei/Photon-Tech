package photontech.data;

import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import photontech.utils.data.HydraulicPipe;
import photontech.utils.data.Hydraulic;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class HydraulicDataManager extends PtDataManagerWithRefCnt<Hydraulic> {
    public static final String NAME = "hydraulicsystem";

    HydraulicDataManager() {
        super(NAME);
    }

    @Nonnull
    @Override
    public Supplier<Hydraulic> getSupplier() {
        return HydraulicPipe::create;
    }

    public static HydraulicDataManager getData(IWorld worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(HydraulicDataManager::new, NAME);
    }

}
