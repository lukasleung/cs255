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
        while (in.hasNextLine()) {

            String[] line = in.nextLine().split("\\s+");
            numHoles = Integer.parseInt(line[0]);
            numSlices = Integer.parseInt(line[1]);
            // fill holes
            v = DIM*DIM*DIM;
            System.out.println("v: " + v);
            holes = new double[numHoles][3];
            for (int i = 0; i < numHoles; i++) {
                line = in.nextLine().split("\\s+");
                int r = Integer.parseInt(line[0]),
                    z = Integer.parseInt(line[3]);
                holes[i][0] = z/1000;
                holes[i][1] = r/1000;
                // holes[i][2] = volume(z - r, z + r, i); // problem
                // System.out.println(holes[i][2]);
                System.out.print("yolo v: " + v);
                v = v - volume(z - r, z + r, i);// holes[i][2];
                System.out.println(" = " + v);
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
            // binarySearch();
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
        // if (l == r && u == r && holes[sphere][2] != Double.NaN) { return holes[sphere][2]; }
        // System.out.println(l + " " + r);
        double vLeft = 0.0, vRight = 0.0,  // value of left and right hemispheres
                r1, r2,  // radius of larger and smaller radii,
                dist, height, temp;          // distance from center and height of band
        // Compute left side if it is there
        if (cLeft) {
            dist = cRight ? 0.0 : u; // if there is a right side, start at the middle
            height = l - dist;
            temp = r*r-dist*dist;
            r1 = Math.sqrt(temp);
            r2 = Math.sqrt(temp-2*dist*height-height*height);
            vLeft = PI*height*(r1*r1 + r2*r2 + height*height/3);
        }
        // Compute right side if it is there
        if (cRight) {
            dist = cLeft ? 0.0 : l;  // if there is a left side, start at the middle
            height = u - dist;
            temp = r*r-dist*dist;
            System.out.print("RIGHT: dist = " + dist + " height = " + height + " temp = " + temp);
            r1 = (temp < 0) ? 0.0 : Math.sqrt(temp);
            r2 = Math.sqrt(temp-2*dist*height-height*height);
            r1 = (r1 == Double.NaN) ? 0.0 : r1;
            r2 = (r2 == Double.NaN) ? 0.0 : r2;
            System.out.print(" r1 = " + r1 + " r2 = " + r2);
            vRight = PI*height*(r1*r1 + r2*r2 + height*height/3);
        }
        if (vLeft < 0 || vRight < 0) { System.out.println("I fucked up"); }
        System.out.print(" - (" + vLeft + " + " + vRight + ")");
        return vLeft + vRight;
    }
    // calculate the volume of all spheres contained in the range [l, u]
    private double allVol(double a, double b) {
        double val = DIM*DIM*(b - a);
        double r = 0;
        for (int i = 0; i < holes.length; i++) {
            if (holes[i][0] - holes[i][1] > b) { break; }    // z - r > b
            if (holes[i][0] + holes[i][1] < a) { continue; } // z + r < a
            // intersects with sphere, take out part of sphere intersecting with band a, b
            val -= volume(a, b, i);
            r = holes[i][1];
        }
//        System.out.println(val);
//        System.out.println(val + Math.PI*4/3*r*r*r);
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