
public class TestTurtleTokenizer {
    public static void main(String[] args) throws Exception {
        System.out.println("Case 1:");
        Stopwatch stahp = new Stopwatch();
        TurtleTokenizer tokenizer = new TurtleTokenizer("f10 R120");
        while (tokenizer.hasMoreTokens()) {
            System.out.print(tokenizer.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        Runtime usage = Runtime.getRuntime();
        double used = (double) (usage.totalMemory() - usage.freeMemory()),
                total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);



        System.out.println("__________________________________________");
        System.out.println("Case 2:");
        stahp = new Stopwatch();
        TurtleTokenizer tokenizer2 = new TurtleTokenizer("g10");
        while (tokenizer2.hasMoreTokens()) {
            System.out.print(tokenizer2.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);



        System.out.println("__________________________________________");
        System.out.println("Case 3:");
        stahp = new Stopwatch();
        TurtleTokenizer tokenizer3 = new TurtleTokenizer("x2{f60x30{10R60L10f}g16}");
        while (tokenizer3.hasMoreTokens()) {
            System.out.print(tokenizer3.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);

        System.out.println("__________________________________________");
        System.out.println("Case 4:");
        stahp = new Stopwatch();
        TurtleTokenizer tokenizer4 = new TurtleTokenizer("f60x30{10R60L10f}g16");
        while (tokenizer4.hasMoreTokens()) {
            System.out.print(tokenizer4.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);


        System.out.println("__________________________________________");
        System.out.println("Case 5:");
        stahp = new Stopwatch();
        TurtleTokenizer tokenizer5 = new TurtleTokenizer("10R60L10f");
        while (tokenizer5.hasMoreTokens()) {
            System.out.print(tokenizer5.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);



        System.out.println("__________________________________________");
        System.out.println("Case 6:");
        stahp = new Stopwatch();
        TurtleTokenizer tokenizer6 = new TurtleTokenizer("yolo");
        while (tokenizer6.hasMoreTokens()) {
            System.out.print(tokenizer6.nextToken() + "\n");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);


        System.out.println("__________________________________________");
        System.out.println("Case 7:");
        stahp = new Stopwatch();
        try {
            TurtleTokenizer tokenizer7 = new TurtleTokenizer("x2{f60x30{10R60L10f}g16");
            while (tokenizer7.hasMoreTokens()) {
                System.out.print(tokenizer7.nextToken() + "\n");
            }
        } catch (acm.util.ErrorException e) {
            System.out.println("CORRECT");
        }
        System.out.println("time: " + stahp.elapsedTime());
        usage = Runtime.getRuntime();
        used = (double) (usage.totalMemory() - usage.freeMemory());
        total = (double) usage.totalMemory();
        System.out.println("memory: " + (used/total)*100);
    }
}