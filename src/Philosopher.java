import java.util.concurrent.ThreadLocalRandom;

public class Philosopher implements Runnable {

    private final int id;
    private final Chopstick leftChopstick;
    private final Chopstick rightChopstick;
    private final int mealsToEat;
    private volatile PhilosopherState state = PhilosopherState.PHILOSOPHIZING;

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick, int mealsToEat) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.mealsToEat = mealsToEat;
    }

    public int getId() {
        return id;
    }

    public PhilosopherState getState() {
        return state;
    }

    @Override
    public void run() {
        for (int meal = 1; meal <= mealsToEat; meal++) {
            philosophize();
            eat(meal);
        }

        System.out.printf("Philosopher %d leaves the table.%n", id);
    }

    private void philosophize() {
        state = PhilosopherState.PHILOSOPHIZING;
        System.out.printf("Philosopher %d is philosophizing.%n", id);
        pause();
    }

    private void eat(int mealNumber) {
        state = PhilosopherState.HUNGRY;
        System.out.printf("Philosopher %d is hungry.%n", id);

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

    private void pause() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(250, 750));
        } catch (InterruptedException ignored) {}
    }
}
