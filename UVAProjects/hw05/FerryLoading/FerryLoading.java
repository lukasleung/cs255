import java.util.Arrays;
import java.util.Scanner;

/**
 * UVA 10261 Ferry Loading Timelimit = 3.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac FerryLoading
 *      java FerryLoading < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 3.0
 */
public class FerryLoading {
    public static void printS(boolean[][] s) {
        System.out.println("SOLUTIONS -------------------------------------------");
        for (int r = 0; r < s.length; r++) {
            boolean used = false;
            for (int c = 0; c < s[r].length; c++) {
                if (s[r][c]) {
                    System.out.println("  solutions[" + r + "][" + c + "]");
                    used = true;
                }
            }
            if (used) { System.out.println(); }
        }
    }
    public static void printD(boolean[][] d) {
        for (int r = 0; r < d.length; r++) {
            for (int c = 0; c < d[r].length; c++) {
                if (d[r][c]) {
                    System.out.println("   lastUsed[" + r + "][" + c + "]");
                }
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numCases = Integer.parseInt(in.nextLine());
        for (int caseNum = 0; caseNum < numCases; caseNum++) {
            in.nextLine(); // blank line before start of each case
            final int LENGTH = Integer.parseInt(in.nextLine()) * 100;

            // only use 1 row at a time so take out after each use
            boolean[][] lastUsed = new boolean[2][LENGTH + 1];
            lastUsed[0][0] = true; // always possible to have 0 length

            boolean[][] solutions = new boolean[202][LENGTH + 1];
            int[] carLen = new int[202];

            boolean done = false;
            int curRow = 0,
                invRow,
                curCar = 0,  // index of the current car
                     n = 0,  // number of cars loaded
                sumLen = 0,  // sum of all lengths
               lastLen = 0;  // length at port side

            while (true) {
                int currentLen = Integer.parseInt(in.nextLine());
                if (currentLen == 0) { break; } // end of this case
                if (done) { continue; } // ignore the following cars

                invRow = curRow;
                curRow = (curRow + 1) % 2;
                curCar++;
                carLen[curCar] = currentLen;
                sumLen += currentLen;

                Arrays.fill(lastUsed[curRow], false); // clean the current row
                boolean canLoad = false;
                for (int len = 0; len <= LENGTH; len++) {
                    if (!lastUsed[invRow][len]) { continue; }
                    // put car on starboard side
                    int pos = len + currentLen;
                    if ((pos <= LENGTH)) {
                        lastUsed[curRow][pos] = true;
                        solutions[curCar][pos] = false; // false for starboard
                        lastLen = pos;
                        canLoad = true;
                    }
                    // put car on port side
                    if (sumLen - len <= LENGTH) {
                        lastUsed[curRow][len] = true;
                        solutions[curCar][len] = true;  // true for port side
                        lastLen = len;
                        canLoad = true;
                    }
                }
                if (!canLoad) { done = true; }
                else {
                    System.out.println(" " + curCar + "-----------------" + curRow);
                    printD(lastUsed);
                    n++;
                }
            }
            printS(solutions);
            // iterate in the reverse direction, from last car to the first
            boolean[] backtrack = new boolean[n+1];
            for (int i = n; i > 0; i--) {
                // if last car was port
                System.out.println(lastLen);
                if (!solutions[i][lastLen]) {
                    lastLen -= carLen[i];  // decrease starboard side length
                    backtrack[i] = false;  // record starboard side
                } else if (solutions[i][lastLen]) {
                    backtrack[i] = true;
                }
            }
            // print everything out
            System.out.println(n);
            for (int i = 1; i <= n; i++) {
                if (!backtrack[i]) {
                    System.out.println(carLen[i] + ": starboard");
                } else if (backtrack[i]) {
                    System.out.println(carLen[i] + ": port");
                }
            }
            if (caseNum < numCases - 1) { System.out.println(); }
        }
    }
}