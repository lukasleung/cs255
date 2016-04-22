import java.util.Scanner;

/**
 * UVA 108 Maximum Sum Timelimit = 3.000 seconds
 * Completed testing in: 0.152 seconds on 2016-03-19 19:51:07
 * To Run type:
 *      javac MaximumSum
 *      java MaximumSum < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class MaximumSum {
    public static int maxSum(int[][] map, int N) {
        int max = -128;
        // visit every element
        for (int row = 1; row <= N; row++) {
            for (int rowOff = 0; rowOff <= (N - row); rowOff++) {
                // going to next col
                int sum = 0;
                for (int col = 0; col < N; col++) {
                    int colSum = map[row + rowOff][col] - map[rowOff][col];
                    // make sure that it is not 0 at this point
                    if (sum >= 0) {
                        sum += colSum;
                    } else {
                        sum = colSum;
                    }
                    if (sum > max) {
                        max = sum;
                    }
                }
            }
        }
        return max;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int N = in.nextInt();
            int[][] map = new int[N+1][N+1];
            for (int r = 1; r <= N; r++) {
                for (int c = 0; c < N; c++) {
                    map[r][c] = in.nextInt() + map[r - 1][c];
                }
            }
            System.out.println(maxSum(map, N));
        }
    }
}