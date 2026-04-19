package github.jodevnull.minepkl;

import java.nio.file.Path;

public class Options
{
    // TODO: make this configurable
    public static final String MAIN_DIR = "minepkl";
    public static final String BUILD    = "build.pkl";
    public static final String EXTERNAL = "external.pkl";
    public static final String CONFIG   = "pack.json";

    // private static JsonObject config = new JsonObject();

    public static void load() {
        // File configfile = new File(getConfigFilePath().toUri());
        //
        // if (configfile.exists()) {
        //     Gson gson = new Gson();
        //
        //     try {
        //         config = gson.fromJson(Files.readString(configfile.toPath()), JsonObject.class);
        //     } catch (IOException e) {
        //         Minepkl.LOGGER.error("Error loading minepkl/pack.json:", e);
        //     }
        // }
    }

    public static Path getOutputZipFile(String name) {
        Path outputFile = Path.of("config/minepkl/generated/" + name + ".zip");

        // if (config.has("outputFile")) {
        //     var output = config.get("outputFile").getAsJsonObject();
        //
        //     if (output.has(name)) {
        //         outputFile = Path.of(output.get(name).getAsString());
        //     }
        // }
        //
        // if (outputFile.isAbsolute())
        //     return outputFile;

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
