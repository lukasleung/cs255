import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * UVA 10937 Blackbeard the Pirate Timelimit = 3.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac BlackBeard.java
 *      java BlackBeard < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung, Parker
 * @version 1.0
 */
public class Factors {
    private static int[] primes = { 2, 3, 5, 7, 11, 13, 17,
                                    19, 23, 29, 31, 37, 41, 43,
                                    47, 53, 59, 61, 67, 71 };
    private HashMap<Integer, Integer> table;  // (n-value, k-value)
    private int[] xSequence = new int[20];
    public Factors() {
        buildLookUp();
        Scanner in = new Scanner(System.in);
    }
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
    private int fact(int n) {
        int fac = 1;
        for (int i = 2; i <= n; i++) {
            fac *= i;
        }
        return fac;
    }
    private int calcBot() {
        int ret = 1;
        for (int i = 1; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            ret *= fact(xSequence[i]);
        }
        return ret;
    }
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
//        xSequence[0] = 4;
//        xSequence[1] = 2;
//        xSequence[2] = 1;
        int top, bot, key, val;
        for (int i = 0; i < primes.length; i++) {
            top = calcTop();
            bot = calcBot();
            key = top/bot;
            val = calcVal();
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