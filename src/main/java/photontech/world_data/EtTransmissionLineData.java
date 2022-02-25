package photontech.world_data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import photontech.utils.capability.electric.EtTransmissionLine;
import photontech.utils.capability.electric.IEtCapacitor;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class EtTransmissionLineData extends PtComplexCapabilityData<IEtCapacitor> {
    public static final String NAME = "EtSystem";

    public EtTransmissionLineData() {
        super(NAME);
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        CompoundNBT datasNBT = nbt.getCompound(this.getId());
        ListNBT listNBT = (ListNBT) datasNBT.get(DATAS);
        this.nextID = datasNBT.getInt(NEXT_ID);
        assert listNBT != null;
        this.datas = new HashMap<>(INITIAL_SIZE);

        for (INBT inbt : listNBT) {
            CompoundNBT entryNBT = (CompoundNBT) inbt;
            int key = entryNBT.getInt(KEY);
            IEtCapacitor value = EtTransmissionLine.create(1, 1);
            value.load(entryNBT.getCompound(VALUE));
            datas.put(key, value);
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
