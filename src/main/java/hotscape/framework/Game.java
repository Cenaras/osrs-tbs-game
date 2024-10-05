package hotscape.framework;

import hotscape.standard.PlayerState;

import java.util.List;

public interface Game {
    /**
     * Returns the winner of the game, or Player.NONE if game is not won yet.
     *
     * @return winner of the game or Player.NONE if game is not won yet.
     */
    Player getWinner();

    /**
     * Returns the hero of a given player.
     *
     * @param who the player owning the hero
     * @return the hero owned by the player
     */
    Hero getHero(Player who);

    /**
     * Gets the units of a player.
     *
     * @param who the player
     * @return units the player has played.
     */
    List<Unit> getUnits(Player who);

    Unit getUnitAt(Position position);


    // TODO: More than just units -- items later on, maybe a type for this? And play method then takes this type...
    List<Unit> getInventory(Player who);

    Unit getItemInInventory(Player who, int index);

    /**
     * Plays a unit onto the field.
     * PRECONDITION: unit is retrieved by getItemInInventory
     *
     * @param player the player performing the play action
     * @param unit   the unit to play
     * @return Status of operation
     */
    // TODO: Position on field?
    Status playUnit(Player player, Unit unit, Position position);

    Status moveUnit(Player player, Position oldPosition, Position newPosition);

    /**
     * Use the hero power during the PLAY phase
     */
    Status usePower();

    Status endShopPhase(Player who);

    /**
     * Signals that a player is ready to end their play phase.
     *
     * @param who the player who wishes to end their play phase
     */
    Status endPlayPhase(Player who);

    Status performBattlePhase();

    /**
     * Returns the current state of the game.
     *
     * @return the current state of the game
     */
    GameState getGameState();


    List<Unit> getDeck(Player player);

    PlayerState getPlayerState(Player who);
}
