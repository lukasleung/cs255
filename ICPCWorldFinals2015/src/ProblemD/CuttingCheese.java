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
    private final double PI = Math.PI/2, DIM = 100, EPSILON = 0.000001;
    private double v, goal; // total volume, and what each segment must be
    private int numHoles, numSlices;
    private double[][] holes; // ith sphere (z, r)
    public CuttingCheese() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            v = DIM*DIM*DIM;
            System.out.println(v);
            numHoles = in.nextInt();
            numSlices = in.nextInt();
            holes = new double[numHoles][3];
            for (int i = 0; i < numHoles; i++) {
                int r = in.nextInt();
                in.nextInt();
                in.nextInt(); // x,y
                int z = in.nextInt();
                holes[i][0] = z/1000;
                holes[i][1] = r/1000;
                holes[i][2] = volume(z - r, z + r, i);
                v -= holes[i][2];
            }
            if (numSlices == 1) {
                System.out.printf("0.000000");
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
    // calculates the volume of a sphere contained in the range [a, b]
    //  (guaranteed to be in these bounds before here)
    //  http://mathworld.wolfram.com/SphericalSegment.html
    //  https://en.wikipedia.org/wiki/Spherical_segment
    private double volume(double a, double b, int sphere) {
        double z = holes[sphere][0],
               r = holes[sphere][1],
               l, u;     // distance of lower and upper bands from center
        boolean cLeft, cRight; // contained in Left and Right
        // set lower bound
        if (a < z-r) {
            l = r;
            cLeft = true;
        }
        else {   // a >= z - r
            l = Math.abs(a - z);
            cLeft = (a - z < 0);
        }
        // set upper bound
        if (b > z+r) {
            u = r;
            cRight = true;
        }
        else {  // b <= z + r
            u = Math.abs(b - z);
            cRight = (b - z > 0);
        }
        // if whole sphere, return cached
        if (l == -r && u == r) { return holes[sphere][2]; }
        double vLeft = 0.0, vRight = 0.0,  // value of left and right hemispheres
                r1, r2,  // radius of larger and smaller radii,
                dist, height;          // distance from center and height of band
        // Compute left side if it is there
        if (cLeft) {
            dist = u;
            if (cRight) {
                dist = 0.0;
            }
            height = l - dist;
            double temp = r*r-dist*dist;
            r1 = Math.sqrt(temp);
            r2 = Math.sqrt(temp-2*dist*height-height*height);
            vLeft = PI*height*(r1*r1 + r2*r2 + height/3);
        }
        // Compute right side if it is there
        if (cRight) {
            dist = l;
            if (cLeft) {
                dist = 0.0;
            }
            height = u - dist;
            double temp = r*r-dist*dist;
            r1 = Math.sqrt(temp);
            r2 = Math.sqrt(temp-2*dist*height-height*height);
            vRight = PI*height*(r1*r1 + r2*r2 + height/3);
        }

        return vLeft + vRight;
    }
    // calculate the volume of all spheres contained in the range [z1, z2]
    private double allVol(double l, double h) {
        double val = DIM*DIM*(h-l);
        for (int i = 0; i < holes.length; i++) {
            if (holes[i][0] - holes[i][1] > h) { break; }    // z - r > h
            if (holes[i][0] + holes[i][1] < l) { continue; } // z + r < l
            // intersects with sphere
            val -= volume(l, h, i);
        }
        System.out.println(val);
        return val;
    }
    // binary search
    private void binarySearch() {
        double l = 0, h = DIM, cut, last = 0.0;
        int slicePerformed = 1;
        while (true) {
            cut = (h + l)/2;
            double diff = goal - allVol(last, cut);
            if (Math.abs(diff) < EPSILON) { // got it
                System.out.printf("%.6f\n", (cut - last));
                last = cut;
                // reset
                l = last; h = DIM;
                slicePerformed++;
                if (slicePerformed == numSlices) { break; }
            } else if (diff > 0) {  // undershot
                l = cut;
            }
            else if (diff < 0) {    // over shot
                h = cut;
            }
            break;
        }
        // System.out.printf("%.6f\n", (DIM - last));
    }
    public static void main(String[] args) {
        CuttingCheese solve = new CuttingCheese();
    }
}