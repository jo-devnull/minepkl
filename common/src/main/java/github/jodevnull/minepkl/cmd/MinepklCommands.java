package github.jodevnull.minepkl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.pack.PackGenerator;
import github.jodevnull.minepkl.pkl.MinepklEvaluator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MinepklCommands
{
    private enum CommandType
    {
        All,
        Packs,
        External;
    }

    public static void createCommand(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal(Minepkl.MOD_ID)
            .then(Commands.literal("build")
                .then(Commands.literal("all").executes(ctx -> build(ctx, CommandType.All)))
                .then(Commands.literal("packs").executes(ctx -> build(ctx, CommandType.Packs)))
                .then(Commands.literal("external").executes(ctx -> build(ctx, CommandType.External)))
                .executes(ctx -> build(ctx, CommandType.All))));
    }

    private static int build(CommandContext<CommandSourceStack> context, CommandType type)
    {
        switch (type)
        {
            case All: PackGenerator.generate(); break;
            case Packs: PackGenerator.generatePack(); break;
            case External: PackGenerator.generateExternalFiles(); break;
        }

        if (MinepklEvaluator.hasError()) {
            Minepkl.logError(context.getSource().getPlayer(), "[pkl] Error generating files:");
            Minepkl.logError(context.getSource().getPlayer(), "%s", MinepklEvaluator.popError());
            return 0;
        }

        context.getSource().sendSystemMessage(Component.literal("Building pkl files completed!"));
        return 1;
    }
}
