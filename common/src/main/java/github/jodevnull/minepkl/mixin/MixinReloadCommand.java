package github.jodevnull.minepkl.mixin;

import github.jodevnull.minepkl.Minepkl;
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
        PackGenerator.generatePack();
        ExternalResources.generateExternalFiles();
    }
}
