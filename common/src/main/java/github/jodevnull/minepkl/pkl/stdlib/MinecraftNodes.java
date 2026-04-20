package github.jodevnull.minepkl.pkl.stdlib;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import github.jodevnull.minepkl.Minepkl;
import org.pkl.core.runtime.VmList;
import org.pkl.core.runtime.VmTyped;
import org.pkl.core.stdlib.ExternalMethod0Node;
import org.pkl.core.stdlib.ExternalMethod1Node;

@SuppressWarnings("unused")
public final class MinecraftNodes
{
    private MinecraftNodes() {}

    public abstract static class isModLoaded extends ExternalMethod1Node
    {
        @Specialization
        protected boolean eval(VmTyped self, String modId) {
            return Minepkl.getPlatform().isModLoaded(modId);
        }
    }

    public abstract static class getModList extends ExternalMethod0Node
    {
        @TruffleBoundary
        @Specialization
        protected VmList eval(VmTyped self) {
            return VmList.create(Minepkl.getPlatform().getModList());
        }
    }
}
