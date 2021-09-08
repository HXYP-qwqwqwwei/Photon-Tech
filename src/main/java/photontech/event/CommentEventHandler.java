package photontech.event;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import photontech.network.PtNetWorking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.heat.PtHeatReservoir;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommentEventHandler {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PtNetWorking::registerMessage);

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(IHeatReservoir.class, new Capability.IStorage<IHeatReservoir>() {
            @Override
            public INBT writeNBT(Capability<IHeatReservoir> capability, IHeatReservoir instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("Heat", instance.getHeat());
                nbt.putFloat("Capacity", instance.getCapacity());

                return nbt;
            }

            @Override
            public void readNBT(Capability<IHeatReservoir> capability, IHeatReservoir instance, Direction side, INBT nbt) {
                CompoundNBT compoundNBT = (CompoundNBT)nbt;
                instance.setHeat(compoundNBT.getInt("Heat"));
                instance.setCapacity(compoundNBT.getFloat("Capacity"));
            }
        }, () -> new PtHeatReservoir(1000F, 0)));
    }
}
