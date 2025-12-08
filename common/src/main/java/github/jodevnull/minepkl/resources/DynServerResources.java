package github.jodevnull.minepkl.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import github.jodevnull.minepkl.Minepkl;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;
import org.pkl.core.FileOutput;

import java.util.function.Consumer;

public class DynServerResources
{
    public static void init() {
        ServerAssetsGenerator generator = new ServerAssetsGenerator();
        generator.register();
    }

    public static class ServerAssetsGenerator extends DynServerResourcesGenerator
    {
        protected ServerAssetsGenerator() {
            super(new DynamicDataPack(Minepkl.res("generated_data"), Pack.Position.TOP, false, false));
        }

        @Override
        public Logger getLogger() { return Minepkl.LOGGER; }

        @Override
        public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor)
        {
            ExternalResources.generateExternalFiles();
            executor.accept(ServerAssetsGenerator::genPklData);
        }

        private static void genPklData(ResourceManager resourceManager, ResourceSink sink)
        {
            for (var entry : Minepkl.getData().entrySet()) {
                String path = entry.getKey();
                String contents = entry.getValue();

                if (!ResourceLocation.isValidResourceLocation(path)) {
                    Minepkl.LOGGER.error("Invalid location: '{}' -- Skipping...", path);
                    continue;
                }

                ResourceLocation location = new ResourceLocation(path);
                JsonElement output = JsonParser.parseString(contents);
                sink.addJson(location, output, ResType.JSON);
                Minepkl.LOGGER.info("[Pkl:data] generated '{}'", location);
            }
        }
    }
}
