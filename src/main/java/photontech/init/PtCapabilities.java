package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.utils.capability.heat.IHeatReservoir;
import photontech.utils.capability.kinetic.IRigidBody;

public class PtCapabilities {
    @CapabilityInject(IHeatReservoir.class)
    public static Capability<IHeatReservoir> HEAT_RESERVOIR;
    @CapabilityInject(IRigidBody.class)
    public static Capability<IRigidBody> RIGID_BODY;
}
