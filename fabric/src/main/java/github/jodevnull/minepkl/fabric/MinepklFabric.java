package github.jodevnull.minepkl.fabric;

import github.jodevnull.minepkl.Minepkl;
import net.fabricmc.api.ModInitializer;

public final class MinepklFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Minepkl.init();
    }
}
