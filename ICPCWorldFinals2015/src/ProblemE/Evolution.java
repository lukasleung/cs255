import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Arrays;

/**
 * UVA 1713 ICPC Problem E 2015 World Finals Timelimit = 30.000 seconds
 * Completed testing in:  seconds on
 * To Run type:
 *      javac Evolution.java
 *      java Evolution < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 1.0
 */
public class Evolution {
    private String[] sequence;
    private LinkedList<Integer>[] sso; // Sub-Sequence Of
    private String goal;
    public Evolution() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            int N = Integer.parseInt(in.nextLine());
            goal = in.nextLine();
            sequence = new String[N];
            for (int i = 0; i < N; i++) {
                sequence[i] = in.nextLine();
            }
            Arrays.sort(sequence, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return (o1.length() - o2.length());
                }
            });
            print();
            sso = new LinkedList[N];
            for (int i = 0; i < N; i++) {
                sso[i] = new LinkedList<Integer>();
                System.out.println("*" + sequence[i]);
                for (int j = i+1; j < N; j++) {
                    if (isSubSequence(sequence[i], sequence[j])) {
                        System.out.println(" - " + sequence[j]);
                        sso[i].add(j);
                    }
                }
            }
        }
    }
    // determines if s1 is a sub sequence of s2
    private boolean isSubSequence(String s1, String s2) {
        int i = 0;
        for (int j = 0; j < s2.length(); j++) {
            if (s1.charAt(i) == s2.charAt(j)) {
                if (++i == s1.length()) { return true; }
            }
        }
        return false;
    }
    private void print() {
        System.out.println("\nGOAL: " + goal);
        for (int i = 0; i < sequence.length; i++) {
            System.out.println(sequence[i]);
        }
        System.out.println();
    }
    public static void main(String[] args) { Evolution solve = new Evolution(); }
}
