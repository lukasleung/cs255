import java.util.Scanner;

/**
 * Created by lukas on 5/6/16.
 */
public class DatabaseQuery {
    private int N;
    private int[] d1, d2;
    public DatabaseQuery() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            N = in.nextInt();
            d1 = new int[N];
            d2 = new int[N];
            for (int i = 0; i < N; i++) {
                d1[i] = in.nextInt();
            }
            for (int i = 0; i < N; i++) {
                d2[i] = in.nextInt();
            }
            System.out.println(calculateMid(0, N - 1, 0, N - 1));
        }
    }
    private void print(int l1, int h1, int l2, int h2) {
        for (int i = l1; i <= h1; i++) {
            System.out.print(d1[i] + " ");
        }
        System.out.print("| ");
        for (int i = l2; i <= h2; i++) {
            System.out.print(d2[i] + " ");
        }
        System.out.println();
    }
    private int calculateMid(int l1,int h1, int l2, int h2) {
        int mid1 = l1 + ((h1 - l1)/2),
            mid2 = l2 + ((h2 - l2)/2),
              m1 = d1[mid1],
              m2 = d2[mid2];
//        System.out.println("\n" + l1 + " + " + ((h1 - l1)) + "/2 , " + l2 + " + " + ((h2 - l2)) + "/2");
        if (m1 > m2) {
            if (l1 == h1 && l2 == h2) {
                return m2;
            }
            // take the lower half of d1, upper half of d2
            int nH1 = mid1, nL2 = mid2,
                diff1 = nH1 - l1, diff2 = h2 - nL2;
            if (diff1 == diff2) {
//                print(l1, nH1, nL2, h2);
                return calculateMid(l1, nH1, nL2, h2);
            } else if (diff1 > diff2) {
//                print(l1, nH1-1, nL2, h2);
                return calculateMid(l1, nH1-1, nL2, h2);
            } else {
//                print(l1, nH1, nL2+1, h2);
                return calculateMid(l1, nH1, nL2+1, h2);
            }
        } else {
            if (l1 == h1 && l2 == h2) {
                return m1;
            }
            // take the upper half of d1, lower half of d2
            int nL1 = mid1, nH2 = mid2,
                    diff1 = h1 - nL1, diff2 = nH2 - l2;
            if (diff1 == diff2) {
//                print(nL1, h1, l2, nH2);
                return calculateMid(nL1, h1, l2, nH2);
            } else if (diff1 > diff2) {
//                print(nL1+1, h1, l2, nH2);
                return calculateMid(nL1+1, h1, l2, nH2);
            } else {
//                print(nL1, h1, l2, nH2-1);
                return calculateMid(nL1, h1, l2, nH2-1);
            }
        }
    }
    public static void main(String[] args) {
        DatabaseQuery solve = new DatabaseQuery();
    }
}
