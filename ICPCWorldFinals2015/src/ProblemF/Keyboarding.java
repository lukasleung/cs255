import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * UVA 1714 ICPC Problem F 2015 World Finals Timelimit = 30.000 seconds
 * Completed testing in: seconds on
 * To Run type:
 *      javac Keyboarding.java
 *      java Keyboarding < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 8.0
 */
public class Keyboarding {
    private char[][] keyboard;
    private int[] dr = {-1, 1, 0,  0},
                  dc = { 0, 0, 1, -1};
    private int R, C;
    public Keyboarding() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] temp = line.split("\\s+");
            R = Integer.parseInt(temp[0]);
            C = Integer.parseInt(temp[1]);
            keyboard = new char[R][C];
            for (int r = 0; r < R; r++) {
                line = in.nextLine();
                for (int c = 0; c < C; c++) {
                    keyboard[r][c] = line.charAt(c);
                }
            }
            line = in.nextLine().concat("*");
            System.out.println(bfs(line) + line.length());
        }
    }
    private boolean inBounds(int r, int c) {
        return ((r >= 0 && r < R) && (c >= 0 && c < C));
    }
    private int bfs(String line) {
        int length = line.length();
        boolean[][][] seen  = new boolean[length][keyboard.length][keyboard[0].length];
        Queue<Integer> q = new LinkedList<Integer>();
        // 0: index in string
        int temp = 0;
        while (line.charAt(temp) == keyboard[0][0]) {
            temp++;
        }
        q.add(temp);
        q.add(0); // 1: row
        q.add(0); // 2: column
        q.add(0); // 3: length thus far
        seen[0][0][0] = true;
        while (!q.isEmpty()) {
            int index = q.remove(), row = q.remove(),
                  col = q.remove(), l = q.remove();
            l++;
            char goal = line.charAt(index), curLetter = keyboard[row][col];

            for (int i = 0; i < dc.length; i++) {
                int dR = row + dr[i], dC = col + dc[i];
                char lastLetter = keyboard[dR-dr[i]][dC-dc[i]];
                while (inBounds(dR, dC)) {
                    char thisLetter = keyboard[dR][dC];
                    // the same letter as before
                    if (thisLetter == lastLetter) {
                        dR += dr[i];
                        dC += dc[i];
                        continue;
                    }
                    // if not the same letter as we started with
                    if (!seen[index][dR][dC]) {
                        seen[index][dR][dC] = true;
                        if (thisLetter == goal) {
                            temp = 0;
                            while (line.charAt(index+temp) == keyboard[dR][dC]) {
                                temp++;
                                if (index + temp == length) {
                                    return l;
                                }
                            }
                            q.add(index + temp);
                            seen[index + temp][dR][dC] = true;
                        } else {
                            q.add(index);
                        }
                        q.add(dR);
                        q.add(dC);
                        q.add(l);
                    }
                    break;
                }
            }
        }
        return -1;
    }
    public static void main(String[] args) { Keyboarding solve = new Keyboarding(); }
}