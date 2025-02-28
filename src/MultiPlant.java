/**
 * The {@code MultiPlant} class simulates a multithreaded system where multiple plants process oranges
 * to produce juice bottles. Each plant operates as a separate thread, and their operations are synchronized
 * to manage shared resources and time.
 */

public class MultiPlant implements Runnable {
    // How long do we want to run the juice processing
    public static final long PROCESSING_TIME = 5 * 1000;

    private static final int NUM_PLANTS = 2;


    /**
     * Constructs a {@code MultiPlant} object with the specified thread to manage the plant operations.
     *
     * @param thread The thread that will execute the plant operations.
     */

    public MultiPlant(Thread thread) {
        this.thread = thread;
    }

    /**
     * The main method that initializes and starts the plants. It runs the plants, waits for them to finish,
     * and then summarizes the results.
     *
     * @param args Command-line arguments (not used in this class).
     */

    public static void main(String[] args) {
        // Startup the plants
        Plant[] plants = new Plant[NUM_PLANTS];
        for (int i = 0; i < NUM_PLANTS; i++) {
            // add 3 to the parameters which is the numThreads
            plants[i] = new Plant(i + 1, 3);
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

    private Thread thread;
    private int orangesProvided;
    private int orangesProcessed;
    private volatile boolean timeToWork;


    /**
     * Initializes a new plant with the specified thread number.
     *
     * @param threadNum The identifier number for the plant thread.
     */

    void Plant(int threadNum) {
        orangesProvided = 0;
        orangesProcessed = 0;
        thread = new Thread(this, "Plant[" + threadNum + "]");
    }

    /**
     * Starts the plant thread and begins processing oranges.
     */
    public void startPlant() {
        timeToWork = true;
        thread.start();
    }

    /**
     * Stops the plant thread from processing any more oranges.
     */
    public void stopPlant() {
        timeToWork = false;
    }

    /**
     * Waits for the plant thread to complete its work and stop.
     */
    public void waitToStop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println(thread.getName() + " stop malfunction");
        }
    }

    /**
     * The main processing loop for the plant. Each thread processes oranges as long as timeToWork is true.
     */
    public void run() {
        System.out.print(Thread.currentThread().getName() + " Processing oranges ");
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
        orangesProcessed++;
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
     * Returns the number of juice bottles created by this plant.
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
        return orangesProcessed % ORANGES_PER_BOTTLE;
    }
}