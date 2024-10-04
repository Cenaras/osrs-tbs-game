package hotscape.standard;

import hotscape.framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TestStandardGame {

    private Game game;
    private final Position zero = new Position(0, 0);
    private final Position one = new Position(1, 1);

    @BeforeEach
    public void setup() {
        this.game = new StandardGame();
    }

    @Test
    public void shouldStartInPlayState() {
        assertThat(game.getGameState(), is(GameState.PLAY_STATE));
    }

    @Test
    public void shouldBecomeBattleStateAfterBothPlayersEndPlayState() {
        endShopPhase();

        Status saraEndStatus = game.endPlayPhase(Player.SARADOMIN);
        Status zammyEndStatus = game.endPlayPhase(Player.ZAMORAK);

        assertThat(saraEndStatus, is(Status.OK));
        assertThat(zammyEndStatus, is(Status.OK));

        GameState gameState = game.getGameState();
        assertThat(gameState, is(GameState.BATTLE_PHASE));
    }

    @Test
    public void shouldGive_PLAYER_NOT_IN_PLAY_STATE_WhenEndingTryingToEndPlayPhaseInBattlePhase() {
        endShopPhase();

        game.endPlayPhase(Player.SARADOMIN);
        game.endPlayPhase(Player.ZAMORAK);

        // Game is now in BATTLE PHASE, and calling endPlayPhase should give error
        Status status = game.endPlayPhase(Player.SARADOMIN);
        assertThat(status, is(Status.PLAYER_NOT_IN_PLAY_STATE));
    }

    @Test
    public void saraStartsAsWiseOldMan() {
        assertThat(game.getHero(Player.SARADOMIN).getType(), is(GameConstants.WISE_OLD_MAN_HERO));
    }

    @Test
    public void zammyStartsAsGnomeChild() {
        assertThat(game.getHero(Player.ZAMORAK).getType(), is(GameConstants.GNOME_CHILD_HERO));
    }

    @Test
    public void playingGoblingAndMovingItPlacesItInNewPosition() {
        game.endShopPhase(Player.SARADOMIN);

        Unit goblin = game.getItemInInventory(Player.SARADOMIN, 0);
        game.playUnit(Player.SARADOMIN, goblin, zero);
        Status status = game.moveUnit(Player.SARADOMIN, zero, one);
        assertThat(status, is(Status.OK));
        assertThat(game.getUnitAt(one).getType(), is(GameConstants.GOBLIN_UNIT));
    }

    @Test
    public void movingEmptyPositionGivesNoUnitAtPosError() {
        game.endShopPhase(Player.SARADOMIN);
        Status status = game.moveUnit(Player.SARADOMIN, zero, one);
        assertThat(status, is(Status.NO_UNIT_AT_POSITION));
    }

    @Test
    public void movingUnitOutsidePlayPhaseGivesError() {
        Unit goblin = game.getItemInInventory(Player.SARADOMIN, 0);
        game.playUnit(Player.SARADOMIN, goblin, zero);
        Status status = game.moveUnit(Player.SARADOMIN, zero, one);
        assertThat(status, is(Status.PLAYER_NOT_IN_PLAY_STATE));
    }

    @Test
    public void playersOwnTheirHero() {
        assertThat(game.getHero(Player.SARADOMIN).getOwner(), is(Player.SARADOMIN));
        assertThat(game.getHero(Player.ZAMORAK).getOwner(), is(Player.ZAMORAK));
    }

    @Test
    public void shouldHave_MAX_HEALTH_AtStart() {
        Hero saraHero = game.getHero(Player.SARADOMIN);
        assertThat(saraHero.getHealth(), is(GameConstants.MAX_HEALTH));

        Hero zammyHero = game.getHero(Player.ZAMORAK);
        assertThat(zammyHero.getHealth(), is(GameConstants.MAX_HEALTH));
    }

    @Test
    public void shouldHaveEmptyBoardAtStart() {
        // TODO: test for increasing field size
        int saraFieldSize = game.getUnits(Player.SARADOMIN).size();
        assertThat(saraFieldSize, is(0));
    }

    @Test
    public void boardInitiallyEmpty() {
        for (int row = 0; row < GameConstants.MAX_ROW; row++) {
            for (int col = 0; col < GameConstants.MAX_COL; col++) {
                var pos = new Position(row, col);
                assertThat(game.getUnitAt(pos), is(nullValue()));
            }
        }
    }

    @Test
    public void bothPlayersStartWithGoblinInInventoryAtIndex0() {
        List<Unit> saraInventory = game.getInventory(Player.SARADOMIN);
        List<Unit> zammyInventory = game.getInventory(Player.ZAMORAK);

        assertThat(saraInventory.size(), is(1));
        assertThat(zammyInventory.size(), is(1));

        assertThat(game.getItemInInventory(Player.SARADOMIN, 0).getType(), is(GameConstants.GOBLIN_UNIT));
        assertThat(game.getItemInInventory(Player.ZAMORAK, 0).getType(), is(GameConstants.GOBLIN_UNIT));
    }

    @Test
    public void goblinCosts1Has5HealthActionSpeed4() {
        Unit goblin = game.getItemInInventory(Player.SARADOMIN, 0);
        assertThat(goblin.getType(), is(GameConstants.GOBLIN_UNIT));

        // Assert specification of goblin // TODO attack stats
        assertThat(goblin.getCost(), is(1));
        assertThat(goblin.getHealth(), is(5));
        assertThat(goblin.getActionSpeed(), is(4));
    }

    @Test
    public void shouldHave3GPForEachHeroAtStart() {
        assertThat(game.getHero(Player.SARADOMIN).getGP(), is(3));
        assertThat(game.getHero(Player.ZAMORAK).getGP(), is(3));
    }

    @Test
    public void shouldHaveInitialDeckOfSize6() {
        assertThat(game.getDeck(Player.SARADOMIN).size(), is(6));
        assertThat(game.getDeck(Player.ZAMORAK).size(), is(6));
    }


    @Test
    public void cannotPlayUnitInBattlePhase() {

        endShopPhase();

        game.endPlayPhase(Player.ZAMORAK);
        game.endPlayPhase(Player.SARADOMIN);

        Status status = game.playUnit(Player.SARADOMIN, game.getItemInInventory(Player.SARADOMIN, 0), new Position(1, 1));
        assertThat(status, is(Status.NOT_PLAY_PHASE));
    }

    // TODO: NOT_ENOUGH_GP_TEST
    // TODO: Test covering all possible Status codes
    // TODO: Test ALREADY_OCCUPIED


    @Test
    public void shouldHaveGoblinOn1x1WhenPlaying() {
        Unit goblin = game.getItemInInventory(Player.SARADOMIN, 0);
        Position pos = new Position(1, 1);
        game.playUnit(Player.SARADOMIN, goblin, pos);

        assertThat(game.getUnitAt(pos).getType(), is(GameConstants.GOBLIN_UNIT));
        assertThat(game.getUnitAt(pos).getOwner(), is(Player.SARADOMIN));
    }


    @Test
    public void shouldBothHaveAGoblinWhenPlaying() {
        game.playUnit(Player.SARADOMIN, game.getItemInInventory(Player.SARADOMIN, 0), new Position(1, 1));
        game.playUnit(Player.ZAMORAK, game.getItemInInventory(Player.ZAMORAK, 0), new Position(7, 7));

        assertThat(game.getUnits(Player.SARADOMIN).size(), is(1));
        assertThat(game.getUnits(Player.ZAMORAK).size(), is(1));
    }

    @Test
    public void shouldStartInShopPhase() {
        assertThat(game.getPlayerState(Player.SARADOMIN), is(PlayerState.SHOP));
        assertThat(game.getPlayerState(Player.ZAMORAK), is(PlayerState.SHOP));
    }

    @Test
    public void endingShopPhaseTransitionsPlayerToPlayPhase() {
        Status status = game.endShopPhase(Player.SARADOMIN);
        assertThat(status, is(Status.OK));

        assertThat(game.getPlayerState(Player.SARADOMIN), is(PlayerState.PLAY));
        assertThat(game.getPlayerState(Player.ZAMORAK), is(PlayerState.SHOP));

        game.endShopPhase(Player.ZAMORAK);
        assertThat(game.getPlayerState(Player.ZAMORAK), is(PlayerState.PLAY));
    }

    @Test
    public void cannotEndPlayPhaseWhileNotInPlayState() {
        Status status = game.endPlayPhase(Player.SARADOMIN);
        assertThat(status, is(Status.PLAYER_NOT_IN_PLAY_STATE));

        game.endShopPhase(Player.SARADOMIN);
        Status success = game.endPlayPhase(Player.SARADOMIN);
        assertThat(success, is(Status.OK));
    }

    @Test
    public void bothPlayersEndingShopAndPlayPhaseTurnsGameToBattlePhase() {
        game.endShopPhase(Player.SARADOMIN);
        game.endShopPhase(Player.ZAMORAK);

        Status endSaraPlay = game.endPlayPhase(Player.SARADOMIN);
        assertThat(endSaraPlay, is(Status.OK));
        assertThat(game.getGameState(), is(GameState.PLAY_STATE));

        Status endZammyPlay = game.endPlayPhase(Player.ZAMORAK);
        assertThat(endZammyPlay, is(Status.OK));

        assertThat(game.getGameState(), is(GameState.BATTLE_PHASE));
    }


    private void endShopPhase() {
        game.endShopPhase(Player.SARADOMIN);
        game.endShopPhase(Player.ZAMORAK);
    }



}
