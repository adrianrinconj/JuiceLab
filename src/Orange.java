/**
 * The {@code Orange} class represents an orange being processed through various stages:
 * Fetched, Peeled, Squeezed, Bottled, and Processed. Each stage takes a specified amount
 * of time to complete, and the orange progresses through these stages sequentially.
 * This class simulates the process of handling an orange, with each stage of processing requiring
 * a certain amount of time. The orange can be processed through each stage by calling the {@code runProcess} method.
 */


public class Orange {

    /**
     * Enum representing the different states an orange can be in during processing.
     * Each state has an associated time to complete the processing for that stage.
     */
    public enum State {
        /**
         * The 'Fetched' state, representing the first stage of the orange's processing.
         * Time to complete: 15 milliseconds.
         */
        Fetched(15),

        /**
         * The 'Peeled' state, representing the second stage of the orange's processing.
         * Time to complete: 38 milliseconds.
         */
        Peeled(38),

        /**
         * The 'Squeezed' state, representing the third stage of the orange's processing.
         * Time to complete: 29 milliseconds.
         */
        Squeezed(29),

        /**
         * The 'Bottled' state, representing the fourth stage of the orange's processing.
         * Time to complete: 17 milliseconds.
         */
        Bottled(17),

        /**
         * The 'Processed' state, representing the final stage of the orange's processing.
         * Time to complete: 1 millisecond.
         */
        Processed(1);

        private static final int finalIndex = State.values().length - 1;

        final int timeToComplete;


        /**
         * Constructor to set the time to complete for each state.
         *
         * @param timeToComplete The time in milliseconds to complete the state.
         */
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        /**
         * Returns the next state in the processing sequence.
         *
         * @return The next state after the current one.
         * @throws IllegalStateException if the current state is already the final state.
         */
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }

    // The current state of the orange.
    private State state;

    /**
     * Constructs a new {@code Orange} object in the 'Fetched' state and immediately begins processing.
     */
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    /**
     * Returns the current state of the orange.
     *
     * @return The current state of the orange.
     */
    public State getState() {
        return state;
    }

    /**
     * Processes the orange by moving it to the next state. Each state requires a specified
     * amount of time to complete. The orange progresses through states sequentially.
     * <p>
     * If the orange has already been processed, this method will throw an exception.
     * </p>
     *
     * @throws IllegalStateException if the orange has already been processed.
     */
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        doWork();
        state = state.getNext();
    }

    /**
     * Simulates the work required for the current state of the orange by sleeping for the
     * amount of time defined for that state.
     */
    private void doWork() {
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }
}