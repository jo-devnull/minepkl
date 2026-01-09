package github.jodevnull.minepkl.mixin;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Options;
import github.jodevnull.minepkl.core.PklEvaluator;
import github.jodevnull.minepkl.core.resources.ExternalResources;
import github.jodevnull.minepkl.core.resources.PackGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ReloadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class MixinReloadCommand
{
    @Inject(at = @At("HEAD"), method = "reloadPacks")
    private static void minepkl$reloadPacks(Collection<String> collection, CommandSourceStack commandSourceStack, CallbackInfo ci) {
        Minepkl.LOGGER.info("Generating minepkl resources...");
        Options.load();
        PackGenerator.generatePack();
        ExternalResources.generateExternalFiles();

        if (PklEvaluator.hasError()) {
            Minepkl.logError(commandSourceStack.getPlayer(), "[pkl] Error generating files:");
            Minepkl.logError(commandSourceStack.getPlayer(), "%s", PklEvaluator.popError());
        }
    }
}
