import java.util.Scanner;

/**
 * UVA 1709 ICPC Problem A 2015 World Finals Timelimit = 5.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac AmalgamatedArtichokes.java
 *      java AmalgamatedArtichokes < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class AmalgamatedArtichokes {
    private int p, a, b, c, d;
    public AmalgamatedArtichokes() {
        Scanner in = new Scanner(System.in);
        int round = 0;
        StringBuilder out = new StringBuilder();
        while (in.hasNextInt()) {
            if (round++ != 0) {
//                System.out.println();
                out.append("\n");
            }
//            long i = System.nanoTime();
            p = in.nextInt();
            a = in.nextInt();
            b = in.nextInt();
            c = in.nextInt();
            d = in.nextInt();
            int n = in.nextInt();
//            long read = (System.nanoTime() - i);
//            System.out.println("read: " + read);
            double val = f(1),
                    h = val,
                    l = val,
                    diff = 0;
//            i = System.nanoTime();
            for (int x = 2; x < n; x++) {
                val = f(x);
                if (val > h) { // higher than highest, reset
                    h = val;
                    l = val;
                } else if (val < l) { // lower than current lowest, check diff
                    l = val;
                    double curdiff = h - l;
                    if (curdiff > diff) {
                        diff = curdiff;
                    }
                }
            }
//            long comp = (System.nanoTime() - i);
//            System.out.println("comp: " + comp);
//            System.out.printf("%.6f", p*(diff));
            out.append((p*diff));
        }
        System.out.print(out.toString());
    }
    private double f(int x) {
        return (Math.sin(a*x+b)+Math.cos(c*x+d));
    }
    public static void main(String[] args) {
        AmalgamatedArtichokes solve = new AmalgamatedArtichokes();
    }
}