package hotscape.standard;

import hotscape.framework.GameConstants;
import hotscape.framework.Hero;
import hotscape.framework.Player;

public class StandardHero implements Hero {

    private final int health;
    private final Player owner;
    private final String type;
    private int gp = 3;


    public StandardHero(Player owner, String type) {
        this.owner = owner;
        this.type = type;
        this.health = GameConstants.MAX_HEALTH;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean canUsePower() {
        return false;
    }

    @Override
    public String getPowerDescription() {
        return "";
    }

    @Override
    public int getGP() {
        return this.gp;
    }
}
