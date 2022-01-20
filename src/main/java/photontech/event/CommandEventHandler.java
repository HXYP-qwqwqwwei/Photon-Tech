//package photontech.event;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.tree.LiteralCommandNode;
//import net.minecraft.command.CommandSource;
//import net.minecraft.command.Commands;
//import net.minecraftforge.event.RegisterCommandsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import photontech.command.PtThreadTPSCommand;
//import photontech.utils.Utils;
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class CommandEventHandler {
//    @SubscribeEvent
//    public static void onCommandRegister(RegisterCommandsEvent event) {
//        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
//        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
//                Commands.literal(Utils.MOD_ID).then(
//                        Commands.literal("ptthreadtps")
//                                .requires((commandSource) -> commandSource.hasPermission(0))
//                                .executes(PtThreadTPSCommand.INSTANCE)
//                )
//        );
//    }
//}
