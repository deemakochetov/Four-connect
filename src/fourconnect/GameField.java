package fourconnect;

import java.util.Objects;

/**
 * Class that handles the state of the game field and operations with it.
 *
 * @author urhlc
 */
public class GameField {
    private static final String SIGN_SEPARATOR = "|";
    private static final String SIGN_PLACEHOLDER = " ";
    private static final String WHITESPACE = " ";
    private static final String NEW_LINE = System.lineSeparator();
    private static String[][] field;
    private final int numberOfRows;
    private final int numberOfColumns;

    /**
     * Initializes a new game field with specified dimensions.
     *
     * @param numberOfRows Number of rows the field should have.
     * @param numberOfColumns Number of columns the field should have.
     */
    public GameField(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        field = new String[numberOfRows][numberOfColumns];
    }

    /**
     * Getter method for the number of rows.
     *
     * @return number of rows.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Getter method for the number of columns.
     *
     * @return number of columns.
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * Puts specified checker to the first empty slot from below.
     *
     * @param column Index of the column to put the checker in.
     * @param checker Sign to be put into the free slot.
     * @return the index of the row, where checker was put to.
     */
    public int putCheckerToColumn(int column, String checker) {
        // start iterating from the last row index
        int currentRow = numberOfRows - 1;
        // find first empty slot searching from below
        while (field[currentRow][column] != null) {
            currentRow--;
        }
        // put checker in the free slot that was found
        field[currentRow][column] = checker;
        return currentRow;
    }

