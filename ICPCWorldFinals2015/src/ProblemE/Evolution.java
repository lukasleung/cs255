import java.util.*;

/**
 * UVA 1713 ICPC Problem E 2015 World Finals Timelimit = 30.000 seconds
 * Completed testing in: 0.620 seconds on 2016-05-04 17:06:22
 * To Run type:
 *      javac Evolution.java
 *      java Evolution < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 3.0
 */
public class Evolution {
    public Evolution() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            int N = Integer.parseInt(in.nextLine());
            String goal = in.nextLine();
            String[] sequence = new String[N];
            for (int i = 0; i < N; i++) {
                sequence[i] = in.nextLine();
            }
            // sort so that it is shortest to longest
            Arrays.sort(sequence, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return (o1.length() - o2.length());
                }
            });
            computeHighToLow(sequence, goal);
        }
    }
    private void computeHighToLow(String[] sequence, String goal) {
        Stack<String> sub1 = new Stack<String>(),
                      sub2 = new Stack<String>();
        LinkedList<String> sharedList = new LinkedList<String>();
        boolean failed = false, shared = false;
        String lastS1 = "", lastS2 = "";
        sub1.push(goal);
        sub2.push(goal);
        // for each string from longest to shortest go through and
        //  see if they belong to subsequence 1 or 2 or both
        for (int i = sequence.length-1; !failed && i >= 0; i--) {
            String token = sequence[i];
            if (shared) { // if both
                if (isSubSequence(token, sharedList.peekFirst())) {
                    // if can be appended to the shared
                    sharedList.addFirst(token);
                } else if (isSubSequence(token, lastS1)) {
                    // if it can only be in sequence 1 before shared
                    sub1.push(token);
                    append(sharedList, sub2);  // put shared on 2
                    shared = false;
                } else if (isSubSequence(token, lastS2)) {
                    // if it can only be in sequence 2 before shared
                    sub2.push(token);
                    append(sharedList, sub1);  // put shared on 1
                    shared = false;
                } else {
                    // cannot be on any of the three. impossible
                    failed = true;
                }
            } else {
                boolean inS1 = isSubSequence(token, sub1.peek()),
                        inS2 = isSubSequence(token, sub2.peek());
                if (inS1 && inS2) {
                    // if shared, then mark as such and initialize shared
                    shared = true;
                    sharedList.addFirst(token);
                    // store the last seen elements
                    lastS1 = sub1.peek();
                    lastS2 = sub2.peek();
                } else if (inS1) {
                    // only can be put on sequence 1
                    sub1.push(token);
                } else if (inS2) {
                    // only can be put on sequence 2
                    sub2.push(token);
                } else {
                    // cannot be on either. impossible
                    failed = true;
                }
            }
        }
        if (failed) {
            System.out.println("impossible");
        } else {
            if (shared) { append(sharedList, sub2); }
            System.out.println((sub1.size()-1) + " " + (sub2.size()-1));
            print(sub1);
            print(sub2);
        }
    }
    // prints out the contents of one of the stacks
    private void print(Stack<String> s) {
        while (s.size() > 1) { System.out.println(s.pop()); }
    }
    // appends the shared linked list onto the corresponding sub-sequence
    private void append(LinkedList<String> shared, Stack<String> subSequence) {
        while (!shared.isEmpty()) { subSequence.push(shared.removeLast()); }
    }
    // determines if s1 is a sub sequence of s2
    private boolean isSubSequence(String s1, String s2) {
        for (int j = 0, i = 0; j < s2.length(); j++) {
            if (s1.charAt(i) == s2.charAt(j)) {
                if (++i == s1.length()) { return true; }
            }
        }
        return false;
    }
    public static void main(String[] args) { Evolution solve = new Evolution(); }
}