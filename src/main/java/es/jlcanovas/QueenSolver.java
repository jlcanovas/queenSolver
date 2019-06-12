package es.jlcanovas;

import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Solver for N Queens challenge + additional constraint:
 *  - Problem: Place N queens on an NxN chess board so that none of them attack each other.
 *  - Constraint:  make sure that no three queens are in a straight line at ANY angle, so queens on A1, C2 and E3,
 *    despite not attacking each other, form a straight line at some angle.
 *
 *  The solver performs a depth search and prints every solution that is found. Each level of the search tree
 *  corresponds to a new row of the board. A queen is tried to be located in each column of the row, always checking
 *  these rules:
 *  R1. No queen has been previously located in the same row
 *  R2. No queen has been previously located in the same column
 *  R3. No queen has been previously located in the diagonals (at 45 and 135 degrees)
 *  R4. No 3 queens are in straight line
 *
 *  To meet R1 and R2, we use an array to represent the solution. The array is indexed according to the row and
 *  array values represent the column where the queen is located. For example, sol=[1,3,0,2,4] is a valid solution w
 *  where the first queen is located in row 0 and column 1, the second queen is located in row 2 and column 3, etc.
 *
 *  To meet R3, we first need to know that the values of the cells in the diagonal at 135 degrees are always equals to
 *  row-col; and that the values of the cells in the diagonal at 45 degrees are always equals to row+col. Also, in a
 *  board of n cols/rows, there are 2n-1 diagonals (in each direction). With that in mind we use 2 boolean arrays
 *  (one per diagonal) to keep track of diagonals already attacked. Thus once a queen is located we mark the diagonals.
 *  For instance, in a board of n=4, a queen at position (2,1) will mark 135-degrees diagonal 1 and 45-degrees
 *  diagonal 3.
 *
 *  To meet R4, we rely on the fact that the area of the triangle built by 3 points in line is 0. As we need to check
 *  ANY combination of 3 points in the board, once we reach 3 or more queens we check that no 3-points are in line. To
 *  do this, we generate the possible combinations for the points (i.e., queens) in the board and calculate the area.
 *  This is maybe the most time consuming calculation of the solver but as it is performed while the tree is being
 *  searched, we assure some pruning in the search space
 */
public class QueenSolver {
    /**
     * Solutions found
     */
    private List<int[]> solutions;

    /**
     * The size of the board to be solved
     */
    private int size;

    /**
     * Constructor for the class. Only the size od the board has to be given
     * @param size The size of the board (will be size x size)
     */
    public QueenSolver(int size) {
        // Just a bit of control
        if(size <= 0)
            throw new IllegalArgumentException("The size must be greater than 0");

        this.size = size;
        this.solutions = new ArrayList<>();
    }

    /**
     * Launches the solver for a specific value of n (the dimension of the board)
     */
    public void launch() {
        int n = this.size;
        int[] start = new int[n];
        for(int i = 0; i < n; i++) start[i] = -1; // Initializing the board (empty positions with -1 value)
        boolean[] diag135 = new boolean[n+(n-1)];
        boolean[] diag45 = new boolean[n+(n-1)];
        for(int i = 0; i < n+(n-1); n++) {        // Initializing the diagonal attacks
            diag135[i] = false;
            diag45[i] = false;
        }
        solve(0, start, diag135, diag45);
    }

    /**
     * Main method to solve the problem. Once a solution is found, it is printed via printBoard. This method is called
     * recursively for each level of the search tree. There are as many levels as the n value of the board. In each
     * level, a new row is explored an a queen is placed.
     *
     * @param row The row of the board that is going to the searched
     * @param sol The current solution (i.e., the board, represented as an array indexed according to the row and
     *            array values represent the column where the queen is located)
     * @param diag135 The diagonals at 135 degrees already attacked by a queen
     * @param diag45 The diagonals at 45 degrees already attacked by a queen
     */
    private void solve(int row, int[] sol, boolean[] diag135, boolean[] diag45) {
        // If we have reach the max level of the search tree, we found a solution, we store it and return
        if(row == sol.length) {
            this.solutions.add(sol);
            return;
        }

        // We try with every column of this row
        for(int col = 0; col < sol.length; col++) {
            if(isValid(row, col, sol, diag135, diag45)) {
                // Position of the queen is valid so we track the changes
                // We duplicate :( arrays to keep track of each solution
                // First in the array storing the solution
                int[] newSol = Arrays.copyOf(sol, sol.length);
                newSol[row] = col;

                // Then we keep track of the attacked diagonals
                boolean[] newDiag135 = Arrays.copyOf(diag135, diag135.length);
                int checkDiag135 = row - col + (sol.length - 1);
                newDiag135[checkDiag135] = true;
                boolean[] newDiag45 = Arrays.copyOf(diag45, diag45.length);
                int checkDiag45 = row + col;
                newDiag45[checkDiag45] = true;

                // We launch a new solver for the next row level (and pass the partial solution)
                solve(row+1, newSol, newDiag135, newDiag45);
            }
        }
    }

