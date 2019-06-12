package es.jlcanovas;

import org.junit.Test;
import org.junit.Assert;

/**
 * Some test cases for illustrating the behavior of the QueenSolver
 */
public class QueenSolverTest {

    /**
     * Some tests to illustrate the isValid Method for a board of size 5
     */
    @Test
    public void isValidTest5() {
        int n = 5;
        QueenSolver qs = new QueenSolver(n);

        // Init values
        int[] sol = new int[n];
        boolean[] diag135 = new boolean[n+(n-1)];
        boolean[] diag45 = new boolean[n+(n-1)];
        for(int i = 0; i < n+(n-1); n++) {
            diag135[i] = false;
            diag45[i] = false;
        }

        // CASE 1. Invalid. Same column
        //
        // #Q###
        // #Q### <- We are checking at this level (row 1)
        // #####
        // #####
        // #####
        sol = new int[] {1, -1, -1, -1, -1}; // We set the queens for the first row
        Assert.assertFalse(qs.isValid(1, 1, sol, diag135, diag45));

        // CASE 2. Invalid. Same 135-degrees diagonal
        //
        // #Q###
        // ##Q## <- We are checking at this level (row 1)
        // #####
        // #####
        // #####
        sol = new int[] {1, -1, -1, -1, -1}; // We set the queens for the first row
        diag135[3] = true; // We set the 135-degree diagonal attacked by the queen on first row
        diag45[6] = true;  // We set the 45-degree diagonal attacked by the queen on first row
        Assert.assertFalse(qs.isValid(1, 2, sol, diag135, diag45));

        // CASE 3. Invalid. 3 queens in a row
        //
        // ###Q#
        // #Q###
        // ####Q
        // ##Q##
        // Q#### <- We are checking at this level (row 4)
        sol = new int[] {3, 1, 4, 2, -1}; // We set the queens for the four upper rows
        diag135[3] = false; // We are "deactivating" the diagonal control to force to check the constraint regarding
        diag45[6] = false;  // the 3queens on line :)
        Assert.assertFalse(qs.isValid(4, 0, sol, diag135, diag45));
    }

    /**
     * Some tests to illustrate the isValid Method for a board of size 4
     */
    @Test
    public void isValidTest4() {
        int n = 4;
        QueenSolver qs = new QueenSolver(n);

        // Init values
        int[] sol = new int[n];
        boolean[] diag135 = new boolean[n+(n-1)];
        boolean[] diag45 = new boolean[n+(n-1)];
        for(int i = 0; i < n+(n-1); n++) {
            diag135[i] = false;
            diag45[i] = false;
        }

        // CASE 1. Invalid. Same column
        //
        // #Q##
        // ###Q
        // ###Q <- We are checking at this level (row 2)
        // ####
        sol = new int[] {1, 3, -1, -1}; // We set the queens for the two upper rows
        diag135[2] = true; // We set the 135-degree diagonal attacked by the queen on first row
        diag45[1] = true;  // We set the 45-degree diagonal attacked by the queen on first row
        diag135[1] = true; // We set the 135-degree diagonal attacked by the queen on second row
        diag45[4] = true;  // We set the 45-degree diagonal attacked by the queen on second row
        Assert.assertFalse(qs.isValid(2, 3, sol, diag135, diag45));

        // CASE 2. Valid. Partial Solution
        //
        // #Q##
        // ###Q
        // Q### <- We are checking at this level (row 2)
        // ####
        sol = new int[] {1, 3, -1, -1}; // We set the queens for the two upper rows
        diag135[2] = true; // We set the 135-degree diagonal attacked by the queen on first row
        diag45[1] = true;  // We set the 45-degree diagonal attacked by the queen on first row
        diag135[1] = true; // We set the 135-degree diagonal attacked by the queen on second row
        diag45[4] = true;  // We set the 45-degree diagonal attacked by the queen on second row
        Assert.assertTrue(qs.isValid(2, 0, sol, diag135, diag45));

        // CASE 3. Valid. Final Solution
        //
        // #Q##
        // ###Q
        // Q###
        // ##Q# <- We are checking at this level (row 3)
        sol = new int[] {1, 3, 0, -1}; // We set the queens for the three upper rows
        diag135[2] = true; // We set the 135-degree diagonal attacked by the queen on first row
        diag45[1] = true;  // We set the 45-degree diagonal attacked by the queen on first row
        diag135[1] = true; // We set the 135-degree diagonal attacked by the queen on second row
        diag45[4] = true;  // We set the 45-degree diagonal attacked by the queen on second row
        diag135[5] = true; // We set the 135-degree diagonal attacked by the queen on third row
        diag45[2] = true;  // We set the 45-degree diagonal attacked by the queen on third row
        Assert.assertTrue(qs.isValid(3, 2, sol, diag135, diag45));
    }

    /**
     * Test the solver for board of size 4. There must be 2 solutions
     */
    @Test
    public void launchTest4() {
        QueenSolver qs = new QueenSolver(4);
        qs.launch();
        Assert.assertEquals(2, qs.getSolutions().size());
    }

    /**
     * Test the solver for board of size 8. There must be 8 solutions
     */
    @Test
    public void launchTest8() {
        QueenSolver qs = new QueenSolver(8);
        qs.launch();
        Assert.assertEquals(8, qs.getSolutions().size());
    }
}
