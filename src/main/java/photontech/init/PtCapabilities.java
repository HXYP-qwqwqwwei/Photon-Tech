package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.kinetic.IRotateBody;

public class PtCapabilities {
    @CapabilityInject(IHeatReservoir.class)
    public static Capability<IHeatReservoir> HEAT_RESERVOIR;
    @CapabilityInject(IRotateBody.class)
    public static Capability<IRotateBody> RIGID_BODY;
}
