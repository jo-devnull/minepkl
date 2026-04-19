package github.jodevnull.minepkl.fabric;

import github.jodevnull.minepkl.Platform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformFabric implements Platform {
    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }
}