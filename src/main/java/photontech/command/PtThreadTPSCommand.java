//package photontech.command;
//
//import com.mojang.brigadier.Command;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.command.CommandSource;
//import net.minecraft.util.text.TranslationTextComponent;
//import photontech.event.PtExtraThread;
//import photontech.event.PtExtraThreadHandler;
//import photontech.utils.Utils;
//
//public class PtThreadTPSCommand implements Command<CommandSource> {
//    public static PtThreadTPSCommand INSTANCE = new PtThreadTPSCommand();
//
//    @Override
//    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
//        double tickCostMilliSeconds = PtExtraThread.PT_TICK_COST * 0.001;
//        double tps = 1000.0 / (tickCostMilliSeconds < PtExtraThreadHandler.PERIOD ? PtExtraThreadHandler.PERIOD : tickCostMilliSeconds);
//        context.getSource().sendSuccess(new TranslationTextComponent(
//                "cmd." + Utils.MOD_ID + ".pttps",
//                PtExtraThread.MACHINE_TABLE.size(), PtExtraThread.REGISTER_TASKS.size(), PtExtraThread.REMOVE_TASKS.size(), PtExtraThread.NORMAL_TASKS.size(),
//                String.format("%.2f", tickCostMilliSeconds),
//                String.format("%.2f", PtExtraThread.PT_TICK_MACHINE_COST * 0.001),
//                String.format("%.2f", PtExtraThread.PT_TICK_REMOVE_COST * 0.001),
//                String.format("%.2f", PtExtraThread.PT_TICK_REGISTER_COST * 0.001),
//                String.format("%.2f", PtExtraThread.PT_TICK_TASK_COST * 0.001),
//                String.format("%.2f", tps)),
//                false
//        );
//        return 0;
//    }
//}
