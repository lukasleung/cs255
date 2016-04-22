import java.util.LinkedList;
import java.util.Scanner;
/**
 * UVA 124 Following Orders Timelimit = 3.00 seconds
 * Completed testing in: 0.065 seconds on 2016-02-26 03:37:20
 * Note: to see a walk-through uncomment all which have "//"
 *          on the left most side of the page.
 * To Run type:
 *      javac FollowingOrders
 *      java FollowingOrders < input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 3.0
 */
public class FollowingOrders {
    /***
     * Recursive roundabout implementation of hashing and depth first search
     *  on a graph. The outcome of this is a full string builder containing
     *  all of the resulting anagrams in order with the valid conditions
     *  holding.
     * @param left      - Index of the left most letter being searched from
     * @param im        - inverseMap used to give the character associated
     *                      with each index
     * @param inDegree  - the number of letters that must be less than each
     *                      letter given
     * @param G         - the graph containing all of the letters and the
     *                      letters that follow them (conditions)
     * @param result    - the string built thus far (in recursive process)
     */
    public static void dfsHash(int left, char[] im, int[] inDegree,
                           LinkedList<Integer>[] G, char[] result) {
        // base case, if we have the correct result, we will have the result we want
        if (left == 0) {
            System.out.println(new String(result));
            return;
            // sb.append(new String(result)).append('\n');
        }
        // for each letter check to see if they do not need
        //  to be less than another letter
        for (int i = 0; i < result.length; i++)
        {
            // at this stage, i guaranteed to be able to go anywhere
            if (inDegree[i] == 0)
            {
                // if they do not, mark read
                inDegree[i] = -1;
                // write down the given character from inverse map
                result[result.length - left] = im[i];
                // for each letter that must be greater than the target letter
                //  decrement its number of inDegrees, for now we have taken
                //  care of it
                for (int letter : G[i]) { inDegree[letter]--; }
                // recursively call this method with updated lists and left
                dfsHash(left - 1, im, inDegree, G, result); // recursive call
                // return inDegrees to normal
                for (int letter : G[i]) { inDegree[letter]++; }
                // now fully used this letter, will be noted and move on to next one
                inDegree[i] = 0;
            }
        }
    }
    // bucket sort the given character array O(R + N)
    public static void sortArray(char[] sort)
    {
        final int LOWER_A = 97;
        int[] bucket = new int[26]; // R
        // fill in my buckets
        for (char c : sort) { bucket[c-LOWER_A]++; }
        // if the buckets are not empty refill the char array
        for (int c = 0, i = 0; c < 26 && i < sort.length; c++)
        {
            if (bucket[c] > 0) { sort[i++] = (char) (c + LOWER_A); }
        }
    }
    // turn string array into a char array O(N + M <-rules)
    public static char[] stringsToChar(String[] strings)
    {
        char[] goal = new char[strings.length];
        for (int i = 0; i < strings.length; i++) {
            goal[i] = strings[i].charAt(0);
        }
        return goal;
    }
    // main functionality
    public static void main(String[] args) {
        final int LOWER_A = 97;
        int[] map = new int[26];
        Scanner in = new Scanner(System.in);
        while (in.hasNext())
        {
            // get the symbols and sort them
            String[] strings = in.nextLine().split("\\s+");
            char[] symbols = stringsToChar(strings);
            sortArray(symbols);
            // get the comparisons each pair correspond to a rule
            strings = in.nextLine().split("\\s+");
            char[] comparisons = stringsToChar(strings);

            int n = symbols.length; // number of symbols
            LinkedList<Integer>[] G = (LinkedList<Integer>[]) new LinkedList[n];
            // for each character, initialize a linked list
            for (int i = 0; i < n; i++) { G[i] = new LinkedList<>(); }
            // for each character, map the index of the character
            //   and the character of the index
            char[] inverseMap = new char[n];
            for (int i = 0; i < n; i++)
            {
                int indexOfChar = symbols[i]-LOWER_A;
                map[indexOfChar] = i;
                inverseMap[i] = symbols[i];
            }
            char[] result = new char[n];
            int[] inDegree = new int[n];
            // for each comparison, record how many indegrees each char has
            //  and which letters must be greater than which other letters
            for (int i = 0; i < comparisons.length; i += 2)
            {
                // get index of less and greater in sorted list
                int l = map[comparisons[i]-LOWER_A];
                int g = map[comparisons[i + 1]-LOWER_A];
                inDegree[g]++;  // increment how many letters must be less than g
                G[l].add(g);  // record which letter is greater than which less
            }
            // perform dfs from the left most side first
            dfsHash(n, inverseMap, inDegree, G, result);
            // print out everything
            if (in.hasNext()) { System.out.println(); }
        }
    }
}