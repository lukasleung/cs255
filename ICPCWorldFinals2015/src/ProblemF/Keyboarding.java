import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
/**
 * UVA 1714 ICPC Problem F 2015 World Finals Timelimit = 30.000 seconds
 * Completed testing in: seconds on
 * To Run type:
 *      javac Keyboarding.java
 *      java Keyboarding < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 5.0
 */
public class Keyboarding {
    private int[][] adj, graph;
    private char[][] keyboard;
    private int[] dr = {-1, 1, 0,  0},
                  dc = { 0, 0, 1, -1},
             inverse;
    private int R, C, size;
    private HashMap<Character, String> indices;
    private HashMap<Character, Integer> num;

    public Keyboarding() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            init(line);
            int index = 0;
            for (int r = 0; r < R; r++) {
                line = in.nextLine();
                for (int c = 0; c < C; c++) {
                    char letter = line.charAt(c);
                    if (indices.containsKey(letter)) {
                        String s = indices.get(letter),
                              ns = s.concat(" " + index);
                        indices.put(letter, ns);
//                        System.out.println(indices.get(letter));
                        int newNum = num.get(letter) + 1;
                        num.put(letter, newNum);
//                        System.out.println(num.get(letter));
                    } else {
                        indices.put(letter, "" + index);
                        num.put(letter, 1);
                    }
                    keyboard[r][c] = letter;
                    index++;
                }
            }
            // now build adj
            build();
//            printkeyboard();
//            printadj();
//            System.out.println("\n\n");
            fw();
//            printadj();
            line = in.nextLine().concat("*"); // string to find
//            System.out.println(line);
            buildGraph(line);
//            System.out.println("\n\n");
//            printgraph();
            findShortestPaths();
//            System.out.println("\n\n");
//            printgraph();
            int minValue = Integer.MAX_VALUE;
            for (int i = (graph.length-num.get('*')); i < graph.length; i++) {
                 minValue = Math.min(graph[0][i], minValue);
            }
//            System.out.println(minValue);
            System.out.println(minValue + line.length());
        }
    }
    private void printkeyboard() {
        for (int r = 0; r < keyboard.length; r++) {
            for (int c = 0; c < keyboard[r].length; c++) {
                System.out.printf(" %s ", keyboard[r][c]);
            }
            System.out.println();
        }
        for (int r = 0; r < keyboard.length; r++) {
            for (int c = 0; c < keyboard[r].length; c++) {
                System.out.printf(" %2d ", (r*C+c));
            }
            System.out.println();
        }
    }
    private void printadj() {
        System.out.println(adj.length);
        for (int r = 0; r < adj.length; r++) {
            System.out.printf(" %2d ", r);
        }
        System.out.println();
        for (int r = 0; r < adj.length; r++) {
            for (int c = 0; c < adj[r].length; c++) {
                if (adj[r][c] == Integer.MAX_VALUE) {
                    System.out.printf("  - ");
                } else {
                    System.out.printf(" %2d ", adj[r][c]);
                }
            }
            System.out.println();
        }
    }
    private void printgraph() {
        for (int r = 0; r < graph.length; r++) {
            for (int c = 0; c < graph[r].length; c++) {
                if (graph[r][c] == Integer.MAX_VALUE) {
                    System.out.printf("  - ");
                } else {
                    System.out.printf(" %2d ", graph[r][c]);
                }
            }
            System.out.println();
        }
    }
    public void print(int[][] arr) {
        System.out.println("TEMP");
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr.length; c++) {
                System.out.printf(" %10d ", arr[r][c]);
            }
            System.out.println();
        }
    }
    public void print(int[] arr, String string) {
        System.out.println(string);
        for (int i = 0; i < arr.length; i++) {
            System.out.printf(" %10d ", arr[i]);

        }
        System.out.println();
    }
    // initialize data structures
    private void init(String line) {
        String[] temp = line.split("\\s+");
        R = Integer.parseInt(temp[0]);
        C = Integer.parseInt(temp[1]);
        size = R*C;
        adj = new int[size][size];
        for (int i = 0; i < size; i++) { Arrays.fill(adj[i], Integer.MAX_VALUE); }
        keyboard = new char[R][C];
        indices = new HashMap<Character, String>();
        num = new HashMap<Character, Integer>();
    }
    // HELPERS
    private int indexOf(int r, int c) {
        return r*C + c;
    }
    private boolean inBounds(int r, int c) {
        return ((r >= 0 && r < R) && (c >= 0 && c < C));
    }
    // build the initial adjacency matrix
    private void build() {
        int index = 0;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                char letter = keyboard[r][c];
                adj[index][index] = 0;
                for (int i = 0; i < dc.length; i++) {
                    int dR = r + dr[i],
                        dC = c + dc[i];
                    while (inBounds(dR, dC)) {
                        // the same letter as before
                        if (keyboard[dR][dC] == keyboard[dR-dr[i]][dC-dc[i]]) {
                            dR = dR + dr[i];
                            dC = dC + dc[i];
                            continue;
                        }
                        int dIndex = indexOf(dR, dC);
                        if (letter != keyboard[dR][dC]) {
                            adj[index][dIndex] = 1;
                            //adj[dIndex][index] = 1;
                            break;
                        }
                        dR = dR + dr[i];
                        dC = dC + dc[i];
                    }
                }
                index++;
            }
        }
    }
    // run Floyd-Warshall on adj
    private void fw() {
        int N = adj.length;
        for (int i = 0; i < N; i++) {
            int[] vRow = new int[N],
                    vCol = new int[N];
            for (int row = 0; row < N; row++) {
                vRow[row] = adj[row][i];
            }
            for (int col = 0; col < N; col++) {
                vCol[col] = adj[i][col];
            }
            // build temp
            int[][] temp = new int[N][N];
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    temp[row][col] = (vRow[row] == Integer.MAX_VALUE || vCol[col] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : vRow[row] + vCol[col] ;
                }
            }
            // adjust
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    if (row == col) { continue; }
                    adj[row][col] = Math.min(adj[row][col], temp[row][col]);
                }
            }
        }
    }
    // calculate size of the graph based on input
    private int calcNumVert(String line) {
        int numVert = 1;
        char c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            numVert += num.get(c);
        }
        return numVert;
    }
    // build the inverse map for determining indexes
    private void buildInverse(String line, int numVert) {
        inverse = new int[numVert];
        char c;
//        System.out.println("Indexes of: " + line);
        for (int i = 0, cursor = 1; i < line.length(); i++) {
            c = line.charAt(i);
            String[] block = indices.get(c).split("\\s+");
//            System.out.println(c + ": " + block.length);
            for (int b = 0; b < block.length; b++) {
                inverse[cursor++] = Integer.parseInt(block[b]);
//                System.out.printf("%3d",inverse[cursor-1], b);
            }
        }
//        System.out.println();
    }
    // build the graph
    private void buildGraph(String line) {
        int numVert = calcNumVert(line), l = 0, h = 1;
        buildInverse(line, numVert);
        graph = new int[numVert][numVert];
        for (int i = 0; i < graph.length; i++) {
            Arrays.fill(graph[i], Integer.MAX_VALUE);
            graph[i][i] = 0;
        }
        int prevSize = 1;
        char c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            int sizeBlock = num.get(c);
            for (int from = 0; from < prevSize; from++) {
                for (int to = 0; to < sizeBlock; to++) {
                    int s = l + from,
                        t = h + to;
                    graph[s][t] = adj[inverse[s]][inverse[t]];
//                    System.out.println("graph["+s+"]["+t+"] = adj["+inverse[s]+"]["+inverse[t]+"] = "+adj[inverse[s]][inverse[t]]);
                }
            }
            l = h;
            h += sizeBlock;
            prevSize = sizeBlock;
        }
    }
    // find shortest path of the line
    private void findShortestPaths() {
        int N = graph.length;
//        System.out.println("ADJACENCY");
//        printgraph();
        for (int i = 0; i < N; i++) {
            int[] vRow = new int[N],
                  vCol = new int[N];

            for (int row = 0; row < N; row++) {
                vRow[row] = graph[row][i];
            }
//            print(vRow, "ROW");
            for (int col = 0; col < N; col++) {
                vCol[col] = graph[i][col];
            }
//            print(vCol, "COL");
            // build temp
            int[][] temp = new int[N][N];
            for (int row = 0; row < N; row++) {
                for (int col = row; col < N; col++) {
                    temp[row][col] = (vRow[row] == Integer.MAX_VALUE || vCol[col] == Integer.MAX_VALUE) ? Integer.MAX_VALUE : vRow[row] + vCol[col] ;
                }
            }
//            print(temp);
//            System.out.println("ADJACENCY");
//            printadj();
            // adjust
            for (int row = 0; row < N; row++) {
                for (int col = row; col < N; col++) {
                    if (row == col) { graph[row][col] = 0; }
                    graph[row][col] = Math.min(graph[row][col], temp[row][col]);
                }
            }
//            System.out.println("POST");
//            printgraph();
        }
    }
    public static void main(String[] args) { Keyboarding solve = new Keyboarding(); }
}