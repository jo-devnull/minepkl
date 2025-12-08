package github.jodevnull.minepkl;

import github.jodevnull.minepkl.resources.DynClientResources;
import github.jodevnull.minepkl.resources.DynServerResources;
import github.jodevnull.minepkl.resources.ExternalResources;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pkl.core.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Minepkl
{
    public static final String MOD_ID = "minepkl";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final Path PKL_DIR = Path.of(PlatHelper.getGamePath() + File.separator + "pkl");

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }

    public static void init() {
        DynClientResources.init();
        DynServerResources.init();

        PlatHelper.addCommonSetup(() -> {
            Minepkl.writeDefaultFiles();
            ExternalResources.generateExternalFiles();
        });
    }

    public static void writeDefaultFiles() {
        try {
            Files.createDirectories(PKL_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeSourceFile("/minepkl/data.pkl");
        writeSourceFile("/minepkl/asset.pkl");
        writeSourceFile("/minepkl/external.pkl");
    }

    public static void writeSourceFile(String path) {
        Path filename = Path.of(path).getFileName();
        Path outputFile = Path.of(PKL_DIR + File.separator + filename);

        if (new File(outputFile.toUri()).exists()) {
            LOGGER.info("'pkl/{}' already exists, skiping...", filename);
            return;
        }

        getSourceFile(path).ifPresent(source -> {
            try {
                Files.write(outputFile, source.getBytes());
            } catch (IOException e) {
                LOGGER.error("Error writing default pkl files:");
                LOGGER.error(e);
            }
        });
    }

    public static Optional<String> getSourceFile(String path) {
        try (InputStream stream = Minepkl.class.getResourceAsStream(path)) {
            if (stream == null) {
                LOGGER.error("InputStream for '{}' is null! This isn't suppose to happen!", path);
                return Optional.empty();
            }

            return Optional.of(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("Error generating default .pkl files:");
            LOGGER.error(e);
        }

        return Optional.empty();
    }

    public static Map<String, String> getAssets() {
        // TODO: Make this configurable
        return getPklOutput("pkl/asset.pkl");
    }

    public static Map<String, String> getData() {
        // TODO: Make this configurable
        return getPklOutput("pkl/data.pkl");
    }

    public static Map<String, String> getExternal() {
        // TODO: Make this configurable
        return getPklOutput("pkl/external.pkl");
    }

    public static Map<String, String> getPklOutput(String pklFilePath) {
        HashMap<String, String> output = new HashMap<>();

        try (Evaluator evaluator = Evaluator.preconfigured()) {
            ModuleSource source = ModuleSource.file(PlatHelper.getGamePath() + File.separator + pklFilePath);

            for (var entry : evaluator.evaluateOutputFiles(source).entrySet())
                output.put(entry.getKey(), entry.getValue().getText());
        } catch (Exception e) {
            LOGGER.error("Exception while running {} (No files generated)", pklFilePath);
            LOGGER.error(e);
        }

        return output;
    }
}
