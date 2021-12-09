//package photontech.mixins;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.network.play.ClientPlayNetHandler;
//import org.apache.logging.log4j.LogManager;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//
//@Mixin(value = Minecraft.class)
//public abstract class MixinMinecraft2 {
//    @Inject(method = "createTitle", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//    private void onCreateTitle2(CallbackInfoReturnable<String> cir, StringBuilder stringbuilder, ClientPlayNetHandler clientplaynethandler) {
//        for (int i = 0; i < 10; ++i)
//            LogManager.getLogger().info(stringbuilder.toString());
//    }
//}
