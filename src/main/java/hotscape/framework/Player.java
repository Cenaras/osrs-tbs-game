package hotscape.framework;

public enum Player {

    // TODO: Several players?
    SARADOMIN,
    ZAMORAK,
    NONE;

    public static Player computeOpponent(Player who) {
        return who == SARADOMIN ? ZAMORAK : SARADOMIN;
    }
}



