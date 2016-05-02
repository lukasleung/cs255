import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

/**
 * UVA 1575 Factors Timelimit = 3.000 seconds
 * Completed testing in: 0.460 seconds on 2016-05-01 23:19:56
 * To Run type:
 *      javac Factors.java
 *      java Factors < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung, Parker Watts, Trung Ngo
 * @version 4.0
 */
public class Factors {
    private static int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
                                    41, 43, 47, 53, 59, 61, 67, 71 };   // 20 numbers
    private static final BigInteger MAX = BigInteger.valueOf((long) Math.pow(2, 63));
    private int[] xSequence = new int[20];    // stores corresponding
                                              // exponents of the primes
    private int anchor = 0, inc = 0;
    private HashMap<BigInteger, BigInteger> table;  // (n-value, k-value)
    // builds the table of precomputed values, then just ask and print
    //   out the corresponding values.
    public Factors() {
        buildLookUp();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            BigInteger key = new BigInteger(in.nextLine());
            System.out.println(key.toString() + " " + table.get(key).toString());
        }
    }

    private void print() {
        for (int i = 0; i < xSequence.length; i++) {
            System.out.printf("%3d ", xSequence[i]);
        }
        System.out.println();
    }

    // calculate (x_1 + x_2 + ... + x_j)! / (x_1)!
    private BigInteger calcTop() {
        int sum = 0;
        BigInteger ret = BigInteger.ONE;
        for (int i = 0; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            sum += xSequence[i];
        }
        for (int i = xSequence[0]+1; i <= sum; i++) {
            ret = ret.multiply(BigInteger.valueOf(i));
        }
        return ret;
    }
    // calculates n!
    private BigInteger fact(int n) {
        BigInteger fac = BigInteger.ONE;
        for (int i = 2; i <= n; i++) { fac = fac.multiply(BigInteger.valueOf(i)); }
        return fac;
    }
    // calculates (x_2)! + ... + (x_j)!
    private BigInteger calcBot() {
        BigInteger ret = BigInteger.ONE;
        for (int i = 1; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            ret = ret.multiply(fact(xSequence[i]));
        }
        return ret;
    }
    // calculates product( p_i^{x_i} )
    private BigInteger calcVal() {
        BigInteger ret = BigInteger.ONE;
        for (int i = 0; i < xSequence.length; i++) {
            if (xSequence[i] == 0) { break; }
            ret = ret.multiply(BigInteger.valueOf((long) Math.pow(primes[i], xSequence[i])));
        }
        return ret;
    }
    public void zeroAfter() {
        for (int i = anchor + 1; i < xSequence.length; i++) {
            xSequence[i] = 0;
        }
    }
    // gets the next value if prev was too big
    private void reset() {
        int prev = anchor-1, next = anchor + 1;
        // move anchor to the right place
        if (anchor != inc && next != xSequence.length && xSequence[anchor] > xSequence[next]) {
            anchor++;
        } else {
            while ((prev >= 0 && xSequence[prev] == xSequence[anchor])) {
                anchor--;
                prev--;
            }
        }
        // zero after the anchor and increment the anchor
        zeroAfter();
        xSequence[anchor]++;
        inc = anchor;
    }
    // build the n, k hashmap
    private void buildLookUp() {
        table = new HashMap<BigInteger, BigInteger>();
        // table.put(1, 2);    // corner case
        xSequence[0] = 1;
        BigInteger top, bot, key, val;
        // need to compute from 2^61 * 3^1
        while (true) {
//            print();
            if (xSequence[0] == 63) { break; }
            val = calcVal();
            if (val.compareTo(MAX) > 0) {
                reset();
                continue;
            }
            top = calcTop();
            bot = calcBot();
            key = top.divide(bot);
            if (key.compareTo(MAX) > 0) {
                reset();
                continue;
            }
//            System.out.println("\t added");
            if (table.containsKey(key)) {
                BigInteger stored = table.get(key);
                if (val.compareTo(stored) < 0) {
                    table.put(key, val);
                }
            } else {
                table.put(key, val);
            }
            if (inc+1 < xSequence.length) {
                xSequence[++inc]++;
            } else {
                reset();
            }

        }

    }
    public static void main(String[] args) {
        Factors solve = new Factors();
    }
}