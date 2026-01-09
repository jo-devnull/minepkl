package github.jodevnull.minepkl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Options
{
    // TODO: make this configurable
    public static final String MAIN_DIR = "minepkl";
    public static final String ASSETS   = "assets.pkl";
    public static final String DATA     = "data.pkl";
    public static final String EXTERNAL = "external.pkl";
    public static final String CONFIG   = "pack.json";

    private static JsonObject config = new JsonObject();

    public static void load() {
        File configfile = new File(getConfigFilePath().toUri());

        if (configfile.exists()) {
            Gson gson = new Gson();

            try {
                config = gson.fromJson(Files.readString(configfile.toPath()), JsonObject.class);
            } catch (IOException e) {
                Minepkl.LOGGER.error("Error loading minepkl/pack.json:", e);
            }
        }
    }

    public static Path getOutputZipFile() {
        Path outputFile = Path.of("resourcepacks/minepkl@generated.zip");

        if (config.has("outputFile"))
            outputFile = Path.of(config.get("outputFile").getAsString());

        if (outputFile.isAbsolute())
            return outputFile;

        return Path.of(PlatHelper.getGamePath() + "/" + outputFile);
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
