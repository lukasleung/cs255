import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
/**
 * UVA 1712 ICPC Problem D 2015 World Finals Timelimit = 3.000 seconds
 * Completed testing in: _ seconds on _
 * To Run type:
 *      javac CuttingCheese.java
 *      java CuttingCheese < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 3.0
 */
public class CuttingCheese {
    private final double PI = Math.PI/2, DIM = 100000, EPSILON = 0.000001;
    private double v, goal; // total volume, and what each segment must be
    private int numHoles, numSlices;
    private double[][] holes; // ith sphere (z, r)
    public CuttingCheese() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {

            String[] line = in.nextLine().split("\\s+");
            numHoles = Integer.parseInt(line[0]);
            numSlices = Integer.parseInt(line[1]);
            // fill holes
            v = DIM*DIM*DIM;
            System.out.println("v: " + v);
            holes = new double[numHoles][2];
            for (int i = 0; i < numHoles; i++) {
                line = in.nextLine().split("\\s+");
                int r = Integer.parseInt(line[0]),
                    z = Integer.parseInt(line[3]);
                holes[i][0] = z;
                holes[i][1] = r;
                // holes[i][2] = volume(z - r, z + r, i); // problem
                // System.out.println(holes[i][2]);
                System.out.println("yolo v: " + v);
                v = v - volInRange(z - r, z + r, i);
                System.out.println(" = " + v);
            }
            if (numSlices == 1) {
                System.out.printf("0.000000\n");
                continue;
            }
            goal = v / numSlices;
            System.out.println("v: " + v + ", s:" + goal);
            Arrays.sort(holes, new Comparator<double[]>() {
                @Override
                public int compare(double[] o1, double[] o2) {
                    return (int) ((o1[0] - o1[1]) - (o2[0] - o2[1]));
                }
            });
                    System.out.println("____________________");
            print();
            System.out.println("____________________");
            binarySearch();
        }
    }
    private void print() {
        for (int i = 0; i < holes.length; i++) {
            System.out.println(holes[i][0] + ": " + holes[i][1]);
        }
    }
    // calculates a spherical segment given d and h and R
    private double calcSphericalSegment(double d, double h, double R) {
        double r_1, r_2, tmp;
        tmp = (d+h);  // R^2 - d^2
        r_1 = Math.sqrt((R*R) - (d*d));
        r_2 = Math.sqrt((R*R) - tmp*tmp); // R^2 - d^2 - 2dh - h^2
        return PI*h*((r_1*r_1) + (r_2*r_2) + (h*h)/3);
    }
    // calculates the volume of a sphere contained in the range [a, b]
    //  (guaranteed to be in these bounds before here)
    //  http://mathworld.wolfram.com/SphericalSegment.html
    //  https://en.wikipedia.org/wiki/Spherical_segment
    private double volInRange(double a, double b, int sphere) {
        double R = holes[sphere][1],
             mid = holes[sphere][0];
        double coordLeft = mid - R,
              coordRight = mid + R;
        // [a,b] does not contain sphere
        if (a >= coordRight || b <= coordLeft) { return 0.0; }
        // [a,b] fully contains sphere
        if (a <= coordLeft && b >= coordRight) { return (4/3*Math.PI)*(R*R*R); }
        // only a portion of the sphere is contained, calculate that
        boolean inLeft = false, inRight = false;
        double volLeft = 0.0, volRight = 0.0, d, h, coordD;
        if (a < mid) { inLeft = true; }
        if (b > mid) { inRight = true; }
        if (inLeft) {
            d = (inRight) ? 0.0 : mid-b;
            coordD = (inRight) ? mid : b;
            h = (a <= coordLeft) ? coordD-coordLeft : coordD-a;
            System.out.println("d: " + d + ", h: " + h + ", R: " + R);
            volLeft = calcSphericalSegment(d, h, R);
        }
        if (inRight) {
            d = (inLeft) ? 0.0 : a-mid;
            coordD = (inLeft) ? mid : a;
            h = (b >= coordRight) ? coordRight-coordD : b-coordD;
            volRight = calcSphericalSegment(d, h, R);
        }
        System.out.println(" Left: " + volLeft + ", Right: " + volRight);
        return volLeft + volRight;
    }
    // calculate the volume of all spheres contained in the range [l, u]
    private double allVol(double a, double b) {
        double val = DIM*DIM*(b - a);
//        System.out.println("Pre : " + val);
        double r = 0;
        for (int i = 0; i < holes.length; i++) {
            if (holes[i][0] - holes[i][1] > b) { break; }    // z - r > b
            if (holes[i][0] + holes[i][1] < a) { continue; } // z + r < a
            // intersects with sphere, take out part of sphere intersecting with band a, b
            val -= volInRange(a, b, i);
            r += holes[i][1];
        }
//        System.out.println("Post: " + val);
//        System.out.println("Redo: " + val + Math.PI*4/3*r*r*r);
        return val;
    }
    // binary search
    private void binarySearch() {
        double l = 0, h = DIM, cut, last = 0.0;
        int slicePerformed = 1;
        while (true) {
            cut = (h + l)/2;
            double diff = goal - allVol(last, cut);
            if (Double.isNaN(diff)) {
                System.out.println("oops");
                return;
            }
            if (Math.abs(diff) < EPSILON) { // got it
                System.out.printf("%.6f\n", (cut - last));
                if (slicePerformed++ == numSlices) { break; }
                last = cut;
                // reset
                l = last; h = DIM;
            } else if (diff > 0) {  // too light
                l = cut;
            }
            else if (diff < 0) {    // too heavy
                h = cut;
            }
        }
        System.out.printf("%.6f\n", (DIM - last));
    }
    public static void main(String[] args) {
        CuttingCheese solve = new CuttingCheese();
    }
}