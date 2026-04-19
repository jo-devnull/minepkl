package github.jodevnull.minepkl.forge.mixin;

import github.jodevnull.minepkl.core.pack.MinepklPackRepository;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.loading.FMLPaths;
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
            new MinepklPackRepository(PackType.CLIENT_RESOURCES, FMLPaths.GAMEDIR.get()),
            new MinepklPackRepository(PackType.SERVER_DATA, FMLPaths.GAMEDIR.get())
        );
    }
}
