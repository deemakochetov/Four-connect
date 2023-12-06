package fourconnect;

import java.util.ArrayList;
import java.util.Scanner;

import static fourconnect.FourConnectGameLogic.*;
import static fourconnect.GameConstants.COLUMN_COUNT;
import static fourconnect.GameConstants.ROW_COUNT;

public class FourConnectGame {
    private static final String PLAYER_1_DEFAULT_SIGN = "x";
    private static final String PLAYER_2_DEFAULT_SIGN = "o";
    private static final String SIGN_SEPARATOR = "|";
    private static final String SIGN_PLACEHOLDER = " ";
    private static final String WHITESPACE = " ";
    private static final String QUIT_COMMAND = "quit";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Validates args , sets player signs and loops by asking the user for a turn, until game is over
     *
     * @param args player signs, if they are invalid error will be outputted and error shown
     */
    public static void main(String[] args) { // add java doc
        ArrayList<String> players = new ArrayList<>();
        if (args.length == 0) {
            players.add(PLAYER_1_DEFAULT_SIGN);
            players.add(PLAYER_2_DEFAULT_SIGN);
        }
        if (args.length > 6) {
            printErrorAndTerminate("The maximum allowed number of players is 6.");
        }
        for (String player: args) {
            if (player.length() != 1) {
                printErrorAndTerminate("The player sign must consist of exactly one character.");
            }
            if (players.contains(player)) {
                printErrorAndTerminate("The player signs must be unique.");
            }
            // add player if validation was passed successfully
            players.add(player);
        }

        startGame(players);
    }

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

    private static void printErrorAndTerminate(String message) {
        printError(message);
        // pass 1 as there was an error
        terminate(1);
    }

    private static void printError(String message) {
        System.err.println("ERROR: " + message);
    }

    private static void terminate(int code) {
        scanner.close();
        System.exit(code);
    }

    private static void terminate() {
        terminate(0);
    }

    private static void printField() {
        final String[][] field = getField();
        for (int rowCounter = 0; rowCounter < ROW_COUNT; rowCounter++) {
            System.out.print(WHITESPACE);
            for (int columnCounter = 0; columnCounter < COLUMN_COUNT; columnCounter++) {
                if (columnCounter == 0) {
                    System.out.print(SIGN_SEPARATOR);
                }
                // print whitespace if no checker is in the slot
                if (field[rowCounter][columnCounter] == null) {
                    System.out.print(SIGN_PLACEHOLDER);
                } else {
                    System.out.print(field[rowCounter][columnCounter]);
                }
                System.out.print(SIGN_SEPARATOR);
            }
            System.out.println();
        }
    }

    private static void printTurn(int turnCount, int playerIndex) {
        String turnInformation = "%s. Zug, Spieler %s".formatted(turnCount, playerIndex);
        System.out.println(turnInformation);
    }

    private static int inputColumn() {
        while (true) {
            String input = scanner.nextLine();
            if (input.equals(QUIT_COMMAND)) terminate();
            int number;
            try {
                number = Integer.parseInt(input);

                if (number < 1 || number > 7) {
                    printError("The number of column must be between 1 and 7.");
                } else {
                    if (isColumnFull(number - 1)) {
                        printError("The column is already full.");
                    }
                    else return number;
                }
            } catch (NumberFormatException e) {
                printError("The input is not a valid integer.");
            }
        }
    }

}
