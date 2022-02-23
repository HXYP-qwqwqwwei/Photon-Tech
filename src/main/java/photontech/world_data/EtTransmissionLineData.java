package photontech.world_data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import photontech.utils.capability.electric.IEtCapacitor;

import javax.annotation.Nonnull;

public class EtTransmissionLineData extends PtComplexCapabilityData<IEtCapacitor> {
    public static final String NAME = "EtSystem";

    public EtTransmissionLineData() {
        super(NAME);
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get(this.getId());
        assert listNBT != null;
        int size = listNBT.size();
        for (int i = 0; i < size; ++i) {
            CompoundNBT capNBT = (CompoundNBT) listNBT.get(i);
            if (!capNBT.isEmpty()) {
                datas.get(i).load(capNBT);
            }
            else datas.set(i, null);
        }
    }

    public static EtTransmissionLineData get(IWorld worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getDataStorage();
        return storage.computeIfAbsent(EtTransmissionLineData::new, NAME);
    }

}