    /**
     * Checks if there is a vertical line of four specified elements in a row.
     * The winning line has to contain a specified slot (that was added last and therefore may build up a winning line).
     *
     * @param column Index of the column of the element that was added by the player last and therefore may be the part of the winning line.
     * @param row Index of the row of the element that was added by the player last and therefore may be the part of the winning line.
     * @param checker Sign of the user that was put into the slot.
     * @return {@code true} if there is a vertical line (containing specified slot) of four elements in a row. {@code false} otherwise.
     */
    private boolean checkForVerticalLines(int column, int row, String checker) {
        for (int minStartRow = 0; minStartRow <= row; minStartRow++) {
            int startingRow = Math.max(minStartRow, row - 3);
            if (checkConsecutive(startingRow, column, 1, 0, checker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a horizontal line of four specified elements in a row.
     * The winning line has to contain a specified slot (that was added last and therefore may build up a winning line).
     *
     * @param column Index of the column of the element that was added by the player last and therefore may be the part of the winning line.
     * @param row Index of the row of the element that was added by the player last and therefore may be the part of the winning line.
     * @param checker Sign of the user that was put into the slot.
     * @return {@code true} if there is a horizontal line (containing specified slot) of four elements in a row. {@code false} otherwise.
     */
    private boolean checkForHorizontalLines(int column, int row, String checker) {
        for (int minStartColumn = 0; minStartColumn <= column; minStartColumn++) {
            int startingColumn = Math.max(minStartColumn, column - 3);
            if (checkConsecutive(row, startingColumn, 0, 1, checker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a diagonal line of four specified elements in a row.
     * The winning line has to contain a specified slot (that was added last and therefore may build up a winning line).
     *
     * @param column Index of the column of the element that was added by the player last and therefore may be the part of the winning line.
     * @param row Index of the row of the element that was added by the player last and therefore may be the part of the winning line.
     * @param checker Sign of the user that was put into the slot.
     * @return {@code true} if there is a diagonal line (containing specified slot) of four elements in a row. {@code false} otherwise.
     */
    private boolean checkForDiagonalLines(int column, int row, String checker) {
        // check diagonally (top-left to bottom-right)
        if (row <= column) {
            int rowColumnDifference = row - column;
            // iterate over rows, calculating column based on the difference between them
            for (int startingDiagonalRow = Math.max(row - 3, 0); startingDiagonalRow <= row; startingDiagonalRow++) {
                if (checkConsecutive(startingDiagonalRow, startingDiagonalRow - rowColumnDifference, 1, 1, checker)) {
                    return true;
                }
            }
        } else {
            int columnRowDifference = column - row;
            // iterate over columns, calculating row based on the difference between them
            for (int startingDiagonalColumn = Math.max(column - 3, 0); startingDiagonalColumn <= column; startingDiagonalColumn++) {
                if (checkConsecutive(startingDiagonalColumn - columnRowDifference, startingDiagonalColumn, 1, 1, checker)) {
                    return true;
                }
            }
        }

        // Check diagonally (top-right to bottom-left)
        if (row <= column) {
            int rowColumnDifference = row - column;
            // iterate over rows, calculating column based on the difference between them
            for (int startRow = Math.max(row - 3, 0); startRow <= row; startRow++) {
                // pass -1 as column increment as column is being iterated backwards
                if (checkConsecutive(startRow, startRow - rowColumnDifference, 1, -1, checker)) {
                    return true;
                }
            }
        } else {
            int columnRowDifference = column - row;
            // iterate over columns, calculating row based on the difference between them
            for (int startColumn = Math.min(column + 3, numberOfColumns - 1); startColumn >= column; startColumn--) {
                // pass -1 as column increment as column is being iterated backwards
                if (checkConsecutive(startColumn - columnRowDifference, startColumn, 1, -1, checker)) {
                    return true;
                }
            }
        }
        // no conditions above were met, so there are no diagonal lines
        return false;
    }

    /**
     * Checks if player has won after adding specific element.
     * Returns true if there are vertical, horizontal or diagonal lines of four checker elements in a row.
     * The winning line has to contain a specified slot (that was added last and therefore may build up a winning line).
     *
     * @param column Index of the column of the element that was added by the player last and therefore may be the part of the winning line.
     * @param row Index of the row of the element that was added by the player last and therefore may be the part of the winning line.
     * @param checker Sign of the user that was put into the slot.
     * @return {@code true} if player has met at least one of winning conditions. {@code false} otherwise.
     */
    public boolean hasPlayerWon(int column, int row, String checker) {
        boolean hasVerticalLines = checkForVerticalLines(column, row, checker);
        boolean hasHorizontalLines = checkForHorizontalLines(column, row, checker);
        boolean hasDiagonalLines = checkForDiagonalLines(column, row, checker);

        return hasVerticalLines || hasHorizontalLines || hasDiagonalLines;
    }

    /**
     * Checks if there are four consecutive elements with specified value in a specified direction starting at a specified slot.
     *
     * @param startRow Index of the row to start iterating at.
     * @param startCol Index of the column to start iterating at.
     * @param rowIncrement The increment to be applied to the row count.
     * @param colIncrement The increment to be applied to the column count.
     * @param target The element to be matched.
     * @return {@code true} if there are four consecutive elements with specified value in a specified direction. {@code false} otherwise.
     */
    private boolean checkConsecutive(int startRow, int startCol, int rowIncrement, int colIncrement, String target) {
        for (int i = 0; i < 4; i++) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;

            if (!isValidIndex(row, col) || !Objects.equals(field[row][col], target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given indices are within the bounds of the field.
     *
     * @param row Index of the row to be checked.
     * @param col Index of the column to be checked.
     * @return {@code true} if indices are valid. {@code false} otherwise.
     */
    private boolean isValidIndex(int row, int col) {
        return row >= 0 && row < numberOfRows && col >= 0 && col < numberOfColumns;
    }

    /**
     * Determines whether the column with specified index has no empty slots.
     *
     * @param column Index of the column to be checked.
     * @return {@code true} if column is full. {@code false} otherwise.
     */
    public boolean isColumnFull(int column) {
        for (int row = 0; row < numberOfRows; row++) {
            if (field[row][column] == null) {
                return false;
            }
        }
        // all the elements in the column were not null(empty)
        return true;
    }

    /**
     * Overwritten method to build the string representation of the current state of the field.
     *
     * @return string representation of the current state of the field.
     */
    @Override
    public String toString() {
        String resultString = "";
        for (int rowCounter = 0; rowCounter < numberOfRows; rowCounter++) {
            resultString += WHITESPACE;
            for (int columnCounter = 0; columnCounter < numberOfColumns; columnCounter++) {
                if (columnCounter == 0) {
                    resultString += SIGN_SEPARATOR;
                }
                // print sign placeholder if no checker is in the slot
                if (field[rowCounter][columnCounter] == null) {
                    resultString += SIGN_PLACEHOLDER;
                } else {
                    resultString += field[rowCounter][columnCounter];
                }
                resultString += SIGN_SEPARATOR;
            }
            resultString += NEW_LINE;
        }
        return resultString;
    }
}
