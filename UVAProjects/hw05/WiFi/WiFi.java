import java.util.Arrays;
import java.util.Scanner;
/**
 * UVA 11516 WiFi Timelimit = 1.000 seconds
 * Completed ting in:  seconds on
 * To Run type:
 *      javac WiFi
 *      java WiFi < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 1.0
 */
public class WiFi {
    public static boolean inRange(int mid, int[] houses, int m, int n) {
        int oob = 1;
        int range = houses[0] + mid;
        for (int i = 0; i < m; i++) {
            System.out.println("\tRange: " + range +
                    "\n\thouses[" + i + "]: " + houses[i]);
            if (range < houses[i]) {
                range = houses[i] + mid;
                oob++;
            }
        }
        System.out.println(oob + " " + (oob <= n));
        return oob <= n;
    }
    // main solving
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        assert (in.hasNext()); // ensure not empty file
        // begin solving
        int numCases = Integer.parseInt(in.nextLine());
        for (int c = 0; c < numCases; c++) {
            String[] given = in.nextLine().split("\\s+");
            int n = Integer.parseInt(given[0]);  // number of routers
            int m = Integer.parseInt(given[1]);  // number of customers
            // store the locations of each of the houses
            int[] houses = new int[m];
            for (int i = 0; i < m; i++) {
                // in order to get the correct estimation, use the
                houses[i] = Integer.parseInt(in.nextLine()) * 10;
            }
            // if more or equal routers, then this is a trivial problem.
            if (n >= m) {
                System.out.println("0.0");
                continue;
            }
            Arrays.sort(houses);    // setting up for the binary search.
            int l = 0;
            int h = houses[houses.length - 1];
            // do binary search
            int mid = (l + h) / 2;
            while ((h - l) > 1) {
                System.out.println("  highter: " + h +
                "\n  mid: " + mid + "\n  lower: " + l);
                // if we have used all of the routers, use lower
                if (inRange(mid * 2, houses, m, n)) { h = mid; }
                else { l = mid; }
                mid = (l + h) / 2;
                System.out.println("=> new mid: " + mid + "\n");
            }
            double formatMe = (double) h;
            System.out.printf("%.1f\n", formatMe/10);
            System.out.println("-----------------------------------");
        }
    }
}