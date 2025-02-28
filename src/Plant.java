import java.util.ArrayList;
import java.util.List;

public class Plant implements Runnable {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 5 * 1000;

    private static final int NUM_PLANTS = 2;

    //make a new private static final int so that I can have multiple workers for a single plant
    private static final int PLANT_THREADS = 3;

    // used these articles as a reference: https://www.geeksforgeeks.org/multithreading-in-java/
    // https://www.tutorialspoint.com/java/java_multithreading.htm
    public static void main(String[] args) {
        // Startup the plants
        Plant[] plants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
            //add plant threads as a parameter instead
            plants[i] = new Plant(i + 1, PLANT_THREADS);
            plants[i].startPlant();
        }

        // Give the plants time to do work
        delay(PROCESSING_TIME, "Plant malfunction");

        // Stop the plant, and wait for it to shutdown
        for (Plant p : plants) {
            p.stopPlant();
        }
        for (Plant p : plants) {
            p.waitToStop();
        }

        // Summarize the results
        int totalProvided = 0;
        int totalProcessed = 0;
        int totalBottles = 0;
        int totalWasted = 0;
        for (Plant p : plants) {
            totalProvided += p.getProvidedOranges();
            totalProcessed += p.getProcessedOranges();
            totalBottles += p.getBottles();
            totalWasted += p.getWaste();
        }
        System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
        System.out.println("Created " + totalBottles +
                ", wasted " + totalWasted + " oranges");
    }

    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }

    public final int ORANGES_PER_BOTTLE = 3;


    // Use List<Thread> in order to use multiple threads per plant.
    private final List<Thread> threads = new ArrayList<>();
    private final int numThreads;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;

    Plant(int threadNum, int numThreads) {
        this.numThreads = numThreads;
        orangesProvided = 0;
        orangesProcessed = 0;
        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(this, "Plant[" + threadNum + "] Worker[" + (i + 1) + "]"));
        }
    }


    public void startPlant() {
        timeToWork = true;
        for (Thread t : threads) {
            t.start();
        }
    }


    public void stopPlant() {
        timeToWork = false;
    }

    public void waitToStop() {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println(t.getName() + " stop malfunction");
            }

        }
    }

    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges");
        while (timeToWork) {
            processEntireOrange(new Orange());
            orangesProvided++;


        }
        System.out.println("");
        System.out.println(Thread.currentThread().getName() + " Done");
    }

    public void processEntireOrange(Orange o) {
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }

        synchronized (this) {
            orangesProcessed++;
        }
    }

    public int getProvidedOranges() {
        return orangesProvided;
    }

    public int getProcessedOranges() {
        return orangesProcessed;
    }

    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}