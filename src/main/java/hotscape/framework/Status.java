package hotscape.framework;

public enum Status {

    /**
     * Trying to perform hero action twice pr play turn.
     */
    NO_HERO_ACTION_TWICE_PR_TURN,
    /**
     * Trying to perform a play action during non-play phase.
     */
    NOT_PLAY_PHASE,
    /**
     * Trying to end play phase after already having ended it.
     */
    NO_END_PLAY_PHASE_TWICE,
    /**
     * Trying to perform an action with insufficient GP
     */
    NOT_ENOUGH_GP,

    ALREADY_OCCUPIED,

    /**
     * Everything OK.
     */
    OK, NOT_OWNER,
}
