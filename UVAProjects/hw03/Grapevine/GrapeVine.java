import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * UVA 12192 Grapevine Timelimit = 3.00 seconds
 * Completed testing in: 1.118 seconds on 2016-02-26 02:19:02
 * Note: to see a walk-through uncomment all which have "//"
 *          on the left most side of the page.
 * To Run type:
 *      javac GrapeVine
 *      java GrapeVine < input.txt > printToThis.txt
 * @author Lukas Leung
 */
public class GrapeVine
{
    /**
     * This method will get the lowest valid index for a designated row
     *  on the topological map using the params described below.
     * @param map  = the topological map we have stored
     * @param row  = the current row we are looking at
     * @param size = the index of the last column [2D array]
     * @param lb   = the lower bound as seen by the computer
     * @return the index of the lower bound index of the row
     */
    public static int lowerBound(int[] map, int row, int size, int lb)
    {
        int lower = 0,
            higher = size,
            mid,
            answer = -1;
        // Adapted implementation of binary search
        while (lower <= higher)
        {
            mid = lower + (higher - lower) / 2;
            // determine if the middle value is within the lowerbound
            if (map[twoToOne(row, mid, size+1)] >= lb)
            {
                answer = mid;       // we know valid here
                higher = mid - 1;   // check left of mid
            }
            else
            {
                lower = mid + 1;    // advance lower
            }
        }
        return answer;
    }
    // Determines if value is Out Of Bounds
    public static boolean oob(int value)
    {
        return (value <= 0 || value > 500);
    }
    // Conversions between 2d to 1d coordinates
    public static int twoToOne(int row, int col, int numCol)
    {
        return row*numCol + col;
    }
    // main functionality
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        StringBuilder stringBuilder;
        String[] splitLine;
        int N, // numRow
            M, // numCol
            L, // lowerbound
            U, // upperbound
            Q; // numQueries
        while (line != null)
        {

            splitLine = line.split("\\s+");   // split by spaces
            N = Integer.parseInt(splitLine[0]);
            M = Integer.parseInt(splitLine[1]);
            // ensure within bounds
            if (oob(N) || oob(M)) { break; }
            // fill in topology of map
            int[] map = new int[N*M];
            for (int i = 0, k = 0; i < N; i++)
            {
                splitLine = reader.readLine().split("\\s+");
                // printRow(splitLine);
                for (int j = 0; j < M && k < M*N; j++, k++)
                {
                    map[k] = Integer.parseInt(splitLine[j]);
                }
            }
            // get number of upper and lower limit cases
            Q = Integer.parseInt(reader.readLine());
            stringBuilder = new StringBuilder();
            while (Q != 0)
            {
                splitLine = reader.readLine().split("\\s+");
                L = Integer.parseInt(splitLine[0]);
                U = Integer.parseInt(splitLine[1]);
//                System.out.println("--------------------------------\n" +
//                        "Case: Q = " + Q + ", L = " + L + ", U = " + U +
//                        "\n--------------------------------");
                int max = 0;
                // for each row...
                for (int i = 0; i < N && (N - i + 1) > max; i++)
                {
                    // get the lower index
                    int lowerIndex = lowerBound(map, i, (M-1), L);
                    // check validity
//                    System.out.println("\ti = " + i + "\n\t |  lb= " + lowerIndex);
                    if (lowerIndex == -1) { continue; }
                    for (int j = max; j < M; j++)
                    {
//                        System.out.println("\t |\tj = " + j +
//                                 "\n\t |\t | max= " + max);
//                        int n = i + j,
//                                m = lowerIndex + j;
//                        System.out.println("\t |\t |\t | n = " + n +
//                                "\n\t |\t |\t | m = " + m);
                        // if [(out of bounds) or (out of bounds) or (too elevated)]
                        if (i + j >= N ||
                            lowerIndex + j >= M ||
                            map[twoToOne((i + j), (lowerIndex + j), M)] > U) // r,c
                        {
//                            System.out.println("\t |\t |  - breaking");
                            break;
                        }
                        if (j + 1 > max)
                        {
//                            System.out.println("\t |\t |  - advancing");
                            max = j + 1;
                        }
                    }
                }
//                System.out.println("--------------------------------" +
//                        "\n\t max: " + max + "\n--------------------------------");
                stringBuilder.append(max + "\n");
                Q--;
            }
//            System.out.println("--------------------------------");
            stringBuilder.append("-\n");
            System.out.print(stringBuilder.toString());
//            System.out.println("--------------------------------\n" +
//                    "\t   End Case\n--------------------------------");
            line = reader.readLine();
        } // end while
    }
}