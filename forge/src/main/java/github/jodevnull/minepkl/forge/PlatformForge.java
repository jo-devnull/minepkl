package github.jodevnull.minepkl.forge;

import github.jodevnull.minepkl.Platform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.List;

public class PlatformForge implements Platform
{
    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public List<String> getModList() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
    }
}
