package hotscape.standard;

import hotscape.framework.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  TODO: Make a "Simple Initial Hand" to make production easier, worry about balance later on...

public class StandardGame implements Game {

    private final Map<Player, StandardHero> heroMap;
    private final Map<Player, Boolean> endPlayPhaseMap;
    private final Map<Player, List<Unit>> inventoryMap;

    private final Unit[][] board = new Unit[GameConstants.MAX_ROW][GameConstants.MAX_COL];

    private GameState gameState;


    public StandardGame() {
        this.heroMap = new HashMap<>(Map.of(
                Player.SARADOMIN, new StandardHero(Player.SARADOMIN, GameConstants.WISE_OLD_MAN_HERO),
                Player.ZAMORAK, new StandardHero(Player.ZAMORAK, GameConstants.GNOME_CHILD_HERO)
        ));

        this.endPlayPhaseMap = new HashMap<>(Map.of(
                Player.SARADOMIN, false,
                Player.ZAMORAK, false
        ));

        this.inventoryMap = new HashMap<>(Map.of(
                Player.SARADOMIN, List.of(new StandardUnit(Player.SARADOMIN, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4))),
                Player.ZAMORAK, List.of(new StandardUnit(Player.ZAMORAK, new UnitSpec(GameConstants.GOBLIN_UNIT, 1, 5, 4)))
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

    private void placeUnitOnBoard(Unit unit, Position pos) {
        board[pos.row()][pos.col()] = unit;
    }

    @Override
    public Status usePower() {
        return null;
    }

    @Override
    public Status endPlayPhase(Player who) {
        if (this.gameState != GameState.PLAY_STATE) {
            return Status.NOT_PLAY_PHASE;
        }

        endPlayPhaseMap.put(who, true);
        boolean allEndedPlayPhase = true;

        for (Boolean end : endPlayPhaseMap.values()) {
            allEndedPlayPhase &= end;
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
}
