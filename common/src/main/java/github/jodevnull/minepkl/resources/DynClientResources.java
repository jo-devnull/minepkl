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

public class DynClientResources
{
    public static void init() {
        ClientAssetsGenerator generator = new ClientAssetsGenerator();
        generator.register();
    }

    public static class ClientAssetsGenerator extends DynClientResourcesGenerator
    {
        protected ClientAssetsGenerator() {
            super(new DynamicTexturePack(Minepkl.res("generated_assets"), Pack.Position.TOP, false, false));
        }

        @Override
        public Logger getLogger() { return Minepkl.LOGGER; }

        @Override
        public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor)
        {
            executor.accept(ClientAssetsGenerator::genPklData);
        }

        private static void genPklData(ResourceManager resourceManager, ResourceSink sink)
        {
            for (var entry : Minepkl.getAssets().entrySet()) {
                String path = entry.getKey();
                String contents = entry.getValue();

                if (!ResourceLocation.isValidResourceLocation(path)) {
                    Minepkl.LOGGER.error("Invalid location: '{}' -- Skipping...", path);
                    continue;
                }

                ResourceLocation location = new ResourceLocation(path);
                JsonElement output = JsonParser.parseString(contents);
                sink.addJson(location, output, ResType.JSON);
                Minepkl.LOGGER.info("[Pkl:asset] generated '{}'", location);
            }
        }
    }
}
