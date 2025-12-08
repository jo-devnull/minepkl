package github.jodevnull.minepkl.core;

import github.jodevnull.minepkl.Options;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
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
        return EvaluatorBuilder
            .unconfigured()
            .setStackFrameTransformer(StackFrameTransformers.defaultTransformer)
            .setRootDir(PlatHelper.getGamePath())
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
            .setOutputFormat(OutputFormat.JSON)
            .build();
    }

    public static Map<String, String> getAssets() {
        return getFileOutputs(Options.getAssetsPath());
    }

    public static Map<String, String> getData() {
        return getFileOutputs(Options.getDataPath());
    }

    public static Map<String, String> getExternal() {
        return getFileOutputs(Options.getExternalPath());
    }

    public static Map<String, String> getFileOutputs(Path module) {
        HashMap<String, String> output = new HashMap<>();

        try (Evaluator evaluator = buildEvaluator()) {
            ModuleSource source = ModuleSource.file(module.toString());
            for (var entry : evaluator.evaluateOutputFiles(source).entrySet()) {
                output.put(entry.getKey(), entry.getValue().getText());
            }
        } catch (Exception e) {
            LOGGER.error("Exception while running '{}' (No files generated)", getRelative(module));
            LOGGER.error(e);
        }

        return output;
    }
}
