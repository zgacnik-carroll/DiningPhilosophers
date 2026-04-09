/**
 * Main class of program, holds the philosophers who "eat", philosophizing, or are hungry at the table.
 *
 */
public class DiningTable {

    private static final int DEFAULT_PHILOSOPHER_COUNT = 5;
    private static final int DEFAULT_MEALS_PER_PHILOSOPHER = 3;

    private final Philosopher[] philosophers;
    private final Thread[] philosopherThreads;
    private int completedPhilosophers;

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

    public void startDinner() {
        for (Thread philosopherThread : philosopherThreads) {
            philosopherThread.start();
        }

        synchronized (this) {
            try {
                while (completedPhilosophers < philosopherThreads.length) {
                    wait();
                }
            } catch (InterruptedException ignored) {}
        }
    }

    private synchronized void philosopherFinished() {
        completedPhilosophers++;

        if (completedPhilosophers >= philosopherThreads.length) {
            notifyAll();
        } else {
            notify();
        }
    }

    public Philosopher[] getPhilosophers() {
        return philosophers.clone();
    }

    public static void main(String[] args) {
        DiningTable diningTable = new DiningTable(DEFAULT_PHILOSOPHER_COUNT, DEFAULT_MEALS_PER_PHILOSOPHER);
        diningTable.startDinner();
    }

}
