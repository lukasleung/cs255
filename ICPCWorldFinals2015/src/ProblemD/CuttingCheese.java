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
    private final double PI = Math.PI/3, DIM = 100*1000, EPSILON = 0.0000005;
    private double v = DIM*DIM*DIM, goal; // total volume, and what each segment must be
    private int numHoles, numSlices;
    private int[][] holes;
    public CuttingCheese() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            numHoles = in.nextInt();
            numSlices = in.nextInt();
            holes = new int[numHoles][2];
            for (int i = 0; i < numHoles; i++) {
                int r = in.nextInt();
                in.nextInt();
                in.nextInt(); // x,y
                int z = in.nextInt();
                holes[i][0] = z;
                holes[i][1] = r;
                v -= volume(z - r, z + r, i);
            }
            goal = v / numSlices;
            Arrays.sort(holes, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return o1[0] - o2[0];
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
    // calculates the volume of a sphere contained in the range [z1, z2]
    private double volume(double z1, double z2, int sphere) {
        assert(z1 < z2);
        int z = holes[sphere][0],
            r = holes[sphere][1];
        double h = 0, l = 0;
        if ((z+r) <= z1 || (z-r) >= z2) { // not in specified range
            return 0;
        } else { // in range
            // set lower bound
            if (z1 < z-r) { l = -r; }
            else { l = z1 - z; }
            // set upper bound
            if (z2 > z+r) { h = r; }
            else { h = z2 - z; }
        }
        int rprime = 3*r*r;
        return PI*(rprime*h - h*h*h - rprime*l + l*l*l);
    }
    // calculate the volume of all spheres contained in the range [z1, z2]
    private double allVol(double z1, double z2) {
        double val = DIM*DIM*(z2-z1);
        for (int i = 0; i < holes.length; i++) {
            if (holes[i][0] - holes[i][1] > z2) { break; }
            if (holes[i][0] + holes[i][1] < z1) { continue; }
        }
        return val;
    }
    // binary search
    private void binarySearch() {
        double l = 0, h = DIM, cut, last = 0.0;
        int slicePerformed = 1;
        while (slicePerformed != numSlices) {
            cut = (h + l)/2;
            double diff = goal - allVol(last, cut);
            if (Math.abs(diff) < EPSILON) { // got it
                System.out.printf("%.6f\n", (cut - last));
                last = cut;
            } else if (diff > 0) {  // undershot
                l = cut;
            }
            else if (diff < 0) {    // over shot
                h = cut;
            }
        }
    }
    public static void main(String[] args) {
        CuttingCheese solve = new CuttingCheese();
    }
}
