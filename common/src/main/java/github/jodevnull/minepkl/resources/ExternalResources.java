package github.jodevnull.minepkl.resources;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.mojang.text2speech.Narrator.LOGGER;
import static github.jodevnull.minepkl.Minepkl.getExternal;

public class ExternalResources
{
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

                // TODO: Make this configurable
                Files.createDirectories(Paths.get(output.getParent()));
                Files.write(output.toPath(), entry.getValue().getBytes());
                LOGGER.info("[pkl:external] file writen to {}", output.toPath());
            } catch (IOException e) {
                LOGGER.error("Exception generating external file: {}", entry.getKey());
            }
        }
    }
}
