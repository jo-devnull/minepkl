package github.jodevnull.minepkl.pack;

import github.jodevnull.minepkl.Minepkl;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class MinepklPackRepository implements RepositorySource
{
    private final static PackSource SOURCE = PackSource.create(packText -> packText, true);;
    private final PackType packType;
    private final File sourceDir;

    public MinepklPackRepository(PackType type, Path gameDir)
    {
        this.packType = type;
        this.sourceDir = new File(Path.of(gameDir + "/config/minepkl/generated/").toUri());

        if (!sourceDir.exists())
            sourceDir.mkdirs();
        if (!sourceDir.isDirectory())
            Minepkl.LOGGER.error("[pkl] Expected 'config/minepkl/generated' to be a directory");
    }

    @Override
    public void loadPacks(@NotNull Consumer<Pack> consumer) {
        for (File file : Objects.requireNonNull(sourceDir.listFiles())) {
            if (!file.getName().endsWith(".zip"))
                continue;

            final String packName = file.getName();
            final Component displayName = Component.literal("[pkl] " + packName);
            final PackLocationInfo locationInfo = new PackLocationInfo(packName, displayName, SOURCE, Optional.empty());
            final PackSelectionConfig selectionConfig = new PackSelectionConfig(true, Pack.Position.TOP, false);
            final FilePackResources.FileResourcesSupplier supplier = new FilePackResources.FileResourcesSupplier(file);

            consumer.accept(Pack.readMetaAndCreate(locationInfo, supplier, this.packType, selectionConfig));
            Minepkl.LOGGER.info("Successfully loaded generated pack: {}", packName);
        }
    }
}
