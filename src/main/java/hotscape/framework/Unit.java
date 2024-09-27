package hotscape.framework;

public interface Unit {

    /**
     * Returns the action speed of the given unit.
     *
     * @return action speed of the unit
     */
    int getActionSpeed();

    /**
     * Returns the health of the unit.
     *
     * @return health of the unit
     */
    int getHealth();

    /**
     * Returns the owner of the unit.
     *
     * @return owner of the unit.
     */
    Player getOwner();

    /**
     * Returns the cost of the unit.
     *
     * @return cost of the unit.
     */
    int getCost();

    String getType();

    // TODO: action description? Will we allow several? Do we allow items on units? Buffs?

}
