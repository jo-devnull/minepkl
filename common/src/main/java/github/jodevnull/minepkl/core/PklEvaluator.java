package github.jodevnull.minepkl.core;

import github.jodevnull.minepkl.Options;
import github.jodevnull.minepkl.core.resources.InstanceResourceReader;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.resources.ResourceLocation;
import org.pkl.core.*;
import org.pkl.core.module.ModuleKeyFactories;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static github.jodevnull.minepkl.Minepkl.LOGGER;
import static github.jodevnull.minepkl.Minepkl.getRelative;

public class PklEvaluator
{
    public static final List<Pattern> allowedModules = List.of(
        Pattern.compile("file:"),
        Pattern.compile("https:"),
        Pattern.compile("pkl:"),
        Pattern.compile("package:"),
        Pattern.compile("projectpackage:")
    );

    public static final List<Pattern> allowedResources = List.of(
        Pattern.compile("instance:")
    );

    public static void init() {}

    private static Evaluator buildEvaluator() {
        var builder = EvaluatorBuilder
            .unconfigured()
            .setStackFrameTransformer(StackFrameTransformers.defaultTransformer)
            .setAllowedModules(allowedModules)
            .setAllowedResources(allowedResources)
            .addResourceReader(InstanceResourceReader.INSTANCE)
            .addModuleKeyFactory(ModuleKeyFactories.standardLibrary)
            .addModuleKeyFactory(ModuleKeyFactories.file)
            .addModuleKeyFactory(ModuleKeyFactories.http)
            .addModuleKeyFactory(ModuleKeyFactories.pkg)
            .addModuleKeyFactory(ModuleKeyFactories.projectpackage)
            .addModuleKeyFactory(ModuleKeyFactories.genericUrl)
            // Since minecraft expects json files for
            .setOutputFormat(OutputFormat.JSON);

        if (Options.getUseRootDir())
            builder.setRootDir(PlatHelper.getGamePath());

        return builder.build();
    }

    public static Map<String, String> getAssets() {
        return getFileOutputs(Options.getAssetsPath(), false, "assets");
    }

    public static Map<String, String> getData() {
        return getFileOutputs(Options.getDataPath(), false, "data");
    }

    public static Map<String, String> getExternal() {
        return getFileOutputs(Options.getExternalPath(), true, "");
    }

    public static Map<String, String> getFileOutputs(Path module, boolean isExternal, String type) {
        HashMap<String, String> output = new HashMap<>();

        try (Evaluator evaluator = buildEvaluator()) {
            ModuleSource source = ModuleSource.file(module.toString());
            for (var entry : evaluator.evaluateOutputFiles(source).entrySet()) {
                String path = entry.getKey();

                if (isExternal) {
                    output.put(entry.getKey(), entry.getValue().getText());
                } else {
                    if (!ResourceLocation.isValidResourceLocation(path)) {
                        LOGGER.error("Invalid resource location: {}", path);
                        continue;
                    }

                    ResourceLocation location = new ResourceLocation(path);
                    String finalPath = "%s/%s/%s.json".formatted(type, location.getNamespace(), location.getPath());
                    output.put(finalPath, entry.getValue().getText());
                }

                LOGGER.info("[pkl:{}] generating file '{}'", type, path);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while running '{}' (No files generated)", getRelative(module));
            LOGGER.error(e);
        }

        return output;
    }
}
