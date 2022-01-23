package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.kinetic.IMutableBody;
import photontech.utils.capability.kinetic.IRotateBody;
import photontech.utils.capability.magnet.IMagnet;

public class PtCapabilities {
    @CapabilityInject(IHeatReservoir.class)
    public static Capability<IHeatReservoir> HEAT_RESERVOIR;
    @CapabilityInject(IMutableBody.class)
    public static Capability<IMutableBody> RIGID_BODY;
    @CapabilityInject(IMutableConductor.class)
    public static Capability<IMutableConductor> CONDUCTOR;
    @CapabilityInject(IMagnet.class)
    public static Capability<IMagnet> MAGNET;
}
