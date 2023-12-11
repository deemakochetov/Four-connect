package fourconnect;

/**
 * Static final class with error constants
 */
public final class ErrorConstants {
    public static final String columnShouldBeInRangeError = "The column must be between 1 and 7.";
    public static final String columnIsFullError = "The column is already full.";
    public static final String inputIsNotAnIntError = "The input is not a valid integer.";
    public static final String maxAmountOfPlayersError = "The maximum allowed number of players is 6.";
    public static final String signMustBeOneCharError = "The player sign must consist of exactly one character.";
    public static final String signsMustBeUniqueError = "The player signs must be unique.";

    /**
     * Empty constructor as constant classes must not be initialised
     */
    private ErrorConstants() {

    }
}
