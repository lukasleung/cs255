import java.util.*;

/**
 * UVA 1714 ICPC Problem F 2015 World Finals Timelimit = 30.000 seconds
 * Completed testing in: seconds on
 * To Run type:
 *      javac Keyboarding.java
 *      java Keyboarding < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 6.0
 */
public class Keyboarding {
    private int[][] adj;//, graph;
    private char[][] keyboard;
    private int[] dr = {-1, 1, 0,  0},
                  dc = { 0, 0, 1, -1},
             inverse, graph;
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
            long start = System.currentTimeMillis();
            build();
            long stop = System.currentTimeMillis();
            System.err.println("Adj: " + (stop - start));
//            printkeyboard();
//            printadj();
//            System.out.println("\n\n");
            //fw();
//            printadj();
            line = in.nextLine().concat("*"); // string to find
//            System.out.println(line);
            start = System.currentTimeMillis();
            buildGraph(line);
            stop = System.currentTimeMillis();
            System.err.println("G  : " + (stop - start));
//            System.out.println("\n\n");
//            printgraph();
//            System.out.println("\n\n");
//            printgraph();
            int minValue = Integer.MAX_VALUE;
            for (int i = (graph.length-num.get('*')); i < graph.length; i++) {
                 minValue = Math.min(graph[i], minValue);
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
        System.out.println("GRAPH");
        for (int i = 0; i < graph.length; i++) {
            if (graph[i] == Integer.MAX_VALUE) {
                System.out.printf("  - ");
            } else {
                System.out.printf(" %2d ", graph[i]);
            }
        }
        System.out.println();
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
        for (int i = 0; i < size; i++) {
            Arrays.fill(adj[i], Integer.MAX_VALUE);
            adj[i][i] = 0;
        }
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
    // perform bfs with sRow and sCol as the source coordinates
    private void bfs(int sRow, int sCol) {
        int source = indexOf(sRow, sCol);
        boolean[][] seen = new boolean[keyboard.length][keyboard[0].length];
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(sRow);
        q.add(sCol);
        q.add(0);
        seen[sRow][sCol] = true;
        while (!q.isEmpty()) {
            int row = q.remove(), col = q.remove(), dist = q.remove();
            char letter = keyboard[row][col];
            for (int i = 0; i < dc.length; i++) {
                int dR = row + dr[i], dC = col + dc[i];
                while (inBounds(dR, dC)) {
                    // the same letter as before
                    if (keyboard[dR][dC] == keyboard[dR-dr[i]][dC-dc[i]]) {
                        dR = dR + dr[i];
                        dC = dC + dc[i];
                        continue;
                    }
                    int dIndex = indexOf(dR, dC);
                    if (letter != keyboard[dR][dC]) {
                        if (!seen[dR][dC] && dist+1 < adj[source][dIndex]) {
                            adj[source][dIndex] = (dist + 1);
                            q.add(dR);
                            q.add(dC);
                            q.add((dist + 1));
                            seen[dR][dC] = true;
                        }
                        break;
                    }
                    dR = dR + dr[i];
                    dC = dC + dc[i];
                }
            }
        }
    }
    // build the initial adjacency matrix
    private void build() {
        //int index = 0;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                bfs(r, c);
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
        long start = System.currentTimeMillis();
        int numVert = calcNumVert(line), l = 0, h = 1;
        buildInverse(line, numVert);
        long stop = System.currentTimeMillis();
        System.err.println("Set-Up: " + (stop - start));
        // dijkstra on graph
        graph = new int[numVert];
        Arrays.fill(graph, Integer.MAX_VALUE);
        graph[0] = 0; // source to itself is 0;
        int prevSize = 1;
        char c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            int sizeBlock = num.get(c);
            for (int from = 0; from < prevSize; from++) {
                int s = l + from;
                int vSource = graph[s];
                if (vSource == Integer.MAX_VALUE) { continue; }
                for (int to = 0; to < sizeBlock; to++) {
                    int t = h + to, dist = adj[inverse[s]][inverse[t]];
//                    System.out.println("adj["+inverse[s]+"]["+inverse[t]+"] = "+dist);
                    if (dist != Integer.MAX_VALUE && (dist + vSource < graph[t])) {
                        graph[t] = dist + vSource;
                    }
                }
            }
            l = h;
            h += sizeBlock;
            prevSize = sizeBlock;
        }
    }
    public static void main(String[] args) { Keyboarding solve = new Keyboarding(); }
}