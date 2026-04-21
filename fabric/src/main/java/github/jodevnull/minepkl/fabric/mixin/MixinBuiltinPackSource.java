package github.jodevnull.minepkl.fabric.mixin;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.fabric.MinepklFabric;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
public class MixinBuiltinPackSource
{
    @Shadow
    @Final
    private PackType packType;

    @Inject(method = "loadPacks", at = @At("TAIL"))
    private void loadPacks(Consumer<Pack> consumer, CallbackInfo ci) {
        final BuiltInPackSource self = (BuiltInPackSource) (Object) this;

        if (packType == PackType.SERVER_DATA && self instanceof ServerPacksSource) {
            final Path gameDir = MinepklFabric.platform.getGameDir();
            Minepkl.serverRepository(gameDir).loadPacks(consumer);
        }
    }
}
