package github.jodevnull.minepkl.neoforge;

import github.jodevnull.minepkl.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.List;

public class PlatformNeoforge implements Platform
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
