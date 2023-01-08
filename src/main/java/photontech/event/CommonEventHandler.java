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
import photontech.utils.capability.electric.EtTransmissionLine;
import photontech.utils.capability.electric.IEtCapacitor;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.heat.PtHeatReservoir;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.kinetic.KtRotateBody;
import photontech.utils.capability.magnet.IMagnet;
import photontech.utils.capability.magnet.PtMagnet;

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

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(IRotateBody.class, new Capability.IStorage<IRotateBody>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IRotateBody> capability, IRotateBody instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IRotateBody> capability, IRotateBody instance, Direction side, INBT nbt) {}
        }, () -> KtRotateBody.create(1000)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(IEtCapacitor.class, new Capability.IStorage<IEtCapacitor>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IEtCapacitor> capability, IEtCapacitor instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<IEtCapacitor> capability, IEtCapacitor instance, Direction side, INBT nbt) {}
        }, () -> EtTransmissionLine.create(1000, Long.MAX_VALUE)));

        event.enqueueWork(() -> CapabilityManager.INSTANCE.register(IMagnet.class, new Capability.IStorage<IMagnet>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IMagnet> capability, IMagnet instance, Direction side) {return null;}

            @Override
            public void readNBT(Capability<IMagnet> capability, IMagnet instance, Direction side, INBT nbt) {}
        }, () -> PtMagnet.create(IMagnet.MagneticPole.N, 0)));

    }
}
