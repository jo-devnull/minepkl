package github.jodevnull.minepkl.pack;

import github.jodevnull.minepkl.Minepkl;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

public class MinepklPackRepository implements RepositorySource
{
    private final PackType packType;
    private final File sourceDir;
    private final PackSource sourceInfo;

    public MinepklPackRepository(PackType type, Path source)
    {
        this.packType = type;
        this.sourceDir = new File(Path.of(source + "/config/minepkl/generated/").toUri());
        this.sourceInfo = PackSource.create(name -> Component.literal(name.getString()).withStyle(ChatFormatting.GREEN), true);

        if (!sourceDir.exists())
            sourceDir.mkdirs();

        if (!sourceDir.isDirectory())
            Minepkl.LOGGER.error("[pkl] Expected 'config/minepkl/generated' to be a directory");
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        for (File file : Objects.requireNonNull(sourceDir.listFiles())) {
            if (file.getName().endsWith(".zip")) {
                final String packName = file.getName();
                final Component displayName = Component.literal("[pkl] " + packName);
                final Pack pack = Pack.readMetaAndCreate(packName, displayName, true, createPackSupplier(file), this.packType, Pack.Position.TOP, this.sourceInfo);

                if (pack != null) {
                    consumer.accept(pack);
                    Minepkl.LOGGER.info("Successfully loaded generated pack: {}", packName);
                }
            }
        }
    }

    private Pack.ResourcesSupplier createPackSupplier (File packFile) {
        return name -> packFile.isDirectory() ? new PathPackResources(name, packFile.toPath(), false) : new FilePackResources(name, packFile, false);
    }
}
