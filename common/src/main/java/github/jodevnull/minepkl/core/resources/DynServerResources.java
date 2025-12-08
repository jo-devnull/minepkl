package github.jodevnull.minepkl.core.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.core.PklEvaluator;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

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
            for (var entry : PklEvaluator.getData().entrySet()) {
                String path = entry.getKey();

                if (!ResourceLocation.isValidResourceLocation(path)) {
                    Minepkl.LOGGER.error("Invalid location: '{}' -- Skipping...", path);
                    continue;
                }

                try {
                    String contents = entry.getValue();
                    ResourceLocation location = new ResourceLocation(path);
                    JsonElement output = JsonParser.parseString(contents);
                    sink.addJson(location, output, ResType.JSON);
                    Minepkl.LOGGER.info("[Pkl:data] generated '{}'", location);
                } catch (JsonSyntaxException e) {
                    Minepkl.LOGGER.error("Error generating resource: {}", path);
                    Minepkl.LOGGER.error("Output format is not valid JSON. Did you forgot to set the output to JSON?");
                }
            }
        }
    }
}
