package github.jodevnull.minepkl;

import java.nio.file.Path;

public class Options
{
    // TODO: make this configurable
    public static final String MAIN_DIR = "minepkl";
    public static final String BUILD    = "build.pkl";
    public static final String EXTERNAL = "external.pkl";
    public static final String CONFIG   = "pack.json";

    public static void load() {}

    public static Path getOutputZipFile(String name) {
        Path outputFile = Path.of("config/minepkl/generated/" + name + ".zip");
        return Path.of(Minepkl.PLATFORM.getGameDir() + "/" + outputFile);
    }

    public static Path getMainDir() {
        return Path.of(Minepkl.PLATFORM.getGameDir() + "/" + MAIN_DIR);
    }

    public static Path getConfigDir() {
        return Path.of(Minepkl.PLATFORM.getGameDir() + "/config/minepkl");
    }

    public static Path getGeneratorFilePath() {
        return Path.of(getConfigDir() + "/generator.pkl");
    }

    public static Path getBuildFilePath() {
        return Path.of(getMainDir() + "/" + BUILD);
    }

    public static Path getConfigFilePath() {
        return Path.of(getMainDir() + "/" + CONFIG);
    }

    public static Path getExternalPath() {
        return Path.of(getMainDir() + "/" + EXTERNAL);
    }

    public static boolean getUseRootDir() {
        return System.getProperty("minepkl.rootdir", "true").equals("true");
    }
}
