package photontech.init;

import photontech.block.heater.solid.PtBurningItemHeaterContainer;
import photontech.block.crucible.PtCrucibleContainer;
import photontech.utils.Utils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PtContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Utils.MOD_ID);
    public static final RegistryObject<ContainerType<PtCrucibleContainer>> CRUCIBLE_CONTAINER = CONTAINERS.register("crucible_container", () -> IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) -> new PtCrucibleContainer(windowId, inv, data.readBlockPos(), inv.player.getCommandSenderWorld())));
    public static final RegistryObject<ContainerType<PtBurningItemHeaterContainer>> HEATER_CONTAINER = CONTAINERS.register("heater_container", () -> IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) -> new PtBurningItemHeaterContainer(windowId, inv, data.readBlockPos(), inv.player.getCommandSenderWorld())));
}