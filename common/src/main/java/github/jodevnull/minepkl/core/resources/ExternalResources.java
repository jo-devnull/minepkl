package github.jodevnull.minepkl.core.resources;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.core.PklEvaluator;
import github.jodevnull.minepkl.core.PathUtils;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExternalResources
{
    public static void generateExternalFiles() {
        File directory = new File(PlatHelper.getGamePath().toUri());

        for (var entry : PklEvaluator.getExternal().entrySet()) {
            File file = new File(entry.getKey());

            if (entry.getKey().endsWith(File.separator)) {
                Minepkl.LOGGER.error("Path cannot be a directory: {}", entry.getKey());
                continue;
            }

            if (file.isAbsolute()) {
                Minepkl.LOGGER.error("Cannot generate file with absolute path: {}", entry.getKey());
                continue;
            }

            try {
                if (!PathUtils.isInsideOf(directory, file)) {
                    Minepkl.LOGGER.error("Path outside of instance directory: {}", entry.getKey());
                    continue;
                }

                File output = new File(PlatHelper.getGamePath() + File.separator + entry.getKey());

                // TODO: Make this configurable
                Files.createDirectories(Paths.get(output.getParent()));
                Files.write(output.toPath(), entry.getValue().getBytes());
                Minepkl.LOGGER.info("[pkl:external] file writen to {}", output.toPath());
            } catch (Exception e) {
                Minepkl.LOGGER.error("Exception generating external file: {}", entry.getKey());
                Minepkl.LOGGER.error(e);
            }
        }
    }
}
