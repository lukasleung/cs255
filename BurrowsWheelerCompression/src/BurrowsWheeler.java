import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.LinkedList;
// @authors Lukas Leung, Annamalis Sharp
public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder originalWord = new StringBuilder();
        // read the entire in-file
        while (!BinaryStdIn.isEmpty()) {
            originalWord.append(BinaryStdIn.readChar());
        }
        // System.err.println(originalWord.toString());
        // make the circular suffix array and find the sorted line
        CircularSuffixArray csa = new CircularSuffixArray(originalWord.toString());
        int first = -1;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first); // write the first index i.e. [00 00 00 03]
        // write the sorted suffix array attached to it
        for (int i = 0; i < csa.length(); i++) {
            int index; // get index of the last letter (of the ith sorted suffix)
                       //   in the original array
            if ( csa.index(i) == 0) {   // if the letter is the 0th, the last letter is the n-1th
                index = csa.length() - 1;
            } else { // otherwise last letter is in the previous index (circular array)
                index = csa.index(i) - 1;
            }
            BinaryStdOut.write(originalWord.charAt(index));
        }
        BinaryStdOut.flush();
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.err.println("Burrows transform memory: " + (used/total)*100);
    }
    // returns a char[] given a char array list O(N)
    private static char[] copyArray(ArrayList<Character> arr) {
        char[] c = new char[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            c[i] = arr.get(i);
        }
        return c;
    }
    // bucket sort the given character array O(R + N)
    private static void sortArray(char[] sorted) {
        final int NUMBER_OF_ASCI = 256; // R
        int[] bucket = new int[NUMBER_OF_ASCI];
        for (int i = 0; i < sorted.length; i++) {
            char c = sorted[i];
            bucket[c]++;
        }
        for (int c = 0, i = 0; c < NUMBER_OF_ASCI && i < sorted.length; c++) {
            while(bucket[c] > 0) {
                sorted[i++] = (char) c;
                bucket[c]--;
            }
        }
    }
    // build the next array O(R + N)
    private static int[] getNext(char[] t, int length) {
        final int NUMBER_OF_ASCI = 256;
        int[] next = new int[length];
        LinkedList<Integer>[] buckets = new LinkedList[NUMBER_OF_ASCI];
        // fill the buckets!
        for (int i = 0; i < length; i++) {
            char c = t[i];  // character
            if (buckets[c] == null) { buckets[c] = new LinkedList<>(); }
            buckets[c].add(i);
        }
        // use the buckets, fill the next!
        for (int c = 0, i = 0; c < NUMBER_OF_ASCI && i < length; c++) {
            while ((buckets[c] != null) && (i < length) && !buckets[c].isEmpty()) {
                next[i++] = buckets[c].removeFirst();
            }
        }
        return next;
    }
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();  // index of the original string
        // get the word
        ArrayList<Character> codedMessage = new ArrayList<>(); // t-array
        while (!BinaryStdIn.isEmpty()) {
            codedMessage.add(BinaryStdIn.readChar());
        }
        // create a char array of t and its corresponding sorted array
        int length = codedMessage.size();
        char[] t = copyArray(codedMessage),
                sorted = copyArray(codedMessage);
        sortArray(sorted); // bucket sort
        // build the next array
        int[] next = getNext(t,length); //getNext(t, sorted, length);
        // build the string
        for (int i = 0, index = first; i < length && index < length; i++) {
            BinaryStdOut.write(sorted[index]);
            index = next[index];
        }
        BinaryStdOut.flush();
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.err.println("Burrows inverse memory: " + (used/total)*100);
    }
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length > 0) {
            if ("-".equals(args[0])) {
                // Stopwatch sw = new Stopwatch();
                transform();
                // System.err.println("Bur transform time: " + sw.elapsedTime());
            }
            if ("+".equals(args[0])) {
                // Stopwatch sw = new Stopwatch();
                inverseTransform();
                // System.err.println("Bur inverse time: " + sw.elapsedTime());
            }
        }
    }
}