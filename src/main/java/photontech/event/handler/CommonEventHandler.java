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
import photontech.utils.data.electric.DCWireCapacitor;
import photontech.utils.data.electric.ICapacitor;
import photontech.utils.data.heat.IHeatReservoir;
import photontech.utils.data.heat.PtHeatReservoir;
import photontech.utils.data.magnet.IMagnet;
import photontech.utils.data.magnet.PtMagnet;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {
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

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(KineticState.class, new Capability.IStorage<KineticState>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<KineticState> capability, KineticState instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<KineticState> capability, KineticState instance, Direction side, INBT nbt) {}
        }, () -> new KineticState(Long.MAX_VALUE)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(ICapacitor.class, new Capability.IStorage<ICapacitor>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ICapacitor> capability, ICapacitor instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<ICapacitor> capability, ICapacitor instance, Direction side, INBT nbt) {}
        }, () -> DCWireCapacitor.create(Long.MAX_VALUE)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(IMagnet.class, new Capability.IStorage<IMagnet>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IMagnet> capability, IMagnet instance, Direction side) {return null;}

            @Override
            public void readNBT(Capability<IMagnet> capability, IMagnet instance, Direction side, INBT nbt) {}
        }, () -> PtMagnet.create(IMagnet.MagneticPole.N, 0)));

    }
}
