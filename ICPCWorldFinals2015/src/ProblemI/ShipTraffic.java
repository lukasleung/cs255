import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * UVA 1717 ICPC Problem I 2015 World Finals Timelimit = 3.000 seconds
 * Completed testing in:  seconds on
 * To Run type:
 *      javac ShipTraffic.java
 *      java ShipTraffic < pathTo/test.input.txt > printToThis.txt
 * @author Lukas Leung
 * @version 2.0
 */
public class ShipTraffic {
    private double fStart, fEnd;
    // only necesarry to keep track of the intervals that the ferry cannot cross
    private class InvalidInterval implements Comparable<InvalidInterval> {
        private double start, end;
        public InvalidInterval(double start, double end) {
            this.start = start;
            this.end = end;
        }
        public double getStart() {
            return start;
        }
        public double getEnd() {
            return end;
        }
        @Override
        public int compareTo(InvalidInterval that) {
            if (this.start > that.getStart()) {
                return 1;
            }
            return -1;
        }
    }
    public ShipTraffic() {
        Scanner in = new Scanner(System.in);
        String[] line;
        while(in.hasNextLine()) {
            line = in.nextLine().split("\\s+");
            // read input
            int numLanes = Integer.parseInt(line[0]),
                   width = Integer.parseInt(line[1]);
            double shipSpeed = Double.parseDouble(line[2]),
                  ferrySpeed = Double.parseDouble(line[3]);
            fStart = Integer.parseInt(line[4]);
            fEnd = Integer.parseInt(line[5]);
            // build our intervals
            LinkedList<InvalidInterval> invalidIntervals = new LinkedList();
            for (int lane = 1; lane <= numLanes; lane++) {
                line = in.nextLine().split("\\s+");
                boolean eastBound = line[0].equals("E");
                for (int i = 2; i < line.length; i += 2) {
                    int length = Integer.parseInt(line[i]),
                         front = Integer.parseInt(line[i + 1]);

                    if (eastBound) { front *= -1; }
                    int back = front + length;
                    if (back < 0) { continue; } // already too far
//                    System.out.println(length + " " + front + " " + back);
                    double timeForBoatFront = (front/shipSpeed),
                          timeForFerryFront = (lane*width)/ferrySpeed,
                                 restOfBoat = (length/shipSpeed) + (width/ferrySpeed);
                    double start = timeForBoatFront - timeForFerryFront,
                            end = start + restOfBoat;
                    boolean wontMakeIt = end < fStart,
                            alreadyPast = start > fEnd;
                    if (wontMakeIt || alreadyPast) { continue; }
//                    System.out.println("added");
                    start = Math.max(start, fStart);
                    end = Math.min(end, fEnd);
                    invalidIntervals.add(new InvalidInterval(start, end));
                }
            }
            determineMaxValidInterval(convert(invalidIntervals));
        }
    }
    // converts from a linked list to an array and sorts it
    private InvalidInterval[] convert(LinkedList<InvalidInterval> convertFrom) {
        InvalidInterval[] converted = new InvalidInterval[convertFrom.size()];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = convertFrom.removeFirst();
        }
        Arrays.sort(converted);
        return converted;
    }
    // given a set of invalid intervals determine the largest interval for ferry
    private void determineMaxValidInterval(InvalidInterval[] invalidIntervals) {
        if (invalidIntervals.length == 0) { // if no ships, not useful to proceed
            System.out.println((fEnd - fStart));
            return;
        }
        // record the largest gap thusfar as well as the latest known end ship pos
        double largestGap = invalidIntervals[0].getStart() - fStart,
               endTime = invalidIntervals[0].getEnd(),
                curGap;
        for(int curShip = 1; curShip < invalidIntervals.length; curShip++){
            // is this an overlapping segment?
            if(invalidIntervals[curShip].getStart() > endTime) {
                // not, check this gap
                curGap = invalidIntervals[curShip].getStart() - endTime;
                largestGap = Math.max(largestGap, curGap);  // if longer, save
                endTime = invalidIntervals[curShip].getEnd(); // record latest time
            } else {
                // it is, update the last seen portion
                endTime = Math.max(endTime, invalidIntervals[curShip].getEnd());
            }
        }
        // take last into effect
        curGap = fEnd-endTime;
        largestGap = Math.max(largestGap, curGap);
        System.out.printf("%.8f\n", largestGap);
    }
    public static void main(String[] args) {
        ShipTraffic solve = new ShipTraffic();
    }
}
