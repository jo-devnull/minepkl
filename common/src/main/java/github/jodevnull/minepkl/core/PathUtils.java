package github.jodevnull.minepkl.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PathUtils
{
    public static boolean isInsideOf(File directory, File other) throws IOException {
        String fileCanonicalPath = other.getCanonicalPath();
        String directoryCanonicalPath = directory.getCanonicalPath();

        // Ensure the directory path ends with a separator for accurate comparison
        if (!directoryCanonicalPath.endsWith(File.separator))
            directoryCanonicalPath += File.separator;

        return fileCanonicalPath.startsWith(directoryCanonicalPath);
    }

    public static boolean isInsideOf(Path directory, Path other) throws IOException {
        return isInsideOf(new File(directory.toUri()), new File(other.toUri()));
    }
}
