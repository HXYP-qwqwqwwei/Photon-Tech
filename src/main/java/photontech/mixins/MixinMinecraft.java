package photontech.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Minecraft.class, priority = 1001)
public abstract class MixinMinecraft {

    @Inject(method = "createTitle", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void onCreateTitle(CallbackInfoReturnable<String> cir, StringBuilder stringbuilder, ClientPlayNetHandler clientplaynethandler) {
        cir.setReturnValue(stringbuilder.append("-PtTest").toString());
    }


}
