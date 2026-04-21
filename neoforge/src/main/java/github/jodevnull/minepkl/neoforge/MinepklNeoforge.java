package github.jodevnull.minepkl.neoforge;

import github.jodevnull.minepkl.Minepkl;
import github.jodevnull.minepkl.Platform;
import github.jodevnull.minepkl.cmd.MinepklCommands;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Minepkl.MOD_ID)
public final class MinepklNeoforge
{
    static final Platform platform = new PlatformNeoforge();

    static {
        Minepkl.setPlatform(platform);
    }

    public MinepklNeoforge(IEventBus modBus) {
        Minepkl.init();
        modBus.addListener(this::addPackRepository);
        NeoForge.EVENT_BUS.addListener(this::registerCommand);
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
