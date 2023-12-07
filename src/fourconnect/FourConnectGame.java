package fourconnect;

import java.util.ArrayList;
import java.util.Scanner;

import static fourconnect.ErrorConstants.inputIsNotAnIntError;
import static fourconnect.ErrorConstants.columnShouldBeInRangeError;
import static fourconnect.ErrorConstants.columnIsFullError;
import static fourconnect.ErrorConstants.maxAmountOfPlayersError;
import static fourconnect.ErrorConstants.signMustBeOneCharError;
import static fourconnect.ErrorConstants.signsMustBeUniqueError;

import static fourconnect.FourConnectGameLogic.ROW_COUNT;
import static fourconnect.FourConnectGameLogic.COLUMN_COUNT;
import static fourconnect.FourConnectGameLogic.isColumnFull;
import static fourconnect.FourConnectGameLogic.getField;
import static fourconnect.FourConnectGameLogic.putCheckerToColumn;
import static fourconnect.FourConnectGameLogic.hasPlayerWon;

/**
 * Static class that manages interactions with users and uses FourConnectGameLogic class to play the game.
 * Main class in the application.
 *
 * @author Dmytro Kochetov
 * @version 1,0
 */
public class FourConnectGame {
    private static final String PLAYER_1_DEFAULT_SIGN = "x";
    private static final String PLAYER_2_DEFAULT_SIGN = "o";
    private static final String SIGN_SEPARATOR = "|";
    private static final String SIGN_PLACEHOLDER = " ";
    private static final String WHITESPACE = " ";
    private static final String QUIT_COMMAND = "quit";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Validates command line arguments, sets player signs and starts the game.
     *
     * @param args The list of command arguments, that will be used as player signs,
     * If player signs are invalid error will be printed and program will be terminated.
     */
    public static void main(String[] args) { // add java doc
        ArrayList<String> players = new ArrayList<>();
        if (args.length == 0) {
            players.add(PLAYER_1_DEFAULT_SIGN);
            players.add(PLAYER_2_DEFAULT_SIGN);
        }
        if (args.length > 6) {
            printErrorAndTerminate(maxAmountOfPlayersError);
        }
        for (String player: args) {
            if (player.length() != 1) {
                printErrorAndTerminate(signMustBeOneCharError);
            }
            if (players.contains(player)) {
                printErrorAndTerminate(signsMustBeUniqueError);
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

        printField();
        int turnCount = 1;
        int column;
        while (true) {
            int playerIndex = turnCount % numberOfPlayers;
            // if turn count is divided by number of players by a whole
            // set index to be the last player
            if (playerIndex == 0) playerIndex = numberOfPlayers;

            printTurn(turnCount, playerIndex);
            turnCount++;
            column = inputColumn();

            int row = putCheckerToColumn(column - 1, players.get(playerIndex - 1));
            printField();

            // when step count is exactly the area of the field, the game stops
            if (turnCount == ROW_COUNT * COLUMN_COUNT) {
                // stop the game
                System.out.println("Unentschieden!");
                terminate();
            }

            if(hasPlayerWon(column - 1, row, players.get(playerIndex - 1))) {
                System.out.printf("Sieger: Spieler %s", players.get(playerIndex - 1));
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
        scanner.close();
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
     */
    private static void printField() {
        final String[][] field = getField();
        for (int rowCounter = 0; rowCounter < ROW_COUNT; rowCounter++) {
            System.out.print(WHITESPACE);
            for (int columnCounter = 0; columnCounter < COLUMN_COUNT; columnCounter++) {
                if (columnCounter == 0) {
                    System.out.print(SIGN_SEPARATOR);
                }
                // print sign placeholder if no checker is in the slot
                if (field[rowCounter][columnCounter] == null) {
                    System.out.print(SIGN_PLACEHOLDER);
                } else {
                    System.out.print(field[rowCounter][columnCounter]);
                }
                System.out.print(SIGN_SEPARATOR);
            }
            // print new line
            System.out.println();
        }
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
     * @return Entered number
     */
    private static int inputColumn() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equals(QUIT_COMMAND)) terminate();
            int number;
            try {
                number = Integer.parseInt(input);

                if (isInValidRange(number)) {
                    printError(columnShouldBeInRangeError);
                } else {
                    int columnIndex = number - 1;
                    if (isColumnFull(columnIndex)) {
                        printError(columnIsFullError);
                    }
                    else return number;
                }
            } catch (NumberFormatException e) {
                printError(inputIsNotAnIntError);
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
