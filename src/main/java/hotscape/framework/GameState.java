package hotscape.framework;

public enum GameState {
    /**
     * The state where players are allowed to play units.
     */
    PLAY_STATE,
    /**
     * The state where units are performing their actions, and the battle is fought.
     */
    BATTLE_PHASE
}
