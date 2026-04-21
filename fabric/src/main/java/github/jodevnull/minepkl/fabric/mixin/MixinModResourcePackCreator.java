package github.jodevnull.minepkl.fabric.mixin;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.fabric.MinepklFabric;
import github.jodevnull.minepkl.pack.MinepklPackRepository;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ModResourcePackCreator.class)
public class MixinModResourcePackCreator
{
    @Unique
    private MinepklPackRepository newSource;

    @Inject(method = "<init>(Lnet/minecraft/server/packs/PackType;)V", at = @At("TAIL"))
    private void onConstruction(PackType type, CallbackInfo callback) {
        if (type == PackType.SERVER_DATA)
            this.newSource = Minepkl.serverRepository(MinepklFabric.platform.getGameDir());

        else if (type == PackType.CLIENT_RESOURCES)
            this.newSource = Minepkl.clientRepository(MinepklFabric.platform.getGameDir());
    }

    @Inject(method = "loadPacks(Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void loadPacks(Consumer<Pack> consumer, CallbackInfo callback) {
        if (this.newSource == null)
            return;

        this.newSource.loadPacks(consumer);
    }
}