package fourconnect;

import java.util.Objects;

public class FourConnectGameLogic {
    public static final int ROW_COUNT = 6;
    public static final int COLUMN_COUNT = 7;
    private static final String[][] field = new String[ROW_COUNT][COLUMN_COUNT];
    public static String[][] getField() {
        return field;
    }
    public static int putCheckerToColumn(int column, String checker) {
        int row = ROW_COUNT - 1;
        // find first empty slot searching from below
        while (field[row][column] != null) {
            row--;
        }
        // put checker in free slot
        field[row][column] = checker;
        return row;
    }

    public static boolean hasPlayerWon(int column, int row, String checker) {
        // check for vertical lines
        int minStartRow = 0;
        outerWhile:
        while (true) {
            if (minStartRow == row) break;
            int startingRow = Math.max(minStartRow, row - 3);
            for (int i = 0; i < 4; i++) {
                if (!Objects.equals(field[startingRow + i][column], checker)) {
                    minStartRow = Math.min(startingRow + i + 1, row);
                    continue outerWhile;
                }
            }
            return true;
        }

        // code duplication
        // check for horizontal lines
        int minStartColumn = 0;
        outerWhile:
        while (true) {
            if (minStartColumn == column) break;
            int startingColumn = Math.max(minStartColumn, column - 3);
            for (int i = 0; i < 4; i++) {
                if (!Objects.equals(field[row][startingColumn + i], checker)) {
                    minStartColumn = Math.min(startingColumn + i + 1, column);
                    continue outerWhile;
                }
            }
            return true;
        }

        // check for diagonal lines
        // Check diagonally (top-left to bottom-right)
        if (row >= column) {
            int rowColumnDifference = row - column;
            for (int startingDiagonalRow = Math.max(row - 3, 0); startingDiagonalRow <= row; startingDiagonalRow++) {
                if (checkConsecutive(startingDiagonalRow, startingDiagonalRow - rowColumnDifference, 1, 1, checker)) {
                    return true;
                }
            }
        } else {
            int columnRowDifference = column - row;
            for (int startingDiagonalColumn = Math.max(column - 3, 0); startingDiagonalColumn <= column; startingDiagonalColumn++) {
                if (checkConsecutive(startingDiagonalColumn - columnRowDifference, startingDiagonalColumn, 1, 1, checker)) {
                    return true;
                }
            }
        }

        // Check diagonally (top-right to bottom-left)
        if (row >= column) {
            int rowColumnDifference = row - column;
            for (int startingDiagonalRow = Math.max(row - 3, 0); startingDiagonalRow <= row; startingDiagonalRow++) {
                if (checkConsecutive(startingDiagonalRow, startingDiagonalRow - rowColumnDifference, 1, -1, checker)) {
                    return true;
                }
            }
        } else {
            int columnRowDifference = column - row;
            for (int startingDiagonalColumn = Math.min(column + 3, COLUMN_COUNT - 1); startingDiagonalColumn >= column; startingDiagonalColumn--) {
                if (checkConsecutive(startingDiagonalColumn - columnRowDifference, startingDiagonalColumn, 1, -1, checker)) {
                    return true;
                }
            }
        }

        // if no checker lines of length 4 were found return false
        return false;
    }

    // Function to check for four consecutive elements in a specified direction
    private static boolean checkConsecutive(int startRow, int startCol, int rowIncrement, int colIncrement, String target) {
        for (int i = 0; i < 4; i++) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;

            if (!isValidIndex(row, col) || !Objects.equals(field[row][col], target)) {
                return false;
            }
        }
        return true;
    }

    // Function to check if the given indices are within the bounds of the array
    private static boolean isValidIndex(int row, int col) {
        return row >= 0 && row < ROW_COUNT && col >= 0 && col < COLUMN_COUNT;
    }

    public static boolean isColumnFull(int column) {
        for (int row = 0; row < ROW_COUNT; row++) {
            if (field[row][column] == null) return false;
        }
        // all the elements in the column were not null
        return true;
    }
}
