import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.Comparator;

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
//            print();
            // computeHighToLow();
            computeLowToHigh();
        }
    }
    private void printReverse(Stack<String> s) {
        Stack<String> temp = new Stack<String>();
        while (!s.isEmpty()) {
            temp.push(s.pop());
        }
        while (!temp.isEmpty()) {
            System.out.println(temp.pop());
        }
    }
    private void computeLowToHigh() {
        Stack<String> sub1 = new Stack<String>(),
                      sub2 = new Stack<String>();
        boolean failed = false;
        for (int i = 0; !failed && i < sequence.length; i++) {
            String token = sequence[i];
            if (sub1.isEmpty() || isSubSequence(sub1.peek(), token)) {
                sub1.push(token);
            } else if (sub2.isEmpty() || isSubSequence(sub2.peek(), token)) {
                sub2.push(token);
            } else {
                failed = true;
            }
        }
        if (!isSubSequence(sub1.peek(), goal)) {
            failed = true;
        }
        if ((!sub2.isEmpty() && !isSubSequence(sub2.peek(), goal))) {
            failed = true;
        }
        if (failed) { System.out.println("impossible"); }
        else {
            System.out.println((sub2.size()) + " " + (sub1.size()));
            printReverse(sub2);
            printReverse(sub1);
        }
    }
    private void computeHighToLow() {
        Stack<String> sub1 = new Stack<String>(),
                      sub2 = new Stack<String>();
        boolean failed = false;
        sub1.push(goal);
        sub2.push(goal);
        for (int i = sequence.length-1; !failed && i >= 0; i--) {
            String token = sequence[i];
            if (isSubSequence(token, sub1.peek())) { sub1.push(token); }
            else if (isSubSequence(token, sub2.peek())) { sub2.push(token); }
            else { failed = true; }
        }
        if (failed) {
            System.out.println("impossible");
        } else {
            System.out.println((sub2.size()-1) + " " + (sub1.size()-1));
            print(sub2);
            print(sub1);
        }
    }
    // prints out the contents of one of the stacks
    private void print(Stack<String> s) {
        while (s.size() > 1) {
            System.out.println(s.pop());
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