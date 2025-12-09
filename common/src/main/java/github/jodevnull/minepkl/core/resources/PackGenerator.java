package github.jodevnull.minepkl.core.resources;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Options;
import github.jodevnull.minepkl.core.PklEvaluator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackGenerator
{
    private static final String META = """
            {
                "pack": {
                    "pack_format": 15,
                    "description": "Minepkl generated resources"
                }
            }""";

    public static void generatePack() {
        Path outputZipPath = Options.getOutputZipFile();

        Stream<Map.Entry<String, String>> outputFiles = Stream.concat(
            PklEvaluator.getData().entrySet().stream(),
            PklEvaluator.getAssets().entrySet().stream()
        );

        try {
            if (Files.exists(outputZipPath)) Files.delete(outputZipPath);
        } catch (IOException e) {
            Minepkl.LOGGER.error("Error deleting '{}'", outputZipPath);
        }

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputZipPath))) {
            Files.createDirectories(outputZipPath.getParent());

            ZipEntry mcmeta = new ZipEntry("pack.mcmeta");
            zos.putNextEntry(mcmeta);
            zos.write(META.getBytes());
            zos.closeEntry();

            for (Map.Entry<String, String> entry : outputFiles.toList()) {
                String filePath = entry.getKey();
                String content  = entry.getValue();

                ZipEntry zipEntry = new ZipEntry(filePath);
                zos.putNextEntry(zipEntry);
                zos.write(content.getBytes(StandardCharsets.UTF_8));
                zos.closeEntry();
            }
        } catch (IOException e) {
            Minepkl.LOGGER.error("Failed to create output zip file:");
            Minepkl.LOGGER.error(e);
        }
    }
}
