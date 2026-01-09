package github.jodevnull.minepkl.core.resources;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.core.PklEvaluator;
import github.jodevnull.minepkl.core.PathUtils;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalResources
{
    public static void generateExternalFiles() {
        File directory = new File(PlatHelper.getGamePath().toUri());

        for (var entry : PklEvaluator.getExternal().entrySet()) {
            File file = new File(PlatHelper.getGamePath() + "/" + entry.getKey());

            if (entry.getKey().endsWith(File.separator)) {
                Minepkl.setErrorAndLog("Path cannot be a directory: %s", entry.getKey());
                continue;
        }

            if (Path.of(entry.getKey()).isAbsolute()) {
                Minepkl.setErrorAndLog("Cannot generate file with absolute path: %s", entry.getKey());
                continue;
            }

            try {
                if (!PathUtils.isInsideOf(directory, file)) {
                    Minepkl.setErrorAndLog("Path outside of instance directory: %s", entry.getKey());
                    continue;
                }

                File output = new File(PlatHelper.getGamePath() + File.separator + entry.getKey());

                // TODO: Make this configurable
                Files.createDirectories(Paths.get(output.getParent()));
                Files.write(output.toPath(), entry.getValue().getBytes(StandardCharsets.UTF_8));
                Minepkl.LOGGER.info("[pkl:external] file writen to {}", output.toPath());
            } catch (Exception e) {
                Minepkl.setErrorAndLog("Exception generating external file: %s\n%s", entry.getKey(), e);
            }
        }
    }
}
