/**
 * Represents a single shared chopstick at the dining table.
 * <p>
 * A chopstick acts as its own monitor. Philosophers acquire it with
 * {@link #pickUp(Philosopher)} and release it with {@link #putDown(Philosopher)}.
 */
public class Chopstick {

    private final int id;
    private Philosopher owner;

    /**
     * Creates a chopstick with a stable identifier.
     *
     * @param id the chopstick identifier used for output and ordering
     */
    public Chopstick(int id) {
        this.id = id;
    }

    /**
     * Returns the identifier assigned to this chopstick.
     *
     * @return the chopstick identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Waits until the chopstick is available, then assigns ownership to the
     * requesting philosopher.
     *
     * @param philosopher the philosopher attempting to acquire the chopstick
     */
    public synchronized void pickUp(Philosopher philosopher) {
        while (owner != null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        owner = philosopher;
    }

    /**
     * Releases the chopstick if it is currently owned by the provided
     * philosopher and wakes any waiting philosophers.
     *
     * @param philosopher the philosopher releasing the chopstick
     */
    public synchronized void putDown(Philosopher philosopher) {
        if (owner == philosopher) {
            owner = null;
            notifyAll();
        }
    }

}
