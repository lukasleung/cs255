import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * UVA 11506 Angry Programmer Timelimit = 4.000 seconds
 * Completed testing in: 0. seconds on
 * To Run type:
 *      javac AngryProgrammer.java
 *      java AngryProgrammer < pathTo/input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class AngryProgrammer {
    private static int m, numV, serverIn;
    private static LinkedList<FlowEdge>[] adj;
    private static FlowEdge[] edgeTo;
    public static class FlowEdge {
        private final int v;             // from
        private final int w;             // to
        private final int capacity;   // capacity
        private double flow;             // flow

        public FlowEdge(int v, int w, int capacity) {
            this.v = v;
            this.w = w;
            this.capacity = capacity;
            this.flow = 0.0;
        }
        public int from() {
            return v;
        }
        public int to() {
            return w;
        }
        public int other(int vertex) {
            if      (vertex == v) return w;
            else if (vertex == w) return v;
            else throw new IllegalArgumentException("Illegal endpoint");
        }
        public double capacity() {
            return capacity;
        }
        public double residualCapacityTo(int vertex) {
            if (vertex == v) { return flow; }       // backward edge
            else if (vertex == w) { return capacity - flow; }   // forward edge
            else { throw new IllegalArgumentException("Illegal endpoint"); }
        }
        public double flow() {
            return flow;
        }
        public void addResidualFlowTo(int vertex, double delta) {
            if (vertex == v) { flow -= delta; }
            else if (vertex == w) { flow += delta; }
            else { throw new IllegalArgumentException("Illegal endpoint"); }
        }
        public String toString() {
            return v + "->" + w + " " + flow + "/" + capacity;
        }
    }
    public static class Queue<Item> {
        private int N;         // number of elements on queue
        private Node first;    // beginning of queue
        private Node last;     // end of queue
        // helper linked list class
        private class Node {
            private Item item;
            private Node next;
        }
        public Queue() {
            first = null;
            last  = null;
            N = 0;
        }
        public boolean isEmpty() {
            return first == null;
        }
        public int length() {
            return N;
        }
        public void enqueue(Item item) {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            if (isEmpty()) first = last;
            else           oldlast.next = last;
            N++;
        }
        public Item dequeue() {
            if (isEmpty()) throw new NoSuchElementException("Queue underflow");
            Item item = first.item;
            first = first.next;
            N--;
            if (isEmpty()) last = null;   // to avoid loitering
            return item;
        }
    }
    public static int detIndex(int i, boolean out) {
        if (out) { return i + (m-1); }
        return i - 1;
    }
    public static boolean hasAugmentingPath() {
        edgeTo = new FlowEdge[numV];
        boolean[] marked = new boolean[numV];
        // bfs
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(m);
        marked[0] = true;
        marked[m] = true;
        while (!queue.isEmpty() && !marked[serverIn]) {
            int vertex = queue.dequeue();
            for (FlowEdge e : adj[vertex]) {
                int other = e.other(vertex);
                // if residual capacity from v to w
                if (e.residualCapacityTo(other) > 0) {
                    if (!marked[other]) {
                        edgeTo[other] = e;
                        marked[other] = true;
                        queue.enqueue(other);
                    }
                }
            }
        }
        return marked[serverIn];
    }
    public static double excess(int s) {
        double excess = 0.0;
        for (FlowEdge edge : adj[s]) {
            if (s == edge.from()) { excess -= edge.flow(); }
            else { excess += edge.flow(); }
        }
        return excess;
    }
    public static void calcMinCut() {
        double value = excess(m);
        while (hasAugmentingPath()) {
            // compute bottleneck capacity
            double bottle = Double.POSITIVE_INFINITY;
            int v = serverIn;
            while (v != m) {
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
                v = edgeTo[v].other(v);
            }
            // augment flow
            v = serverIn;
            while (v != m) {
                edgeTo[v].addResidualFlowTo(v, bottle);
                v = edgeTo[v].other(v);
            }
            value += bottle;
        }
        System.out.println((int) value);
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int round = 0;
        while (true) {
            round++;
            String[] line = in.nextLine().split("\\s+");
            int W = Integer.parseInt(line[1]);  // num Connections
            m = Integer.parseInt(line[0]);  // num machines
            if (m == 0 && W == 0) { break; }
            if (W == 0) {
                System.out.println(0);
                for (int i = 0; i < (m-2); i++) {
                    in.nextLine();
                }
                continue;
            }
            // if (round != 1) { System.out.println(); }
            numV = 2*m;      // num vertices
            serverIn = m-1;   // index of server, boss is 0
            // 0 => boss, m-1 => server
            adj = new LinkedList[numV];
            for (int i = 0; i < numV; i++) { adj[i] = new LinkedList<FlowEdge>(); }
            // set up computers
            adj[0].add(new FlowEdge(0, m, Integer.MAX_VALUE));
            adj[serverIn].add(new FlowEdge(serverIn, numV-1, Integer.MAX_VALUE));
            for (int curmachine = 0; curmachine < (m - 2); curmachine++) {
                line = in.nextLine().split("\\s+");
                int i = Integer.parseInt(line[0])-1,  // index of machine
                    c = Integer.parseInt(line[1]);  // cost of destroying machine
                FlowEdge e1 = new FlowEdge(i, i+m, c);
//                System.out.println(i + " " + c);
//                System.out.println("  " + (i-1) + " to " + (i+m) +
//                        " w/ cap = " + c);
                adj[i].add(e1);
            }
//            System.out.println("connections: ");
            // record each connection
            for (int c_i = 0; c_i < W; c_i++) {
                line = in.nextLine().split("\\s+");
                int a = Integer.parseInt(line[0]),  // node 1
                    b = Integer.parseInt(line[1]),  // node 2
                    c = Integer.parseInt(line[2]);  // cost
//                System.out.println(a + " to " + b + " w/ cap = " + c);
                int outA = detIndex(a, true),
                     inA = detIndex(a, false),
                    outB = detIndex(b, true),
                     inB = detIndex(b, false);
                // create edges (2-way)
//                System.out.println("  " + outA + " to " + inB + " w/ cap = " + c);
//                System.out.println("  " + outB + " to " + inA + " w/ cap = " + c);
                FlowEdge e1 = new FlowEdge(outA, inB, c),
                         e2 = new FlowEdge(outB, inA, c);
                // connect a - b w/ cost c
                adj[outA].add(e1);
                adj[outB].add(e2);
            }
            // show stuff
//            for (int i = 0; i < numV; i++) {
//                System.out.println(i);
//                for (FlowEdge adjNode : adj[i]) {
//                    System.out.println(" $\\rightarrow$ " + adjNode.to() + " with cap = " + adjNode.capacity());
//                }
//                // System.out.println();
//            }
            // calculate the min cut
            calcMinCut();
        }
    }
}