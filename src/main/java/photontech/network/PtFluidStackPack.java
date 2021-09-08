package photontech.network;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import photontech.utils.capability.fluid.PtMultiFluidTank;

import java.util.function.Supplier;

public class PtFluidStackPack {
//    private final String message;
    private static final Logger LOGGER = LogManager.getLogger();
    private final PtMultiFluidTank tanks = new PtMultiFluidTank();

    public PtFluidStackPack(PacketBuffer buffer) {
        this.tanks.readFromNBT(buffer.readNbt());
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeNbt(tanks.writeToNBT(new CompoundNBT()));
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

        });
        ctx.get().setPacketHandled(true);
    }
}
