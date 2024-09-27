package hotscape.standard;

import hotscape.framework.Player;
import hotscape.framework.Unit;

public class StandardUnit implements Unit {
    private final Player owner;
    private final UnitSpec spec;

    public StandardUnit(Player owner, UnitSpec spec) {
        this.owner = owner;
        this.spec = spec;
    }

    @Override
    public int getActionSpeed() {
        return this.spec.actionSpeed();
    }

    @Override
    public int getHealth() {
        return this.spec.health();
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public int getCost() {
        return this.spec.cost();
    }

    @Override
    public String getType() {
        return this.spec.type();
    }
}
