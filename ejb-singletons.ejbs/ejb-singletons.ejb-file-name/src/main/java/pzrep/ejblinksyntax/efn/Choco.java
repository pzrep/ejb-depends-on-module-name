package pzrep.ejblinksyntax.efn;

import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pzrep.ejblinksyntax.api.Cat;

@Singleton
@Startup
@DependsOn({
        "pzrep.ejb-depends-on-module-name-ejb-singletons.ejb-base-1.0-SNAPSHOT.jar#Ant"
})
public class Choco implements Cat {
    @Override
    public String color() {
        return "brown (file-name#)";
    }
}
