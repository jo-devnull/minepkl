package github.jodevnull.minepkl.fabric;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Platform;
import github.jodevnull.minepkl.cmd.MinepklCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class MinepklFabric implements ModInitializer {
    public static final Platform platform = new PlatformFabric();

    static {
        Minepkl.setPlatform(platform);
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Register commands
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> MinepklCommands.createCommand(dispatcher)));

        // Run our common setup.
        Minepkl.init();
    }
}
