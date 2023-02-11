package photontech.event.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import photontech.block.kinetic.KineticState;
import photontech.network.PtNetWorking;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import photontech.utils.data.electric.ElectricCapacitor;
import photontech.utils.data.electric.DCWireCapacitor;
import photontech.utils.data.heat.HeatReservoir;
import photontech.utils.data.heat.PtHeatReservoir;
import photontech.utils.data.magnet.Magnet;
import photontech.utils.data.magnet.MagnetPole;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PtNetWorking::registerMessage);

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(HeatReservoir.class, new Capability.IStorage<HeatReservoir>() {
            @Override
            public INBT writeNBT(Capability<HeatReservoir> capability, HeatReservoir instance, Direction side) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("Heat", instance.getHeat());
                nbt.putFloat("Capacity", instance.getCapacity());
                return nbt;
            }

            @Override
            public void readNBT(Capability<HeatReservoir> capability, HeatReservoir instance, Direction side, INBT nbt) {
                CompoundNBT compoundNBT = (CompoundNBT)nbt;
                instance.setHeat(compoundNBT.getInt("Heat"));
                instance.setCapacity(compoundNBT.getFloat("Capacity"));
            }
        }, () -> new PtHeatReservoir(1000F, 0)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(KineticState.class, new Capability.IStorage<KineticState>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<KineticState> capability, KineticState instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<KineticState> capability, KineticState instance, Direction side, INBT nbt) {}
        }, () -> new KineticState(Long.MAX_VALUE)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(ElectricCapacitor.class, new Capability.IStorage<ElectricCapacitor>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ElectricCapacitor> capability, ElectricCapacitor instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ElectricCapacitor> capability, ElectricCapacitor instance, Direction side, INBT nbt) {}
        }, () -> DCWireCapacitor.create(Long.MAX_VALUE)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(Magnet.class, new Capability.IStorage<Magnet>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<Magnet> capability, Magnet instance, Direction side) {return null;}

            @Override
            public void readNBT(Capability<Magnet> capability, Magnet instance, Direction side, INBT nbt) {}
        }, () -> MagnetPole.create(Magnet.MagneticPoleType.N, 0)));

    }
}
