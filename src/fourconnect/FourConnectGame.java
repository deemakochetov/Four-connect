package fourconnect;

import java.util.ArrayList;
import java.util.Scanner;

import static fourconnect.ErrorConstants.SIGNS_MUST_BE_UNIQUE_ERROR;
import static fourconnect.ErrorConstants.SIGN_MUST_BE_ONE_CHAR_ERROR;
import static fourconnect.ErrorConstants.MAX_AMOUNT_OF_PLAYERS_ERROR;
import static fourconnect.ErrorConstants.COLUMN_IS_FULL_ERROR;
import static fourconnect.ErrorConstants.COLUMN_SHOULD_BE_IN_RANGE_ERROR;
import static fourconnect.ErrorConstants.INPUT_IS_NOT_AN_INT_ERROR;

/**
 * Static class that manages interactions with users and uses FourConnectGameLogic class to play the game.
 * Main class in the application.
 *
 * @author urhlc
 */
public final class FourConnectGame {
    private static final String PLAYER_1_DEFAULT_SIGN = "x";
    private static final String PLAYER_2_DEFAULT_SIGN = "o";
    private static final String QUIT_COMMAND = "quit";
    private static final int ROW_COUNT = 6;
    private static final int COLUMN_COUNT = 8;
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Empty constructor as utility classes must not be initialised.
     */
    private FourConnectGame() {
    }

    /**
     * Validates command line arguments, sets player signs and starts the game.
     * If player signs are invalid error will be printed and program will be terminated.
     *
     * @param args The list of command arguments, that will be used as player signs.
     */
    public static void main(String[] args) { // add java doc
        ArrayList<String> players = new ArrayList<>();
        if (args.length == 0) {
            players.add(PLAYER_1_DEFAULT_SIGN);
            players.add(PLAYER_2_DEFAULT_SIGN);
        }
        if (args.length > 6) {
            printErrorAndTerminate(MAX_AMOUNT_OF_PLAYERS_ERROR);
        }
        for (String player: args) {
            if (player.length() != 1) {
                printErrorAndTerminate(SIGN_MUST_BE_ONE_CHAR_ERROR);
            }
            if (players.contains(player)) {
                printErrorAndTerminate(SIGNS_MUST_BE_UNIQUE_ERROR);
            }
            // add player if validation was passed successfully
            players.add(player);
        }

        startGame(players);
    }

    /**
     * Reads user inputs and modifies the field accordingly.
     * Continues until the game stops or someone wins.
     *
     * @param players A list of player signs.
     */
    private static void startGame(ArrayList<String> players) {
        int numberOfPlayers = players.size();

        GameField gameField = new GameField(ROW_COUNT, COLUMN_COUNT);
        printField(gameField);
        int turnCount = 1;
        int column;
        // the program will terminate as there are no empty slots more or someone wins, so the loop is not infinite
        while (true) {
            int playerIndex = turnCount % numberOfPlayers;
            // if turn count is divided by number of players by a whole
            // set index to be the last player
            if (playerIndex == 0) {
                playerIndex = numberOfPlayers;
            }

            printTurn(turnCount, playerIndex);
            turnCount++;
            column = inputColumn(gameField);

            int row = gameField.putCheckerToColumn(column - 1, players.get(playerIndex - 1));
            printField(gameField);

            // when step count is exactly the area of the field, the game stops
            if (turnCount == ROW_COUNT * COLUMN_COUNT) {
                // stop the game
                System.out.println("Unentschieden!");
                terminate();
            }

            if (gameField.hasPlayerWon(column - 1, row, players.get(playerIndex - 1))) {
                System.out.printf("Sieger: Spieler %s", playerIndex);
                terminate();
            }
        }
    }

    /**
     * Prints specified error message and terminates the program.
     *
     * @param message Error message to be printed.
     */
    private static void printErrorAndTerminate(String message) {
        printError(message);
        // pass 1 as there was an error
        terminate(1);
    }

    /**
     * Prints an error message with the error prefix.
     *
     * @param message Error message to be printed.
     */
    private static void printError(String message) {
        System.err.println("ERROR: " + message);
    }

    /**
     * Terminates the program with a specific code, closing the scanner before.
     *
     * @param code Code to be terminated with.
     */
    private static void terminate(int code) {
        SCANNER.close();
        System.exit(code);
    }

    /**
     * Terminates the program.
     */
    private static void terminate() {
        terminate(0);
    }

    /**
     * Prints the current state of the field.
     *
     * @param field Instance of GameField. Field to be printed.
     */
    private static void printField(GameField field) {
        System.out.println(field);
    }

    /**
     * Prints the information about the game turn with information about the step count and player number.
     *
     * @param turnCount The step count to be showed.
     * @param playerIndex the index of the player to be showed.
     */
    private static void printTurn(int turnCount, int playerIndex) {
        String turnInformation = "%s. Zug, Spieler %s".formatted(turnCount, playerIndex);
        System.out.println(turnInformation);
    }

    /**
     * Reads user input, converts it to integer.
     * Prints error messages if entered column is invalid, full or cannot be converted to integer.
     * Continues to read user input until quit command is used or valid column number received.
     *
     * @param gameField Instance of GameField to check for full column.
     * @return Entered number
     */
    private static int inputColumn(GameField gameField) {
        while (true) {
            String input = SCANNER.nextLine();
            if (input.equals(QUIT_COMMAND)) {
                terminate();
            }
            int number;
            try {
                number = Integer.parseInt(input);

                if (!isInValidRange(number)) {
                    printError(COLUMN_SHOULD_BE_IN_RANGE_ERROR);
                } else {
                    int columnIndex = number - 1;
                    if (gameField.isColumnFull(columnIndex)) {
                        printError(COLUMN_IS_FULL_ERROR);
                    } else {
                        return number;
                    }
                }
            } catch (NumberFormatException e) {
                printError(INPUT_IS_NOT_AN_INT_ERROR);
            }
        }
    }

    /**
     * Checks if the specified column is within the valid range.
     *
     * @param column The column to be checked for validity.
     * @return {@code true} if the column is within the valid range, {@code false} otherwise.
     */
    private static boolean isInValidRange(int column) {
        return ((column >= 1) && (column <= COLUMN_COUNT));
    }

}
