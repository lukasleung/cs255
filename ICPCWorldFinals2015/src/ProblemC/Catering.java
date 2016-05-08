import java.util.Arrays;
import java.util.Scanner;
/**
 * UVA 1711 ICPC Problem A 2015 World Finals Timelimit = 3.000 seconds
 * Completed testing in: 1.250 seconds on 2016-05-06 04:29:57
 * To Run type:
 *      javac Catering.java
 *      java Catering < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class Catering {
    int N, K, numNodes, s, iFrom, iTo, t; // initial positions from and to
    int[][] graph;

    // http://algs4.cs.princeton.edu/24pq/IndexMinPQ.java.html
    private static class IndexMinPQ<Key extends Comparable<Key>> {
        private int maxN;
        private int N;
        private int[] pq;
        private int[] qp;
        private Key[] keys;

        public IndexMinPQ(int maxN) {
            this.maxN = maxN;
            keys = (Key[]) new Comparable[maxN + 1];
            pq   = new int[maxN + 1];
            qp   = new int[maxN + 1];
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }
        public boolean isEmpty() {
            return N == 0;
        }
        public boolean contains(int i) {
            return qp[i] != -1;
        }
        public void insert(int i, Key key) {
            N++;
            qp[i] = N;
            pq[N] = i;
            keys[i] = key;
            swim(N);
        }
        public int delMin() {
            int min = pq[1];
            exch(1, N--);
            sink(1);
            qp[min] = -1;
            keys[min] = null;
            pq[N+1] = -1;
            return min;
        }
        public void decreaseKey(int i, Key key) {
            keys[i] = key;
            swim(qp[i]);
        }
        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }
        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }
        private void swim(int k) {
            while (k > 1 && greater(k/2, k)) {
                exch(k, k/2);
                k = k/2;
            }
        }
        private void sink(int k) {
            while (2*k <= N) {
                int j = 2*k;
                if (j < N && greater(j, j+1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }
    }


    // Main functionality (Control)
    public Catering() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            buildGraph(in);
            System.out.println(matchMe());
        }
    }
    // constructs the graph with appropriate edgeweights
    private void buildGraph(Scanner in) {
        N = in.nextInt();
        K = in.nextInt();
        numNodes = 1 + (K + (N - 1)) + (N) + 1; // s -> from's -> to's -> t
        s = 0;
        iFrom = 1;
        iTo = K + N;
        t = numNodes - 1;
        graph = new int[numNodes][numNodes];
        for (int i = 0; i < graph.length; i++) { Arrays.fill(graph[i], Integer.MAX_VALUE); }
        // connect source
        for (int i = iFrom; i < iTo; i++) {
            graph[0][i] = 0;
        }
        // make connections from each team to each 'to'
        int[] teams = new int[N]; // store cost for team to travel, will use for number of teams
        for (int i = 0; i < teams.length; i++) {
            teams[i] = in.nextInt();
        }
        for (int i = iFrom; i <= K; i++) {
            for (int j = 0; j < N; j++) {
                graph[i][iTo + j] = teams[j];
            }
        }
        // make connections from each request to another request
        for (int i = iFrom + K, d = 1; i < iTo; i++, d++) {
            for (int j = iTo + d; j < t; j++) {
                graph[i][j] = in.nextInt();
            }
        }
        // connect sink
        for (int i = iTo; i < t; i++) {
            graph[i][t] = 0;
        }
    }
    // makes a copy of the graph array
    private int[][] copy() {
        int[][] cpy = new int[graph.length][graph[0].length];
        for (int r = 0; r < graph.length; r++) {
            for (int c = 0; c < graph[r].length; c++) {
                cpy[r][c] = graph[r][c];
            }
        }
        return cpy;
    }
    private void print(int[] arr) {
        //System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }
        System.out.println("\n");
    }
    private void print(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                String out = (arr[i][j] == Integer.MAX_VALUE) ? "-" : arr[i][j] + "";
                System.out.printf(" %s ", out);
            }
            System.out.println();
        }
    }
    // performs dijkstra, alters defCopyG, distTo and from
    private void dijkstra(int[][] defCopyG, int[] distTo, int[] from, IndexMinPQ<Integer> mpq) {
        Arrays.fill(distTo, Integer.MAX_VALUE);
        distTo[s] = 0;
        mpq.insert(s, distTo[s]);
        while (!mpq.isEmpty()) {
            int i = mpq.delMin();
            for (int j = 0; j < numNodes; j++) {
                if (defCopyG[i][j] != Integer.MAX_VALUE &&
                        distTo[j] > (distTo[i] + defCopyG[i][j])) {
                    distTo[j] = distTo[i] + defCopyG[i][j];
                    from[j] = i;
                    if (mpq.contains(j)) { mpq.decreaseKey(j, distTo[j]); }
                    else { mpq.insert(j, distTo[j]); }
                }
            }
        }
    }
    // performs Bipartite Matching
    public int matchMe() {
        int[][] defCopyG = copy();
        int[] cost = new int[numNodes]; // cost of each node, will change throughout
//        System.out.println("Graph:");
//        print(graph);
        // preform on all requests
        for (int i = 0; i < N; i++) {
//            System.out.println("----------------------------------\n" + i);
            // initialize for dijkstra
            int[] distTo = new int[numNodes];
            int[] from = new int[numNodes];
            IndexMinPQ<Integer> mpq = new IndexMinPQ<Integer>(numNodes);
            // perform dijkstra
            dijkstra(defCopyG, distTo, from, mpq);
//            System.out.println(" Dist");
//            print(distTo);
            // reverses all edges along path found
            int cNode = t;
            while (cNode != s) {
                int index = from[cNode]; // origin node of edge to current node
                defCopyG[cNode][index] = defCopyG[index][cNode]; // reverse
                defCopyG[index][cNode] = Integer.MAX_VALUE;      // set infinite opposite
                cNode = index;  // update
            }
            // update the cost of each vertex
            for (int j = 0; j < numNodes; j++) {
                cost[j] += distTo[j];
            }
//            System.out.println(" Cost");
//            print(cost);
            // update flow on edges going from -> to
            for (int j = iFrom; j < iTo; j++) {
                for (int k = 0; k < numNodes; k++) {
                    if (k != s && defCopyG[j][k] != Integer.MAX_VALUE) {
                        defCopyG[j][k] = cost[j] + graph[j][k] - cost[k];
//                        System.out.println("  " + j + " -> " + k + " = " + cost[j] + " + " + graph[j][k] + " - " + cost[k] + " = " + defCopyG[j][k]);
                    }
                }
            }
//            System.out.println();
            // update flow on edges going from <- to
            for (int j = iTo; j < t; j++) {
                for (int k = 0; k < numNodes; k++) {
                    if (k != t && defCopyG[j][k] != Integer.MAX_VALUE) {
                        defCopyG[j][k] = cost[k] + graph[k][j] - cost[j];
//                        System.out.println("  " + j + " <- " + k + " = " + cost[k] + " + " + graph[k][j] + " - " + cost[j] + " = " + defCopyG[j][k]);
                    }
                }
            }
//            System.out.println(" Graph");
//            print(defCopyG);
        }
        // sums total cost of the edges in the bipartite matching
        int minimum = 0;
        for (int i = iFrom; i < iTo; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (graph[i][j] != Integer.MAX_VALUE
                    && defCopyG[j][i] != Integer.MAX_VALUE) {
                    minimum += graph[i][j];
                }
            }
        }
        return minimum;
    }
    public static void main(String[] args) {
        Catering solve = new Catering();
    }
}