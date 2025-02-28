import java.util.ArrayList;
import java.util.List;


/**
 * The {@code Plant} class is a plant that processes oranges with multiple worker threads.
 * Each plant has a specified number of worker threads, which process oranges concurrently.
 * The plant runs for a fixed period of time, processes oranges into juice, and then summarizes the results.
 * The class utilizes multiple threads per plant.
 */

public class Plant implements Runnable {

    /**
     * The time in milliseconds for which the juice processing will run.
     */
    public static final long PROCESSING_TIME = 5 * 1000;

    /**
     * The number of plants to be simulated.
     */
    private static final int NUM_PLANTS = 2;

    /**
     * The number of worker threads to be used by each plant.
     */
    //make a new private static final int so that I can have multiple workers for a single plant
    private static final int PLANT_THREADS = 3;


    /**
     * Main method to start multiple plants and process juice.
     * It initiates each plant's threads, waits for processing to finish,
     * and then provides a summary of the processing results.
     */


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
                ", wasted " + (-1 * totalWasted) + " oranges");
    }


    /**
     * Pauses the current thread for a specified amount of time.
     *
     * @param time   The amount of time in milliseconds to sleep.
     * @param errMsg The error message to display if the thread is interrupted during sleep.
     */
    private static void delay(long time, String errMsg) {
        long sleepTime = Math.max(1, time);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(errMsg);
        }
    }


    /**
     * The number of oranges required to fill one bottle.
     */
    public final int ORANGES_PER_BOTTLE = 3;


    // Use List<Thread> in order to use multiple threads per plant.
    private final List<Thread> threads = new ArrayList<>();
    private final int numThreads;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;


    /**
     * Initializes a plant with the specified number of worker threads.
     *
     * @param threadNum  The identifier number for the plant.
     * @param numThreads The number of worker threads to assign to this plant.
     */
    Plant(int threadNum, int numThreads) {
        this.numThreads = numThreads;
        orangesProvided = 0;
        orangesProcessed = 0;
        for (int i = 0; i < numThreads; i++) {
            threads.add(new Thread(this, "Plant[" + threadNum + "] Worker[" + (i + 1) + "]"));
        }
    }

    /**
     * Starts all worker threads of the plant to begin processing oranges.
     */
    public void startPlant() {
        timeToWork = true;
        for (Thread t : threads) {
            t.start();
        }
    }


    /**
     * Stops all worker threads from processing any further oranges.
     */
    public void stopPlant() {
        timeToWork = false;
    }

    /**
     * Waits for all worker threads to complete their tasks and stop.
     */
    public void waitToStop() {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println(t.getName() + " stop malfunction");
            }
        }
    }

    /**
     * Main processing loop for each worker thread. Each thread processes oranges
     * until the timeToWork flag is set to false.
     */
    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges");
        while (timeToWork) {
            processEntireOrange(new Orange());
            orangesProvided++;
        }
        System.out.println("");
        System.out.println(Thread.currentThread().getName() + " Done");
    }


    /**
     * Processes an entire orange until it is bottled.
     *
     * @param o The orange to be processed.
     */
    public void processEntireOrange(Orange o) {
        while (o.getState() != Orange.State.Bottled) {
            o.runProcess();
        }

        // Synchronize access to shared data (orangesProcessed)
        synchronized (this) {
            orangesProcessed++;
        }
    }

    /**
     * Returns the total number of oranges provided by this plant.
     *
     * @return The total number of provided oranges.
     */
    public int getProvidedOranges() {
        return orangesProvided;
    }

    /**
     * Returns the total number of oranges processed by this plant.
     *
     * @return The total number of processed oranges.
     */
    public int getProcessedOranges() {
        return orangesProcessed;
    }

    /**
     * Returns the number of juice bottles created by this plant based on the processed oranges.
     *
     * @return The number of bottles created.
     */
    public int getBottles() {
        return orangesProcessed / ORANGES_PER_BOTTLE;
    }

    /**
     * Returns the number of wasted oranges (those that could not be used to fill a bottle).
     *
     * @return The number of wasted oranges.
     */
    public int getWaste() {
        return orangesProcessed % ORANGES_PER_BOTTLE + (orangesProvided - orangesProcessed);
    }
}