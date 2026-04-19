package github.jodevnull.minepkl.core;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Options;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackGenerator
{
    public static void generate() {
        generatePack();
        generateExternalFiles();
    }

    public static void generatePack() {
        HashMap<String, String> outputFiles = PklEvaluator.buildPack(Options.getBuildFilePath());
        HashSet<String> outputPacks = new HashSet<>();

        if (PklEvaluator.hasError()) {
            return;
        }

        for (var entry : outputFiles.entrySet()) {
            Path path = Path.of(entry.getKey());
            outputPacks.add(path.getName(0).toString());
        }

        for (String packfile : outputPacks) {
            Path outputZipPath = Options.getOutputZipFile(packfile);

            try {
                Files.createDirectories(Path.of(Options.getConfigDir() + "/generated"));

                if (Files.exists(outputZipPath))
                    Files.delete(outputZipPath);
            } catch (IOException e) {
                Minepkl.LOGGER.error("Error deleting '{}'", outputZipPath);
            }

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputZipPath))) {
                Files.createDirectories(outputZipPath.getParent());

                for (var entry : outputFiles.entrySet()) {
                    if (!entry.getKey().startsWith(packfile)) continue;

                    String filePath = entry.getKey().replace(packfile + "/", "");
                    String content  = entry.getValue();

                    ZipEntry zipEntry = new ZipEntry(filePath);
                    zos.putNextEntry(zipEntry);
                    zos.write(content.getBytes(StandardCharsets.UTF_8));
                    zos.closeEntry();
                }
            } catch (IOException e) {
                Minepkl.LOGGER.error("Failed to create output zip file for {}:", packfile);
                Minepkl.LOGGER.error(e);
                PklEvaluator.pushError(e);
            }
        }
    }

    public static void generateExternalFiles() {
        File directory = new File(Minepkl.PLATFORM.getGameDir().toUri());

        for (var entry : PklEvaluator.buildExternal().entrySet()) {
            File file = new File(Minepkl.PLATFORM.getGameDir() + "/" + entry.getKey());

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

                File output = new File(Minepkl.PLATFORM.getGameDir() + File.separator + entry.getKey());

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
