import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * UVA 1575 Factors Timelimit = 3.000 seconds
 * Completed testing in:  seconds on
 * To Run type:
 *      javac Factors.java
 *      java Factors < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung, Parker
 * @version 1.0
 */
public class Factors {
    private static int[] primes = { 2, 3, 5, 7, 11, 13, 17,
                                    19, 23, 29, 31, 37, 41, 43,
                                    47, 53, 59, 61, 67, 71 };   // 20 numbers
    private int[] xSequence = new int[20];    // stores corresponding
                                              // exponents of the primes
    private HashMap<Integer, Integer> table;  // (n-value, k-value)
    // builds the table of precomputed values, then just ask and print
    //   out the corresponding values.
    public Factors() {
        buildLookUp();
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int key = in.nextInt();
            System.out.println(key + " " + table.get(key));
        }
    }
    // calculate (x_1 + x_2 + ... + x_j)! / (x_1)!
    private int calcTop() {
        int sum = 0, ret = 1;
        for (int i = 0; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            sum += xSequence[i];
        }
        System.out.println(sum);
        for (int i = xSequence[0]+1; i <= sum; i++) {
            ret *= i;
        }
        return ret;
    }
    // calculates n!
    private int fact(int n) {
        int fac = 1;
        for (int i = 2; i <= n; i++) { fac *= i; }
        return fac;
    }
    // calculates (x_2)! + ... + (x_j)!
    private int calcBot() {
        int ret = 1;
        for (int i = 1; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            ret *= fact(xSequence[i]);
        }
        return ret;
    }
    // calculates product( p_i^{x_i} )
    private int calcVal() {
        int ret = 1;
        for (int i = 0; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            ret *= Math.pow(primes[i], xSequence[i]);
        }
        return ret;
    }
    private void buildLookUp() {
        table = new HashMap<Integer, Integer>();
        table.put(1, 2);    // corner case
        Arrays.fill(xSequence, 1);
        xSequence[0] = 4;
        xSequence[1] = 2;
        xSequence[2] = 1;
        int top, bot, key, val;
        // need to compute from 2^61 * 3^1
        for (int i = 0; i < primes.length; i++) {
            val = calcVal();
            if (val >= (1 << 63)) {
                System.out.println(val + " is too big!");
                continue;
            }
            top = calcTop();
            bot = calcBot();
            key = top/bot;
            if (key >= (1 << 63)) {
                System.out.println(key + " is too big!");
                continue;
            }
            table.put(key, val);
            System.out.println(top + "/" + bot + " = " + key + " with " + val);
            // permute xSequence
            break;
        }

    }
    public static void main(String[] args) {
        Factors solve = new Factors();
    }
}