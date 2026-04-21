package github.jodevnull.minepkl.pkl;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Options;
import org.jetbrains.annotations.NotNull;
import org.pkl.core.SecurityManager;
import org.pkl.core.SecurityManagerException;
import org.pkl.core.module.ModuleKey;
import org.pkl.core.module.ResolvedModuleKey;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class MinepklModuleKey implements ModuleKey, ResolvedModuleKey
{
    public static ModuleKey minepkl(URI uri) {
        return new MinepklModuleKey(uri);
    }

    final URI uri;

    MinepklModuleKey(URI uri) {
        if (!uri.getScheme().equals("minepkl")) {
            throw new IllegalArgumentException("Expected URI with scheme `minepkl`, but got: " + uri);
        }

        this.uri = uri;
    }

    @Override
    public @NotNull URI getUri() {
        return this.uri;
    }

    @Override
    public boolean hasHierarchicalUris() {
        return false;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public boolean isGlobbable() {
        return false;
    }

    @Override
    public @NotNull ResolvedModuleKey resolve(SecurityManager securityManager) throws SecurityManagerException {
        securityManager.checkResolveModule(uri);
        return this;
    }

    @Override
    public @NotNull ModuleKey getOriginal() {
        return this;
    }

    @Override
    public @NotNull String loadSource() throws IOException {
        String module = uri.getSchemeSpecificPart();

        if (module.equalsIgnoreCase("@generator")) {
            return Minepkl.getSourceFile("/minepkl/generator.pkl").orElseThrow();
        }

        Path path = Options.getMainDir().resolve(module + ".pkl");

        if (!Files.exists(path)) {
            throw new IOException(String.format("Script '%s' not found in minepkl dir", module));
        }

        return Files.readString(path);
    }
}