package github.jodevnull.minepkl;

import github.jodevnull.minepkl.pack.MinepklPackRepository;
import github.jodevnull.minepkl.pack.PackGenerator;
import github.jodevnull.minepkl.pkl.MinepklEvaluator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class Minepkl
{
    public static final String MOD_ID = "minepkl";
    public static final Logger LOGGER = LogManager.getLogger();

    private static Optional<MinepklPackRepository> SERVER_REPOSITORY = Optional.empty();
    private static Optional<MinepklPackRepository> CLIENT_REPOSITORY = Optional.empty();

    public static Platform PLATFORM;

    public static void init() {
        MinepklEvaluator.init();
        Minepkl.writeDefaultFiles();
        Options.load();
        PackGenerator.generate();
    }

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID + ":" + path);
    }

    public static void setPlatform(Platform platform) {
        PLATFORM = platform;
    }

    public static Platform getPlatform() {
        return PLATFORM;
    }

    public static MinepklPackRepository serverRepository(Path gameDir) {
        if (SERVER_REPOSITORY.isEmpty()) {
            SERVER_REPOSITORY = Optional.of(new MinepklPackRepository(PackType.SERVER_DATA, gameDir));
        }

        return SERVER_REPOSITORY.orElseThrow();
    }

    public static MinepklPackRepository clientRepository(Path gameDir) {
        if (CLIENT_REPOSITORY.isEmpty()) {
            CLIENT_REPOSITORY = Optional.of(new MinepklPackRepository(PackType.CLIENT_RESOURCES, gameDir));
        }

        return CLIENT_REPOSITORY.orElseThrow();
    }

    public static void writeDefaultFiles() {
        try {
            Files.createDirectories(Options.getMainDir());
            Files.createDirectories(Path.of(Options.getConfigDir() + "/generated"));
        } catch (IOException e) {
            LOGGER.error("Failed to create '{}/' directory", Options.MAIN_DIR);
            return;
        }

        writeSourceFile("/minepkl/build.pkl", Options.getBuildFilePath());
        writeSourceFile("/minepkl/external.pkl", Options.getExternalPath());
        writeSourceFile("/minepkl/generator.pkl", Options.getGeneratorFilePath());
    }

    public static void writeSourceFile(String source, Path output) {
        if (new File(output.toUri()).exists()) {
            LOGGER.info("'{}' already exists, skiping...", getRelative(output));
            return;
        }

        getSourceFile(source).ifPresent(file -> {
            try {
                Files.write(output, file.getBytes());
            } catch (IOException e) {
                LOGGER.error("Error writing default pkl files:");
                LOGGER.error(e);
            }
        });
    }

    public static Optional<String> getSourceFile(String path) {
        try (InputStream stream = Minepkl.class.getResourceAsStream(path)) {
            if (stream == null) {
                LOGGER.error("InputStream for '{}' is null! This isn't suppose to happen!", path);
                return Optional.empty();
            }

            return Optional.of(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("Error generating default {} file:", path);
            LOGGER.error(e);
        }

        return Optional.empty();
    }

    public static String getRelative(Path absolutePath) {
        return absolutePath.toString().replace(PLATFORM.getGameDir().toString(), "");
    }

    public static void setErrorAndLog(String msg, Object ...arguments) {
        String errMessage = msg.formatted(arguments);
        MinepklEvaluator.pushError(new Exception(errMessage));
        LOGGER.error(errMessage);
    }

    public static void logError(ServerPlayer player, String msg, Object ...arguments)  {
        if (player != null) {
            MutableComponent component = Component.literal(msg.formatted(arguments))
                .withStyle(style -> style.withColor(ChatFormatting.RED));

            player.sendSystemMessage(component);
        }
    }
}
