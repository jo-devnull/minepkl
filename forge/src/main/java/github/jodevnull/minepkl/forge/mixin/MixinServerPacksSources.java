package github.jodevnull.minepkl.forge.mixin;

import github.jodevnull.minepkl.pack.MinepklPackRepository;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPacksSource.class)
public class MixinServerPacksSources
{
    @ModifyArg(
        method = "createPackRepository(Ljava/nio/file/Path;)Lnet/minecraft/server/packs/repository/PackRepository;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V")
    )
    private static RepositorySource[] minepkl$injectClientPacks(RepositorySource[] args) {
        return ArrayUtils.add(args, new MinepklPackRepository(PackType.SERVER_DATA, FMLPaths.GAMEDIR.get()));
    }
}
