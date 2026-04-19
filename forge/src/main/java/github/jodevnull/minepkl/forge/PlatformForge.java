package github.jodevnull.minepkl.forge;

import github.jodevnull.minepkl.Platform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PlatformForge implements Platform
{
    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }
}
