package photontech.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import photontech.PhotonTech;
import photontech.data.DCWireDataManager;
import photontech.data.HydraulicDataManager;

public class PipeDataSystemStatueCommand implements Command<CommandSource> {
    public static PipeDataSystemStatueCommand INSTANCE = new PipeDataSystemStatueCommand();

    @Override
    public int run(CommandContext<CommandSource> context) {
        MinecraftServer server = context.getSource().getServer();
        ServerWorld serverWorld = server.getLevel(World.OVERWORLD);
        int etSysSize = DCWireDataManager.getData(serverWorld).getSize();
        int hydraSysSize = HydraulicDataManager.getData(serverWorld).getSize();
        context.getSource().sendSuccess(new TranslationTextComponent(
                "cmd." + PhotonTech.ID + ".etsys", etSysSize, hydraSysSize), false
        );
        return 0;
    }
}
