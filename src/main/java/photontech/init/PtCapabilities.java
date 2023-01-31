package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.block.kinetic.KineticState;
import photontech.utils.capability.electric.IEtCapacitor;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.magnet.IMagnet;

public class PtCapabilities {
    @CapabilityInject(IHeatReservoir.class)
    public static Capability<IHeatReservoir> HEAT_RESERVOIR;

    @CapabilityInject(KineticState.class)
    public static Capability<KineticState> KINETIC_STATE;

    @CapabilityInject(IEtCapacitor.class)
    public static Capability<IEtCapacitor> CONDUCTOR;

    @CapabilityInject(IMagnet.class)
    public static Capability<IMagnet> MAGNET;
}
