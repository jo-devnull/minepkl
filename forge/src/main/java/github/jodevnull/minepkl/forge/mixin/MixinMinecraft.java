package github.jodevnull.minepkl.forge.mixin;

import github.jodevnull.minepkl.pack.MinepklPackRepository;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @ModifyArg(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/repository/PackRepository;<init>([Lnet/minecraft/server/packs/repository/RepositorySource;)V"),
        index = 0
    )
    private RepositorySource[] minepkl$injectClientPacks(RepositorySource[] args) {
        return ArrayUtils.add(args, new MinepklPackRepository(PackType.CLIENT_RESOURCES, FMLPaths.GAMEDIR.get()));
    }
}
