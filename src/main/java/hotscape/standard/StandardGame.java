package hotscape.standard;

import hotscape.framework.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  TODO: Make a "Simple Initial Hand" to make production easier, worry about balance later on...
// Things in inventory are already bought, and should therefore not cost GP
// otherwise the "deck/shop" allows us to purchase from and into inventory

public class StandardGame implements Game {

    private final Map<Player, StandardHero> heroMap;
    private final Map<Player, PlayerState> playerStateMap;
    private final Map<Player, List<Unit>> inventoryMap;

    private final Map<Player, List<Unit>> deckMap;

    private final Unit[][] board = new Unit[GameConstants.MAX_ROW][GameConstants.MAX_COL];



    private GameState gameState;


    public StandardGame() {
        this.heroMap = new HashMap<>(Map.of(
                Player.SARADOMIN, new StandardHero(Player.SARADOMIN, GameConstants.WISE_OLD_MAN_HERO),
                Player.ZAMORAK, new StandardHero(Player.ZAMORAK, GameConstants.GNOME_CHILD_HERO)
        ));

        this.playerStateMap = new HashMap<>(Map.of(
                Player.SARADOMIN, PlayerState.SHOP,
                Player.ZAMORAK, PlayerState.SHOP
        ));

        this.inventoryMap = new HashMap<>(Map.of(
                Player.SARADOMIN, List.of(new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4))),
                Player.ZAMORAK, List.of(new StandardUnit(Player.ZAMORAK, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)))
        ));

        this.deckMap = new HashMap<>(Map.of(
                Player.SARADOMIN, List.of(
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.RAT_UNIT, 1, 3, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.RAT_UNIT, 1, 3, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.MAN_UNIT, 2, 7, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.MAN_UNIT, 2, 7, 4))),
                Player.ZAMORAK, List.of(
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.RAT_UNIT, 1, 3, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.RAT_UNIT, 1, 3, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.MAN_UNIT, 2, 7, 4)),
                        new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.MAN_UNIT, 2, 7, 4)))
        ));

        gameState = GameState.PLAY_STATE;
    }


    @Override
    public Player getWinner() {
        return null;
    }

    @Override
    public Hero getHero(Player who) {
        return heroMap.get(who);
    }

    @Override
    public List<Unit> getUnits(Player who) {
        ArrayList<Unit> units = new ArrayList<>();
        for (int row = 0; row < GameConstants.MAX_ROW; row++) {
            for (int col = 0; col < GameConstants.MAX_COL; col++) {
                Unit unit = getUnitAt(new Position(row, col));
                boolean isOwner = unit != null && unit.getOwner() == who;

                if (isOwner) {
                    units.add(unit);
                }
            }
        }
        return units;
    }

    @Override
    public Unit getUnitAt(Position position) {
        return board[position.row()][position.col()];
    }

    @Override
    public List<Unit> getInventory(Player who) {
        return inventoryMap.get(who);
    }

    @Override
    public Unit getItemInInventory(Player who, int index) {
        return getInventory(who).get(index);
    }

    @Override
    public Status playUnit(Player player, Unit unit, Position position) {
        if (this.gameState != GameState.PLAY_STATE) {
            return Status.NOT_PLAY_PHASE;
        }

        boolean isOwner = player == unit.getOwner();

        if (!isOwner) {
            return Status.NOT_OWNER;
        }

        if (getUnitAt(position) != null) {
            return Status.ALREADY_OCCUPIED;
        }

        placeUnitOnBoard(unit, position);

        return Status.OK;
    }

    @Override
    public Status moveUnit(Player who, Position oldPosition, Position newPosition) {

        if (this.playerStateMap.get(who) != PlayerState.PLAY) {
            return Status.PLAYER_NOT_IN_PLAY_STATE;
        }
        Unit unit = getUnitAt(oldPosition);
        if (unit == null) {
            return Status.NO_UNIT_AT_POSITION;
        }

        removeUnitFrom(oldPosition);
        placeUnitOnBoard(unit, newPosition);

        return Status.OK;
    }

    private void removeUnitFrom(Position pos) {
        this.board[pos.row()][pos.col()] = null;
    }

    private void placeUnitOnBoard(Unit unit, Position pos) {
        board[pos.row()][pos.col()] = unit;
    }

    @Override
    public Status usePower() {
        return null;
    }

    @Override
    public Status endShopPhase(Player who) {
        if (playerStateMap.get(who) != PlayerState.SHOP) {
            return Status.PLAYER_NOT_IN_SHOP_STATE;
        }

        playerStateMap.put(who, PlayerState.PLAY);
        return Status.OK;
    }

    @Override
    public Status endPlayPhase(Player who) {
        if (getPlayerState(who) != PlayerState.PLAY) {
            return Status.PLAYER_NOT_IN_PLAY_STATE;
        }

        playerStateMap.put(who, PlayerState.END_PLAY);
        boolean allEndedPlayPhase = true;

        for (PlayerState state : playerStateMap.values()) {
            allEndedPlayPhase &= state.equals(PlayerState.END_PLAY);
        }

        if (allEndedPlayPhase) {
            this.gameState = GameState.BATTLE_PHASE;
        }

        return Status.OK;
    }

    @Override
    public GameState getGameState() {
        return this.gameState;
    }

    @Override
    public List<Unit> getDeck(Player player) {
        return this.deckMap.get(player);
    }

    @Override
    public PlayerState getPlayerState(Player who) {
        return this.playerStateMap.get(who);
    }
}
