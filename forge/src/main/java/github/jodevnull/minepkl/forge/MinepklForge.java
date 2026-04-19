package github.jodevnull.minepkl.forge;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Platform;
import github.jodevnull.minepkl.core.command.MinepklCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Minepkl.MOD_ID)
public final class MinepklForge
{
    static final Platform platform = new PlatformForge();

    static {
        Minepkl.setPlatform(platform);
    }

    public MinepklForge() {
        // Run our common setup.
        Minepkl.init();
        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
    }

    private void registerCommand(RegisterCommandsEvent event) {
        MinepklCommands.createCommand(event.getDispatcher());
    }
}
