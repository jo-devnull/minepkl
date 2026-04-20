package github.jodevnull.minepkl.fabric.mixin;

import github.jodevnull.minepkl.pack.MinepklPackRepository;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CreateWorldScreen.class)
public class MixinCreateWorldScreen
{
    @ModifyArg(
            method = "openFresh",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V")
    )
    private static RepositorySource[] minepkl$injectClientPacks(RepositorySource[] args) {
        return ArrayUtils.addAll(args,
            new MinepklPackRepository(PackType.CLIENT_RESOURCES, FabricLoader.getInstance().getGameDir()),
            new MinepklPackRepository(PackType.SERVER_DATA, FabricLoader.getInstance().getGameDir())
        );
    }
}
