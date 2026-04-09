import java.util.concurrent.ThreadLocalRandom;

/**
 * Models a philosopher participating in the dining philosophers simulation.
 * <p>
 * Each philosopher alternates between philosophizing and eating for a fixed
 * number of meals while sharing chopsticks with adjacent philosophers.
 */
public class Philosopher implements Runnable {

    private final int id;
    private final Chopstick leftChopstick;
    private final Chopstick rightChopstick;
    private final int mealsToEat;
    private volatile PhilosopherState state = PhilosopherState.PHILOSOPHIZING;

    /**
     * Creates a philosopher with references to the chopsticks on both sides.
     *
     * @param id the philosopher identifier used for output
     * @param leftChopstick the chopstick to the philosopher's left
     * @param rightChopstick the chopstick to the philosopher's right
     * @param mealsToEat the number of meals the philosopher should eat
     */
    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick, int mealsToEat) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.mealsToEat = mealsToEat;
    }

    /**
     * Runs the philosopher life cycle until all assigned meals have been eaten.
     */
    @Override
    public void run() {
        for (int meal = 1; meal <= mealsToEat; meal++) {
            philosophize();
            eat(meal);
        }

        System.out.printf("Philosopher %d leaves the table.%n", id);
    }

    /**
     * Simulates time spent thinking between meals.
     */
    private void philosophize() {
        state = PhilosopherState.PHILOSOPHIZING;
        System.out.printf("Philosopher %d is philosophizing.%n", id);
        pause();
    }

    /**
     * Attempts to acquire both chopsticks, eat a meal, and then release the
     * chopsticks in reverse order.
     *
     * @param mealNumber the meal currently being eaten
     */
    private void eat(int mealNumber) {
        state = PhilosopherState.HUNGRY;
        System.out.printf("Philosopher %d is hungry.%n", id);

        // Always pick up the lower-numbered chopstick first to prevent circular wait.
        Chopstick first = leftChopstick.getId() < rightChopstick.getId() ? leftChopstick : rightChopstick;
        Chopstick second = first == leftChopstick ? rightChopstick : leftChopstick;

        first.pickUp(this);
        System.out.printf("Philosopher %d picked up chopstick %d.%n", id, first.getId());

        try {
            second.pickUp(this);
            System.out.printf("Philosopher %d picked up chopstick %d.%n", id, second.getId());

            try {
                state = PhilosopherState.EATING;
                System.out.printf("Philosopher %d is eating meal %d.%n", id, mealNumber);
                pause();
            } finally {
                second.putDown(this);
                System.out.printf("Philosopher %d put down chopstick %d.%n", id, second.getId());
            }
        } finally {
            first.putDown(this);
            System.out.printf("Philosopher %d put down chopstick %d.%n", id, first.getId());
        }
    }

    /**
     * Pauses for a short random interval to vary the thread schedule.
     */
    private void pause() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(250, 750));
        } catch (InterruptedException ignored) {}
    }
}
