package pzrep.ejblinksyntax.ebn;

import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pzrep.ejblinksyntax.api.Cat;

@Singleton
@Startup
@DependsOn({
        "Base/Ant"
})
public class Choco implements Cat {
    @Override
    public String color() {
        return "brown (module-name/)";
    }
}
