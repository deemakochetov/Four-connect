package fourconnect;

/**
 * Static final class with error constants.
 *
 * @author urhlc
 */
public final class ErrorConstants {
    /**
     * Error to be printed when column is out of valid range.
     */
    public static final String COLUMN_SHOULD_BE_IN_RANGE_ERROR = "The column must be between 1 and 7.";
    /**
     * Error to be printed when column does not have any free slots more.
     */
    public static final String COLUMN_IS_FULL_ERROR = "The column is already full.";
    /**
     * Error to be printed when given input cannot be converted to integer.
     */
    public static final String INPUT_IS_NOT_AN_INT_ERROR = "The input is not a valid integer.";
    /**
     * Error to be printed when a user tries to add a new player, wenn the maximum allowed of players is reached.
     */
    public static final String MAX_AMOUNT_OF_PLAYERS_ERROR = "The maximum allowed number of players is 6.";
    /**
     * Error to be printed when given string consists of more than one element.
     */
    public static final String SIGN_MUST_BE_ONE_CHAR_ERROR = "The player sign must consist of exactly one character.";
    /**
     * Error to be printed when two same signs are assigned to different players.
     */
    public static final String SIGNS_MUST_BE_UNIQUE_ERROR = "The player signs must be unique.";

    /**
     * Empty constructor as constant classes must not be initialised.
     */
    private ErrorConstants() {
    }
}
