package github.jodevnull.minepkl;

import java.nio.file.Path;
import java.util.List;

public interface Platform
{
    Path getGameDir();

    Boolean isModLoaded(String modId);

    List<String> getModList();
}
