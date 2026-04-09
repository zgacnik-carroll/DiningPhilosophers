/**
 * Coordinates the dining philosophers simulation.
 * <p>
 * The table creates the philosophers and chopsticks, starts each philosopher on
 * a dedicated thread, and waits until every philosopher has completed all meals.
 */
public class DiningTable {

    private static final int DEFAULT_PHILOSOPHER_COUNT = 5;
    private static final int DEFAULT_MEALS_PER_PHILOSOPHER = 3;

    private final Philosopher[] philosophers;
    private final Thread[] philosopherThreads;
    private int completedPhilosophers;

    /**
     * Creates a dining table and all participating philosophers.
     * <p>
     * Inputs are normalized so the simulation always has at least two
     * philosophers and a non-negative meal count.
     *
     * @param philosopherCount the requested number of philosophers
     * @param mealsPerPhilosopher the requested meals per philosopher
     */
    public DiningTable(int philosopherCount, int mealsPerPhilosopher) {
        philosopherCount = Math.max(2, philosopherCount);
        mealsPerPhilosopher = Math.max(0, mealsPerPhilosopher);

        Chopstick[] chopsticks = new Chopstick[philosopherCount];
        philosophers = new Philosopher[philosopherCount];
        philosopherThreads = new Thread[philosopherCount];

        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Chopstick(i);
        }

        for (int i = 0; i < philosophers.length; i++) {
            Chopstick left = chopsticks[i];
            Chopstick right = chopsticks[(i + 1) % chopsticks.length];
            philosophers[i] = new Philosopher(i, left, right, mealsPerPhilosopher);
            Philosopher philosopher = philosophers[i];
            philosopherThreads[i] = new Thread(() -> {
                philosopher.run();
                philosopherFinished();
            }, "Philosopher-" + i);
        }
    }

    /**
     * Starts all philosopher threads and waits for the simulation to finish.
     */
    public void startDinner() {
        for (Thread philosopherThread : philosopherThreads) {
            philosopherThread.start();
        }

        synchronized (this) {
            try {
                // Wait until every philosopher thread reports completion.
                while (completedPhilosophers < philosopherThreads.length) {
                    wait();
                }
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * Records that a philosopher has completed execution and wakes waiting
     * threads monitoring the table.
     */
    private synchronized void philosopherFinished() {
        completedPhilosophers++;

        if (completedPhilosophers >= philosopherThreads.length) {
            notifyAll();
        } else {
            notify();
        }
    }

    /**
     * Runs the simulation using the default philosopher and meal counts.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        DiningTable diningTable = new DiningTable(DEFAULT_PHILOSOPHER_COUNT, DEFAULT_MEALS_PER_PHILOSOPHER);
        diningTable.startDinner();
    }

}
