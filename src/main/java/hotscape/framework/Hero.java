package hotscape.framework;

public interface Hero {

    /**
     * Returns the health of the hero.
     *
     * @return health of the hero.
     */
    int getHealth();

    /**
     * Returns the owner of the hero
     *
     * @return the owner of the hero
     */
    Player getOwner();

    /**
     * Returns the type of the hero.
     *
     * @return the type of the hero
     */
    String getType();

    /**
     * Checks if a hero can apply their power or not.
     *
     * @return true of power can be used, false if not.
     */
    boolean canUsePower();

    /**
     * Returns the description of the hero power.
     *
     * @return description of the hero power
     */
    String getPowerDescription();

    /**
     * Returns the GP held by the Hero.
     *
     * @return GP held by the hero.
     */
    int getGP();

}
