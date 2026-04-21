package github.jodevnull.minepkl.forge;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Platform;
import github.jodevnull.minepkl.cmd.MinepklCommands;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Minepkl.MOD_ID)
public final class MinepklForge
{
    static final Platform platform = new PlatformForge();

    static {
        Minepkl.setPlatform(platform);
    }

    public MinepklForge() {
        // Run our common setup.
        Minepkl.init();
        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addPackRepository);
    }

    private void registerCommand(RegisterCommandsEvent event) {
        MinepklCommands.createCommand(event.getDispatcher());
    }

    private void addPackRepository(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            event.addRepositorySource(Minepkl.clientRepository(platform.getGameDir()));
        }

        if (event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource(Minepkl.serverRepository(platform.getGameDir()));
        }
    }
}
