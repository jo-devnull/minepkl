package github.jodevnull.minepkl;

import github.jodevnull.minepkl.core.PklEvaluator;
import github.jodevnull.minepkl.core.resources.ExternalResources;
import github.jodevnull.minepkl.core.resources.PackGenerator;
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
import java.util.Optional;

public final class Minepkl
{
    public static final String MOD_ID = "minepkl";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }

    public static void init() {
        PklEvaluator.init();
        Minepkl.writeDefaultFiles();
        PackGenerator.generatePack();
        ExternalResources.generateExternalFiles();
    }

    public static void writeDefaultFiles() {
        try {
            Files.createDirectories(Options.getMainDir());
        } catch (IOException e) {
            LOGGER.error("Failed to create '{}/' directory", Options.MAIN_DIR);
            return;
        }

        writeSourceFile("/minepkl/data.pkl", Options.getDataPath());
        writeSourceFile("/minepkl/assets.pkl", Options.getAssetsPath());
        writeSourceFile("/minepkl/external.pkl", Options.getExternalPath());
    }

    public static void writeSourceFile(String source, Path output) {
        if (new File(output.toUri()).exists()) {
            LOGGER.info("'{}' already exists, skiping...", getRelative(output));
            return;
        }

        getSourceFile(source).ifPresent(file -> {
            try {
                Files.write(output, file.getBytes());
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
            LOGGER.error("Error generating default {} file:", path);
            LOGGER.error(e);
        }

        return Optional.empty();
    }

    public static String getRelative(Path absolutePath) {
        return absolutePath.toString().replace(PlatHelper.getGamePath().toString(), "");
    }
}
