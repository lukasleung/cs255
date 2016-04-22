import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Arrays;

/**
 * UVA 10937 Blackbeard the Pirate Timelimit = 3.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac BlackBeard.java
 *      java BlackBeard < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class BlackBeard {
    // Key = row*R+col; (location in the map array)
    // Value = index in distance array, 0 => source
    private HashMap<Integer, Integer> index; // keeps track of index of treasure
    private char[][] map;
    private int[][] tresLoc, // stores indices of the treasures
                    dist;    // stores distances between treasures and source
    private int[] dc = {-1,  0, 0, 1, 1, -1, -1,  1}, // left, down, up, right
                  dr = { 0, -1, 1, 0, 1, -1,  1, -1}; // u-r, d-l, u-l, d-r
    private int R, C, numTreasures;
    public BlackBeard() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String[] dim = in.nextLine().split("\\s+");
            R = Integer.parseInt(dim[0]);  // num Row
            C = Integer.parseInt(dim[1]);  // num Columns
            if (R == 0 && C == 0) { break; }
            // build up map
            map = new char[R][C];
            numTreasures = 0;
            for (int row = 0; row < R; row++) {
                String line = in.nextLine();
                assert (line.length() == C);
                for (int col = 0; col < C; col++) {
                    char c = line.charAt(col);
                    map[row][col] = c;
                    if (c == '!') { numTreasures++; }
                }
            }
            if (numTreasures == 0) {
                System.out.println(0);
                continue;
            }
            print();
            // replace the camps with palm Trees
            replaceCamps();
            System.out.println("\n");
            print();
            // locate treasures
            if (!locateTreasures()) {
                System.out.println(-1);
                continue;
            } else {
                boolean reachable = true;
                // create distance array
                dist = new int[tresLoc.length][tresLoc.length];
                for (int i = 0; i < tresLoc.length; i++) {
                    if (!bfs(tresLoc[i][0], tresLoc[i][1])) {
                        reachable = false;
                        break;
                    }
                }
                printDist();
                // if any node un reachable => not possible problem
                if (!reachable) {
                    System.out.println(-1);
                    continue;
                }
            }
            // perform TSP on distance array.
            System.out.println(tsp());
        }
    }
    private void print() {
        for (int row = 0; row < R; row++) {
            System.out.print("");
            for (int col = 0; col < C; col++) {
                System.out.print("& " + map[row][col]);
            }
            System.out.println();
        }
    }
    private void printDist() {
        System.out.println();
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                System.out.printf("&%3d ", dist[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    // decide if in bounds
    private boolean inBounds(int r, int c) {
        return ((0 <= r) && (r < R) && (0 <= c) && (c < C));
    }
    // replace the camps with palm Trees
    private void replaceCamps() {
        for (int row = 0; row < R; row++) {
            for (int col = 0; col < C; col++) {
                char c = map[row][col];
                // water
                if (c == '~') { map[row][col] = '#'; }
                // camps
                else if (c == '*') {
                    map[row][col] = '#';
                    for (int i = 0; i < dc.length; i++) {
                        int rPrime = row + dr[i],
                            cPrime = col + dc[i];
                        // ensure in bounds, and not replacing a camp
                        if (inBounds(rPrime, cPrime) &&
                            map[rPrime][cPrime] != '*') {
                            map[rPrime][cPrime] = '#';
                        }
                    }
                }
                // keep sand and target and source as same
            }
        }
    }
    // fill treasure location and
    private boolean locateTreasures() {
        index = new HashMap(numTreasures);
        boolean start = false; // ensure there is a start
        int count = 0; // ensure still have all treasures
        tresLoc = new int[numTreasures+1][2];
        for (int row = 0; row < R; row++) {
            for (int col = 0; col < C; col++) {
                if (map[row][col] == '@') {
                    start = true;
                    tresLoc[0][0] = row;
                    tresLoc[0][1] = col;
                }
                if (map[row][col] == '!') {
                    int code = row*R+col;
                    index.put(code, ++count);
                    tresLoc[count][0] = row;
                    tresLoc[count][1] = col;
                }
            }
        }
        // returns true only if all treasures are still accessible
        //  and there is still a start
        return (start && count == numTreasures);
    }
    // preform bfs to fill in distances
    private boolean bfs(int row, int col) {
        if (!inBounds(row, col)) { return false; }
        // initialize
        boolean[][] seen = new boolean[R][C];
        Queue<Integer> queue = new LinkedList();
        int curIndex = (map[row][col] == '@') ? 0 : index.get(row*R+col),
            seenTres = 0;
        queue.add(row);
        queue.add(col);
        queue.add(0);
        // perform
        while (!queue.isEmpty()) {
            int r = queue.remove(), // row
                c = queue.remove(), // column
                l = queue.remove(); // level
            if (!seen[r][c]) {
                if (map[r][c] == '@') {
                    dist[curIndex][0] = l;
                } else if (map[r][c] == '!') {
                    int thisIndex = index.get(r * R + c);
                    dist[curIndex][thisIndex] = l;
                    seenTres++;
                }
                seen[r][c] = true;
                // check only left down up right
                for (int i = 0; i < 4; i++) {
                    int rPrime = r + dr[i],
                        cPrime = c + dc[i];
                    // ensure in bounds, able to traverse,
                    //  and have not visited already
                    if (inBounds(rPrime, cPrime) &&
                        map[rPrime][cPrime] != '#' &&
                        !seen[rPrime][cPrime]) {
                        queue.add(rPrime);
                        queue.add(cPrime);
                        queue.add(l+1);
                    }
                }
            }
        }
        return seenTres == numTreasures;
    }
    // calculate the number of elements in the bit representation
    private int numElmts(char[] bitRep) {
        int count = 0;
        for (int i = 0; i < bitRep.length; i++) {
            if (bitRep[i] == 49) { count++; }
        }
        return count;
    }
    private String cToString(char[] bitRep, int c) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bitRep.length; i++) {
            s.append(bitRep[i]);
        }
        s.append(c);
        return s.toString();
    }
    // preform Traveling Salesman
    private int tsp() {
        HashMap<String, Integer> mtsp = new HashMap();
        int length = (2 << (dist.length-2));
        // fill out table
        for (int i = length; i < (length << 1); i++) {
            char[] bitRep = Integer.toBinaryString(i).toCharArray();
            System.out.println(bitRep);
            for (int c = 1; c < bitRep.length; c++) {
                if (bitRep[c] == 49) { // if it is a 1
                    bitRep[c] = '0';
                    if (numElmts(bitRep) == 1) {
//                        System.out.println(cToString(bitRep, c));
                        mtsp.put(cToString(bitRep, c), dist[0][c]);
                    } else {
                        int bestVal = Integer.MAX_VALUE;
                        for (int k = 1; k < bitRep.length; k++) {
                            if (bitRep[k] == 49) { // if it is a 1
                                bitRep[k] = '0';
                                int val = mtsp.get(cToString(bitRep, k))
                                            + dist[k][c];
                                if (val < bestVal) {
                                    bestVal = val;
                                }
                                bitRep[k] = '1';
                            }
                        }
                        mtsp.put(cToString(bitRep, c), bestVal);
                    }
                    bitRep[c] = '1';
                }
            }
        }
        char[] lastLevel = new char[dist.length];
        Arrays.fill(lastLevel, '1');
        int best = Integer.MAX_VALUE;
        for (int i = 1; i < lastLevel.length; i++) {
            lastLevel[i] = '0';
            int val = mtsp.get(cToString(lastLevel, i)) + dist[i][0];
            if (val < best) {
                best = val;
            }
            lastLevel[i] = '1';
        }
        mtsp.put(cToString(lastLevel, 0), best);
        return best;
    }
    public static void main(String[] args) {
        BlackBeard pirate = new BlackBeard();
    }
}