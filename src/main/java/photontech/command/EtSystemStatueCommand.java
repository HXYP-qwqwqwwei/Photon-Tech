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

public class EtSystemStatueCommand implements Command<CommandSource> {
    public static EtSystemStatueCommand INSTANCE = new EtSystemStatueCommand();

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        MinecraftServer server = context.getSource().getServer();
        ServerWorld serverWorld = server.getLevel(World.OVERWORLD);
        DCWireDataManager data = DCWireDataManager.getData(serverWorld);
        int size = data.getSize();
        context.getSource().sendSuccess(new TranslationTextComponent(
                "cmd." + PhotonTech.ID + ".etsys", size), false
        );
        return 0;
    }
}
