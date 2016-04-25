import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;
// @authors Lukas Leung, Annamalis Sharp
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // populate alphabet with every character
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            alphabet.addLast(c);
        }
        // for every input
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(); // get the char
            // Get index of the first occurrence of the char
            int i = alphabet.indexOf(c);    // -1 if DNE
            alphabet.remove(i); // if -1, throws I<Character>OoB Exception
            // write to standard output and move c to front!
            BinaryStdOut.write((char) i);
            alphabet.addFirst(c);
        }
        BinaryStdOut.flush();
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.err.println("MTF encode memory: " + (used/total)*100);
    }
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // populate alphabet with every character
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char c = 0; c < 256; c++) {
            alphabet.addLast(c);
        }
        // for every input
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar(); // get the index corresponding to stored char
            char c = alphabet.remove(index);    // get the char from that index
            BinaryStdOut.write(c);  // write that char to standard output
            // move the char to the front
            alphabet.addFirst(c);
        }
        BinaryStdOut.flush();
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.err.println("MTF decode memory: " + (used/total)*100);
    }
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length > 0) {
            if ("-".equals(args[0])) {
                // Stopwatch sw = new Stopwatch();
                encode();
                // System.err.println("encode time: " + sw.elapsedTime());
            }
            if ("+".equals(args[0])) {
                // Stopwatch sw = new Stopwatch();
                decode();
                // System.err.println("decode time: " + sw.elapsedTime());
            }
        }
    }
}