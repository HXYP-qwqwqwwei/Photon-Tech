package photontech.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import photontech.block.kinetic.KineticState;
import photontech.utils.data.Hydraulic;
import photontech.utils.data.electric.ElectricCapacitor;
import photontech.utils.data.heat.HeatReservoir;
import photontech.utils.data.magnet.Magnet;

public class PtCapabilities {
    @CapabilityInject(HeatReservoir.class)
    public static Capability<HeatReservoir> HEAT_RESERVOIR;

    @CapabilityInject(KineticState.class)
    public static Capability<KineticState> KINETIC_STATE;

    @CapabilityInject(ElectricCapacitor.class)
    public static Capability<ElectricCapacitor> CONDUCTOR;

    @CapabilityInject(Magnet.class)
    public static Capability<Magnet> MAGNET;

    @CapabilityInject(Hydraulic.class)
    public static Capability<Hydraulic> HYDRAULIC_PIPE;
}
