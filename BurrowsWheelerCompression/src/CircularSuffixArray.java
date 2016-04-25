import java.util.Arrays;
import java.util.Comparator;
// @authors Lukas Leung, Annamalis Sharp
public class CircularSuffixArray {
    private Integer[] indices;
    // Construct and sort the indices correctly
    public CircularSuffixArray(String s) {
        if (s == null) { throw new java.lang.NullPointerException(); }
        // myString = s;
        int length = s.length();
        char[] chars = s.toCharArray(); // used in comparator
        indices = new Integer[length];  // used to store integers
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, new Comparator<Integer>() {
            // returns positive if string starting at i1 is greater,
            //     negative if string starting at i2 is greater, and
            //     0 if equal.
            @Override
            public int compare(Integer i1, Integer i2) {
                // for all of the characters starting at i1 and i2
                for (int i = 0; i < length; i++) {
                    // get the first and second characters (of this instance)
                    char c1 = chars[(i1 + i) % length];
                    char c2 = chars[(i2 + i) % length];
                    // if they are not equal return which is greater
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                    // else still have the same suffix, continue checking the next case
                }
                // if they are completely equal return 0
                return 0;
            }
        });
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.err.println("CSA memory: " + (used/total)*100);
    }
    // length of original string
    public int length() {
        return indices.length;
    }
    // get the index stored in the specified index
    public int index(int i) {
        if ( i < 0 || i > length()-1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return indices[i];
    }
    // Unit Testing
    public static void main(String[] args) {
        for (String s : args) {
            CircularSuffixArray csa = new CircularSuffixArray(s);
            for (int i = 0; i < csa.length(); i++) {
                System.out.println(csa.index(i) + " ");
            }
        }
    }
}