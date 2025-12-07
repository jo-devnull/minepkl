package github.jodevnull.minepkl.forge;

import github.jodevnull.minepkl.Minepkl;
import net.minecraftforge.fml.common.Mod;

@Mod(Minepkl.MOD_ID)
public final class MinepklForge {
    public MinepklForge() {
        // Run our common setup.
        Minepkl.init();
    }
}
