/*
 * File: TurtleTokenizer.java
 * --------------------------
 * This file implements a simple tokenizer for the TurtleGraphics system.
 */

/**
 * This class divides up a command string into individual tokens.
 * A token consists of one of two forms:
 * <p/>
 * (1) A letter, optionally followed by any number of decimal digits,
 * as in "F20", "R120", or "D", or
 * (2) A string beginning with "{" and continuing up to the matching "}".
 * <p/>
 * The tokenizer ignores all whitespace characters separating tokens.
 */

public class OtherTurtleTokenizer {

    private String str;

    /**
     * Creates a new TurtleTokenizer that takes its input from the string str.
     *
     * @param str The string to be scanned
     */
    public OtherTurtleTokenizer(String str) {
        this.str = str.trim();
    }

    /**
     * Returns true if there are more tokens to read and false if the tokenizer
     * has reached the end of the input.
     *
     * @return A boolean value indicating whether there are any unread tokens
     */
    public boolean hasMoreTokens() {
        return str.length() > 0;
    }

    /**
     * Returns the next complete token. If this method is called at the end
     * of the input, the tokenizer returns the empty string.
     *
     * @return The next token in the input
     */
    public String nextToken() {
        char charAtI;
        try {
            charAtI = str.charAt(0);
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }

        str = str.substring(1);
        String[] numbers = str.split("[A-Z]|\\{|\\}|\\s+");

        switch (charAtI) {
            case ' ':
                return nextToken();
            case 'f':
            case 'l':
            case 'r':
            case 'u':
            case 'd':
            case 'x':
            case 'F':
            case 'L':
            case 'R':
            case 'U':
            case 'D':
            case 'X':
                if (str.length() > 0 && Character.isDigit(str.charAt(0))) {
                    str = str.substring(numbers[0].length());
                    return charAtI + numbers[0];
                } else {
                    return charAtI + "";
                }
            case '{':
                int counter = 1;
                int endIndex = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '{') counter++;
                    if (str.charAt(i) == '}') counter--;
                    if (counter == 0) {
                        endIndex = i + 1;
                        break;
                    }
                }
                String newString = charAtI + str.substring(0, endIndex);
                str = str.substring(endIndex);
                return newString;
        }
        return "";
    }
}