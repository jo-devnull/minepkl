package github.jodevnull.minepkl.core.resources;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.core.PathUtils;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import org.pkl.core.SecurityManagerException;
import org.pkl.core.externalreader.ExternalReaderProcessException;
import org.pkl.core.resource.ResourceReader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@MethodsReturnNonnullByDefault
public final class InstanceResourceReader implements ResourceReader {
    public static final InstanceResourceReader INSTANCE = new InstanceResourceReader();

    @Override
    public String getUriScheme() { return "instance"; }

    @Override
    public Optional<Object> read(URI uri) throws IOException, URISyntaxException, SecurityManagerException, ExternalReaderProcessException {
        if (!uri.getScheme().equals("instance")) {
            Minepkl.LOGGER.error("Invalid uri scheme: {}", uri);
            return Optional.empty();
        }

        if (Path.of(uri.getSchemeSpecificPart()).isAbsolute()) {
            Minepkl.LOGGER.error("Attempted to read absolute file: '{}'", uri.getSchemeSpecificPart());
            return Optional.empty();
        }

        Path filepath = Path.of(PlatHelper.getGamePath() + File.separator + uri.getSchemeSpecificPart()).normalize();

        if (!PathUtils.isInsideOf(PlatHelper.getGamePath(), filepath)) {
            Minepkl.LOGGER.error("Attempted to read file outside of minecraft instance: '{}'", filepath);
            return Optional.empty();
        }

        return Optional.of(Files.readString(filepath));
    }

    @Override
    public boolean hasHierarchicalUris() throws ExternalReaderProcessException, IOException {
        return false;
    }

    @Override
    public boolean isGlobbable() throws ExternalReaderProcessException, IOException {
        return false;
    }
}
