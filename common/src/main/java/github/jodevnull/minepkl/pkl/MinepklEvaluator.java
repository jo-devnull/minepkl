package github.jodevnull.minepkl.pkl;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Options;
import org.pkl.core.*;
import org.pkl.core.http.HttpClient;
import org.pkl.core.module.ModuleKeyFactories;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

import static github.jodevnull.minepkl.Minepkl.*;

public class MinepklEvaluator
{
    private static Exception ERROR = null;

    public static Exception popError() {
        Exception err = ERROR;
        ERROR = null;
        return err;
    }

    public static void pushError(Exception val) {
        ERROR = val;
    }

    public static boolean hasError() {
        return ERROR != null;
    }

    public static final List<Pattern> allowedModules = List.of(
        Pattern.compile("minepkl:"),
        Pattern.compile("file:"),
        Pattern.compile("pkl:")
    );

    public static final List<Pattern> allowedResources = List.of(
        Pattern.compile("instance:")
    );

    public static void init() {}

    private static Evaluator buildEvaluator() {
        var builder = EvaluatorBuilder
            .unconfigured()
            // This is to prevent pkl scripts to download external files or connect to the internet in any way
            .setHttpClient(HttpClient.dummyClient())
            .setStackFrameTransformer(StackFrameTransformers.defaultTransformer)
            .setAllowedModules(allowedModules)
            .setAllowedResources(allowedResources)
            .addResourceReader(InstanceResourceReader.INSTANCE)
            .addModuleKeyFactory(MinepklModuleFile.INSTANCE)
            .addModuleKeyFactory(ModuleKeyFactories.standardLibrary)
            .addModuleKeyFactory(ModuleKeyFactories.file)
            // Since minecraft expects json files for
            .setOutputFormat(OutputFormat.JSON);

        if (Options.getUseRootDir())
            builder.setRootDir(Minepkl.PLATFORM.getGameDir());

        return builder.build();
    }

    public static void onError(Exception e, Path module) {
        LOGGER.error("Exception while running '{}' (No files generated)", getRelative(module));
        LOGGER.error(e);
        pushError(e);
    }

    public static Map<String, String> buildExternal() {
        Path module = Options.getExternalPath();
        HashMap<String, String> output = new HashMap<>();

        try (Evaluator evaluator = buildEvaluator()) {
            ModuleSource source = ModuleSource.path(Options.getExternalPath());

            for (var entry : evaluator.evaluateOutputFiles(source).entrySet()) {
                String path = entry.getKey();
                output.put(entry.getKey(), entry.getValue().getText());
                LOGGER.info("[pkl] generating external file '{}'", path);
            }
        } catch (Exception e) {
            onError(e, module);
        }

        return output;
    }

    public static HashMap<String, String> buildPack(Path modulePath) {
        HashMap<String, String> output = new HashMap<>();

        try (Evaluator evaluator = buildEvaluator()) {
            ModuleSource source = ModuleSource.uri("minepkl:@generator");
            Map<String, FileOutput> files = evaluator.evaluateOutputFiles(source);

            for (var entry : files.entrySet()) {
                output.put(entry.getKey(), entry.getValue().getText());
                LOGGER.info("[pkl] generating file '{}'", entry.getKey());
            }
        } catch (Exception e) {
            onError(e, modulePath);
        }

        return output;
    }
}
