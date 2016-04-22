import java.util.HashMap;
import java.util.Scanner;

/**
 * UVA 544 Heavy Cargo Timelimit = 3.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac HeavyCargo.java
 *      java HeavyCargo < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 1.0
 */
public class HeavyCargo {
    public static void printLoads(int[][] load) {
        for (int i = 0; i < load.length; i++) {
            for (int j = 0; j < load[i].length; j++) {
                System.out.printf("%5d ", load[i][j]);
            }
            System.out.println();
        }
    }
    public static void printCities(HashMap<String, Integer> cityIndex,
                                   String[] cities) {
        for (String city : cities) {
            System.out.println(city + ": " + cityIndex.get(city));
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int scenario = 1;
        while (true) {
            String[] line = in.nextLine().split("\\s+");
            int n = Integer.parseInt(line[0]),  // num cities
                r = Integer.parseInt(line[1]);  // num road segments
            if (n == 0 && r == 0) {
                break;
            }
            int[][] load  = new int[n][n];
            String[] cities = new String[n];
            HashMap<String, Integer> cityIndex = new HashMap<>();
            // fill load table
            for (int i = 0, lastCity = 0; i < r; i++) {
                // [0] => city1, [1] => city2, [2] => load
                line = in.nextLine().split("\\s+");
                // insert cities into the hashmap if they don't exist
                if (!cityIndex.containsKey(line[0])) {
                    cities[lastCity] = line[0];
                    cityIndex.put(line[0], lastCity++);
                }
                if (!cityIndex.containsKey(line[1])) {
                    cities[lastCity] = line[1];
                    cityIndex.put(line[1], lastCity++);
                }
                // get the load
                int curLoad = Integer.parseInt(line[2]),
                          a = cityIndex.get(line[0]),
                          b = cityIndex.get(line[1]);
                load[a][b] = curLoad;
                load[b][a] = curLoad;
            }
            printCities(cityIndex, cities);
            printLoads(load);
            // Perform additions
            int[] curSet = new int[n];
            int[][] l_i = new int[n][n];
            for (int i = 0; i < n; i++) {
                // get row i, col i
                for (int k = 0; k < n; k++) {
                    curSet[k] = load [i][k];
                }
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        if (row == col) { continue; }
                        l_i[row][col] = Math.min(curSet[row], curSet[col]);
                    }
                }
                System.out.println("-----------------------");
                System.out.println("l_" + (i+1));
                printLoads(l_i);
                System.out.println("-----------------------");
                for (int row = 0; row < n; row++) {
                    for (int col = 0; col < n; col++) {
                        if (row == col) { continue; }
                        load[row][col] = Math.max(load[row][col], l_i[row][col]);
                    }
                }
                System.out.println("load after step " + (i+1));
                printLoads(load);
            }
            line = in.nextLine().split("\\s+");
            System.out.println("Connect " + line[0] + " and " + line[1] + "\n     => "
                    + cityIndex.get(line[0]) + " and " + cityIndex.get(line[1]));
            System.out.println("Scenario #" + scenario++);
            System.out.println(load[cityIndex.get(line[0])][cityIndex.get(line[1])]
                    + " tons\n");
        }
    }
}
