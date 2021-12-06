package photontech.mixins;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    /**
     * @author HXYP
     * @reason For Test
     */
    @Overwrite
    private String createTitle() {
        return "PhotonTech";
    }
}
