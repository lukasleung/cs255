import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

// javac -cp .:/home/lukas/packages/algs4.jar:/home/lukas/packages/stdlib.jar:/home/lukas/packages/acm.jar:/home/lukas/packages/turtle.jar *.java

/**
 * Created by lukas on 1/21/16.
 */
public class Anagrams {
    private int len = 0;               // length of the word
    private HashSet<String> anagrams;  // stores all anagrams found thus far
    // Constructor
    public Anagrams(String filename) {
        if (filename == null) { throw new NullPointerException(); }
        In file = new In(filename);
        // get each word from the text file and print its anagrams
        while (!file.isEmpty()) {
            String word = file.readLine();
            word.toLowerCase();
            if (word.length() < 2) { System.out.println(word + "\n"); }
            else {
                // make new hashset for each word
                anagrams = new HashSet<String>();
                len = word.length();
                findAnagrams(word);
            }
            // System.out.println("Time taken: " + watch.elapsedTime() + "\n");
        }
    }
    // determine all anagrams
    private void findAnagrams(String s) {
        char[] word = s.toCharArray();
        Arrays.sort(word);
        anagrams.add(new String(word));
        // get anagrams
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                if (word[i] == word[j] || word[j-1] == word[j]) { continue; }
                swap(word, i , j);
            }
        }
        String[] toPrint = new String[anagrams.size()];
        int k = 0;
        // print out the list
        for (String anagram : anagrams) { toPrint[k++] = anagram; }
        Arrays.sort(toPrint);
        for (int i = 0; i < toPrint.length; i ++) {
            System.out.println(toPrint[i]);
        }
        System.out.println();
    }
    // swaps two characters
    private void swap(char[] original, int i, int j) {
        if (i < 0 || j < 0) { throw new IndexOutOfBoundsException(); }
        if (i == j || i >= len || j >= len) { return; }
        char[] checkWord = copy(original);
        // swap letters
        char temp = checkWord[i];
        checkWord[i] = checkWord[j];
        checkWord[j] = temp;
        // check to see if new word has been found
        String newWord = new String(checkWord);
        if (!anagrams.contains(newWord)) {
            anagrams.add(newWord);
        }
        // recursively call swap method to ensure all possible strings are checked
        for (int newi = i + 1; newi < len; newi++) {
            for (int newj = newi + 1; newj < len; newj++) {
                if (checkWord[newi] == checkWord[newj]) { continue; }
                swap(checkWord, newi, newj);
            }
        }
    }
    // make a copy of a char array
    private char[] copy(char[] original) {
        char[] copy = new char[len];
        for (int i =0; i < len; i++) {
            copy[i] = original[i];
        }
        return copy;
    }

    public static void main(String[] args) {
        Anagrams a = new Anagrams(args[0]);
    }
}
