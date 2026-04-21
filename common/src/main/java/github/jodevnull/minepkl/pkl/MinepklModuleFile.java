package github.jodevnull.minepkl.pkl;

import org.jetbrains.annotations.NotNull;
import org.pkl.core.module.ModuleKey;
import org.pkl.core.module.ModuleKeyFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class MinepklModuleFile implements ModuleKeyFactory
{
    public static final ModuleKeyFactory INSTANCE = new MinepklModuleFile();

    @Override
    public @NotNull Optional<ModuleKey> create(URI uri) throws URISyntaxException{
        if (uri.getScheme().equalsIgnoreCase("minepkl")) {
            return Optional.of(MinepklModuleKey.minepkl(uri));
        }

        return Optional.empty();
    }
}