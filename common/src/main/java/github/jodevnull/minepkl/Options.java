package github.jodevnull.minepkl;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

import java.nio.file.Path;

public class Options
{
    // TODO: make this configurable
    public static final String MAIN_DIR = "minepkl";
    public static final String ASSETS   = "assets.pkl";
    public static final String DATA     = "data.pkl";
    public static final String EXTERNAL = "external.pkl";

    public static final String PACK_OUTPUT = "resourcepacks/minepkl@generated.zip";

    public static Path getOutputZipFile() {
        return Path.of(PlatHelper.getGamePath() + "/" + PACK_OUTPUT);
    }

    public static Path getMainDir() {
        return Path.of(PlatHelper.getGamePath() + "/" + MAIN_DIR);
    }

    public static Path getAssetsPath() {
        return Path.of(getMainDir() + "/" + ASSETS);
    }

    public static Path getDataPath() {
        return Path.of(getMainDir() + "/" + DATA);
    }

    public static Path getExternalPath() {
        return Path.of(getMainDir() + "/" + EXTERNAL);
    }

    public static boolean getUseRootDir() {
        return System.getProperty("minepkl.rootdir", "true").equals("true");
    }
}
