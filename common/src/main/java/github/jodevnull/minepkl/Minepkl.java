package github.jodevnull.minepkl;

import github.jodevnull.minepkl.resources.DynClientResources;
import github.jodevnull.minepkl.resources.DynServerResources;
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

    private static final Evaluator EVALUATOR = EvaluatorBuilder
        .preconfigured()
        .build();

    private static final Path PKL_DIR = Path.of(PlatHelper.getGamePath() + File.separator + "pkl");

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }

    public static void init() {
        DynClientResources.init();
        DynServerResources.init();

        PlatHelper.addCommonSetup(Minepkl::writeDefaultFiles);
        PlatHelper.addCommonSetup(Minepkl::generateExternalFiles);
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

    public static Map<String, FileOutput> getAssets() {
        // TODO: Make this configurable
        return getPklOutput("pkl/asset.pkl");
    }

    public static Map<String, FileOutput> getData() {
        // TODO: Make this configurable
        return getPklOutput("pkl/data.pkl");
    }

    public static Map<String, FileOutput> getExternal() {
        // TODO: Make this configurable
        return getPklOutput("pkl/external.pkl");
    }

    public static Map<String, FileOutput> getPklOutput(String pklFilePath) {
        try {
            ModuleSource source = ModuleSource.file(PlatHelper.getGamePath() + File.separator + pklFilePath);
            return EVALUATOR.evaluateOutputFiles(source);
        } catch (Exception e) {
            LOGGER.error("Exception while running {} (No files generated)", pklFilePath);
            LOGGER.error(e);
            return new HashMap<>();
        }
    }

    public static void generateExternalFiles() {
        File directory = new File(PlatHelper.getGamePath().toUri());

        for (var entry : getExternal().entrySet()) {
            File file = new File(entry.getKey());

            if (entry.getKey().endsWith(File.separator)) {
                LOGGER.error("Path cannot be a directory: {}", entry.getKey());
                continue;
            }

            if (file.isAbsolute()) {
                LOGGER.error("Cannot generate file with absolute path: {}", entry.getKey());
                continue;
            }

            try {
                String fileCanonicalPath = file.getCanonicalPath();
                String directoryCanonicalPath = directory.getCanonicalPath();

                // Ensure the directory path ends with a separator for accurate comparison
                if (!directoryCanonicalPath.endsWith(File.separator))
                    directoryCanonicalPath += File.separator;

                if (!fileCanonicalPath.startsWith(directoryCanonicalPath)) {
                    LOGGER.error("Path outside of instance directory: {}", entry.getKey());
                    continue;
                }

                File output = new File(PlatHelper.getGamePath() + File.separator + entry.getKey());

                if (output.exists()) {
                    LOGGER.warn("File '{}' already exists, ignoring...", file.getPath());
                    continue;
                }

                Files.createDirectories(Paths.get(output.getParent()));
                Files.write(output.toPath(), entry.getValue().getBytes());
                LOGGER.info("[pkl:external] file writen to {}", output.toPath());
            } catch (IOException e) {
                LOGGER.error("Exception generating external file: {}", entry.getKey());
            }
        }
    }
}
