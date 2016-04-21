/*
 * File: TurtleTokenizer.java
 * --------------------------
 * This file implements a simple tokenizer for the TurtleGraphics system.
 */

import acm.util.ErrorException;

/**
 * This class divides up a command string into individual tokens.
 * A token consists of one of two forms:
 *
 * (1) A letter, optionally followed by any number of decimal digits,
 *     as in "F20", "R120", or "D", or
 * (2) A string beginning with "{" and continuing up to the matching "}".
 *
 * The tokenizer ignores all whitespace characters separating tokens.
 */

public class TurtleTokenizer {

/**
 * Creates a new TurtleTokenizer that takes its input from the string str.
 * @param str The string to be scanned
 */
	public TurtleTokenizer(String str) {
        str = clean(str);
        if (!validInput(str)) { throw new ErrorException("Missing closing brace"); }
        tokens = str;
		size = tokens.length();
	}

/**
 * Returns true if there are more tokens to read and false if the tokenizer
 * has reached the end of the input.
 * @return A boolean value indicating whether there are any unread tokens
 */
	public boolean hasMoreTokens() {
		return c_i < size;
	}

/**
 * Returns the next complete token.  Iftokens.length() this method is called at the end
 * of the input, the tokenizer returns the empty string.
 * @return The next token in the input
 */
	public String nextToken() {
        // ensure there are more tokens
        if (!hasMoreTokens()) return "";
        StringBuilder pillip = new StringBuilder();


        while (c_i < size) {
            boolean x = false;
            // Must begin with one of these.
            if( (tokens.charAt(c_i) == 'F') || (tokens.charAt(c_i) == 'R')
                   || (tokens.charAt(c_i) == 'L') || (tokens.charAt(c_i) == 'X')
                   || (tokens.charAt(c_i) == 'D') || (tokens.charAt(c_i) == 'U') ) {
                if (tokens.charAt(c_i) == 'X') { x = true; }
                // append the letter
                pillip.append(tokens.charAt(c_i++));
                // appends all numbers after the letters
                while (hasMoreTokens() && isNumber(c_i)) {
                    pillip.append(tokens.charAt(c_i++));
                }
                if (x && tokens.charAt(c_i) != '{') {
                    throw new ErrorException("Missing command block");
                }
                return pillip.toString();
            } else if (hasMoreTokens() && (tokens.charAt(c_i) == '{')) {
                // if this was an x, it would be followed by brackets {}
                int seenOpen = 1;     // saw {
                // until we close the main '{' append the strings
                while (hasMoreTokens() && seenOpen > 0) {
                    pillip.append(tokens.charAt(c_i++));
                    // this allows for nested x's with trailing braces
                    if (tokens.charAt(c_i) == '{') {
                        seenOpen++;
                    } else if (tokens.charAt(c_i) == '}') {
                        seenOpen--;
                        if (seenOpen == 0) {
                            pillip.append(tokens.charAt(c_i++));
                        }
                    }
                }
                return pillip.toString();
            } else {
                c_i++;
            }
        }
        return pillip.toString();
	}

// Add private methods and instance variables here
    private String tokens;  // stores all tokens
    private int c_i = 0,
                size = 0;

    // returns the number of white spaces in a string
	private int numSpaces(String s) {
		int numSpaces = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isWhitespace(s.charAt(i))) {
			   numSpaces++;
			}
		}
		return numSpaces;
	}
    // removes all whitespaces and turns all characters to lowercase
 	private String clean(String s) {
		int numCharacters = s.length() - numSpaces(s);
		StringBuilder str = new StringBuilder(numCharacters);
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(!Character.isWhitespace(c)){		// ignore whitespaces
				c = Character.toUpperCase(c);   // ensure all same case
				str.append(c);
			}
		}
		return str.toString();
	}
    // Tests to see that this is a valid
    private boolean validInput(String s) {
        int yolo = 0;
        // ensure brackets are good.
        for (int i = 0; i < s.length(); i++) {
             if (Character.compare(s.charAt(i),'{') == 0) {
                 yolo++;
             } else if(Character.compare(s.charAt(i),'}') == 0) {
                 yolo--;
             }
            if (yolo < 0) { return false; }
        }
        return yolo == 0;
    }
    // Tests to see if the indexed character is a number
    private boolean isNumber(int index) {
        return (tokens.charAt(index) >= '0' && tokens.charAt(index) <= '9');
    }
//    // Unit Testing
//    public static void main(String[] args) {
//        String command = "{g10r2 F3 X3 {FF4 x3{R3} L12 r }c11r f23 x2{uf20dr20f10} u f40}";
//        //command = "{f}";
//        command = command.substring(1,command.length() - 1);
//        TurtleTokenizer turtle = new TurtleTokenizer(command);
//        for (int i = 0; i < command.length(); i++) {
//            System.out.println(turtle.nextToken());
//        }
//    }
}