    /**
     * This method checks that a potential position for a queen in the board is valid. The method checks the main
     * constraints of the problem (i.e,. R1, R2, R3 and R4)
     *
     * We set the visibility of this method to package access to ease the creation of test cases and illustrate its
     * behavior. In future versions, it can also help to be specialized by subclasses.
     *
     * @param row The row where we want to place the queen
     * @param col The column where we want to place the queen
     * @param sol The current solution (i.e., the board, represented as an array indexed according to the row and
     *            array values represent the column where the queen is located)
     * @param diag135 The diagonals at 135 degrees already attacked by a queen
     * @param diag45 The diagonals at 45 degrees already attacked by a queen
     * @return True if the position does not violate the constraints of the problem
     */
    boolean isValid(int row, int col, int[] sol, boolean[] diag135, boolean[] diag45) {
        // R1 & R2. No queen has been previously located in the same row & column
        for(int checkRow = 0; checkRow < sol.length; checkRow++) {
            if(sol[checkRow] == col)
                return false;
        }

        // R3. No queen has been previously located in the diagonals (at 45 and 135 degrees)
        // Positions at the same diagonal at 135 degrees share the value row-col (which of course can have negative
        // values). We work with the following formula to be able to be indexed in an array.
        int checkDiag135 = row - col + (sol.length - 1);
        if(diag135[checkDiag135]) return false;
        int checkDiag45 = row + col;
        if(diag45[checkDiag45]) return false;

        // R4. No 3 queens are in straight line
        // Only if we have at least 3 queen in play
        if(row > 2) {
            // We have to temporary include the queen in the solution to check all the possible stright lines in the board
            sol[row] = col;
            // We generate all the possible combinations given the current row (as it's a zero-indexed array, we need
            // row+1) in groups of 3
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(row+1, 3);
            while (iterator.hasNext()) {
                int[] comb = iterator.next();
                // We take the points in the board
                int ay = comb[0]; int ax = sol[comb[0]];
                int by = comb[1]; int bx = sol[comb[1]];
                int cy = comb[2]; int cx = sol[comb[2]];
                // Calculate the area of triangle represented by these points
                // NOTE: the formula does requires division by 2, but as we are looking for zero value, there is no
                //       need for such division.
                float area = ax * (by - cy) + bx * (cy - ay) + cx * (ay - by);
                // If the area is zero, they are in line and reject this position
                if(area == 0.0) {
                    sol[row] = -1; // We need to recover the previous value of this position
                    return false;
                }
            }
            sol[row] = -1; // We need to recover the previous value of this position
        }
        return true;
    }

    /**
     * Prints all the solutions found
     */
    public void printSolutions() {
        for(int[] sol : this.solutions)
            printBoard(sol);
    }

    /**
     * Returns the solutions 
     * @return List of boards (represented as int[])
     */
    public List<int[]> getSolutions() {
        return this.solutions;
    }

    /**
     * Prints a board with a solution (i.e., the array has to include >=0 values)
     *
     * @param sol The current solution (i.e., the board, represented as an array indexed according to the row and
     *            array values represent the column where the queen is located)
     */
    private void printBoard(int[] sol) {
        // If any value of the array includes -1 (init value of the position), we return
        for(int rowCount = 0; rowCount < sol.length; rowCount++) {
            if (sol[rowCount] == -1) {
                System.out.println("Solution not valid");
                return;
            }
        }

        // We build the board playing with strings
        StringBuilder board = new StringBuilder();
        for(int rowCount = 0; rowCount < sol.length; rowCount++) { // For each row
            char[] row = new char[sol.length];
            for(int i = 0; i < row.length; i++)  // We fill the row with # symbols to represent empty positions
                row[i] = '#';
            int queenColumn = sol[rowCount];
            row[queenColumn] = 'Q';              // The queen is represented by Q
            board.append(row);
            board.append("\n");
        }
        board.append("\n");

        System.out.print(board.toString());
    }

    /**
     * Code to launch the solver directly from here. Change the size parameter of the constructor to play with
     * different board sizes
     *
     * @param args Currently not used
     */
    public static void main(String[] args) {
       QueenSolver qs = new QueenSolver(4);
       qs.launch();
       qs.printSolutions();
       System.out.println("Solutions found: " + qs.getSolutions().size());
    }
}
