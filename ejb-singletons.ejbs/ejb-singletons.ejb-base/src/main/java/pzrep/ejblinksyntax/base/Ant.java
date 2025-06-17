package pzrep.ejblinksyntax.base;

import jakarta.ejb.Singleton;
import pzrep.ejblinksyntax.api.Creature;

@Singleton
public class Ant implements Creature {
    @Override
    public String name() {
        return "Z";
    }
}
