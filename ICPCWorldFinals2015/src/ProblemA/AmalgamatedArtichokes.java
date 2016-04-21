import java.util.Scanner;
/**
 * UVA 1709 ICPC Problem A 2015 World Finals Timelimit = 5.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac AmalgamatedArtichokes.java
 *      java AmalgamatedArtichokes < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 3.0
 */
public class AmalgamatedArtichokes {
    private int p, a, b, c, d;
    private double pi = 2*Math.PI;
    public AmalgamatedArtichokes() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            p = in.nextInt();
            a = in.nextInt();
            b = in.nextInt();
            c = in.nextInt();
            d = in.nextInt();
            int n = in.nextInt();
            double val = f(1),
                    h = val,
                    l = val,
                    diff = 0;
            for (int x = 2; x < n; x++) {
                val = f(x);
                if (val > h) { // higher than highest, reset
                    h = val;
                    l = val;
                } else if (val < l) { // lower than current lowest, check diff
                    l = val;
                    double curdiff = h - l;
                    if (curdiff > diff) { diff = curdiff; }
                }
            }
            System.out.printf("%.6f\n", p*(diff));
        }
    }
    private double f(int x) {
        double ab = (a*x+b) % pi,
               cd = (c*x+d) % pi;
        return (Math.sin(ab)+Math.cos(cd));
    }
    public static void main(String[] args) {
        AmalgamatedArtichokes solve = new AmalgamatedArtichokes();
    }
}