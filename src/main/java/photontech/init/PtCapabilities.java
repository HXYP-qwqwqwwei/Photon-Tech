package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.block.kinetic.KineticState;
import photontech.utils.data.electric.ICapacitor;
import photontech.utils.data.heat.IHeatReservoir;
import photontech.utils.data.magnet.IMagnet;

public class PtCapabilities {
    @CapabilityInject(IHeatReservoir.class)
    public static Capability<IHeatReservoir> HEAT_RESERVOIR;

    @CapabilityInject(KineticState.class)
    public static Capability<KineticState> KINETIC_STATE;

    @CapabilityInject(ICapacitor.class)
    public static Capability<ICapacitor> CONDUCTOR;

    @CapabilityInject(IMagnet.class)
    public static Capability<IMagnet> MAGNET;
}
