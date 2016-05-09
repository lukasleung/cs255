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
            } else if (this.start < that.getStart()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    public ShipTraffic() {
        Scanner in = new Scanner(System.in);
        while(in.hasNextInt()) {
            // read input
            int numLanes = in.nextInt(),
                   width = in.nextInt();
            double shipSpeed = in.nextDouble(),
                  ferrySpeed = in.nextDouble();
            fStart = in.nextInt();
            fEnd = in.nextInt();
            // build our intervals
//            long init = System.currentTimeMillis();
            LinkedList<InvalidInterval> invalidIntervals = new LinkedList();
            for (int lane = 1; lane <= numLanes; lane++) {
                boolean eastBound = in.next().equals("E");
                int numBoats = in.nextInt();
                for (int i = 0; i < numBoats; i++) {
                    int length = in.nextInt(),
                         front = in.nextInt();

                    if (eastBound) { front *= -1; }
                    int back = front + length;
                    if (back < 0) { continue; } // already too far
//                    System.out.println(length + " " + front + " " + back);
                    double timeForBoatFront = (front/shipSpeed),
                          timeForFerryFront = (lane*width)/ferrySpeed,
                                 restOfBoat = (length/shipSpeed) + (width/ferrySpeed);
                    double start = timeForBoatFront - timeForFerryFront,
                            end = start + restOfBoat;
                    // wontMakeIt || alreadyPast
                    if (end < fStart || start > fEnd) { continue; }
//                    System.out.println("added");
                    start = Math.max(start, fStart);
                    end = Math.min(end, fEnd);
                    invalidIntervals.addFirst(new InvalidInterval(start, end));
                }
            }
//            long stop = System.currentTimeMillis();
//            System.err.println("Build: " + (stop - init));
//            init = System.currentTimeMillis();
            determineMaxValidInterval(convert(invalidIntervals));
//            stop = System.currentTimeMillis();
//            System.err.println("Find: " + (stop - init));
        }
    }
    // converts from a linked list to an array and sorts it
    private InvalidInterval[] convert(LinkedList<InvalidInterval> convertFrom) {
//        long init = System.currentTimeMillis();
        InvalidInterval[] converted = new InvalidInterval[convertFrom.size()];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = convertFrom.removeFirst();
        }
        Arrays.sort(converted);
//        long stop = System.currentTimeMillis();
//        System.err.println("Convert: " + (stop - init));
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
