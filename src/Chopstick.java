public class Chopstick {

    private final int id;
    private Philosopher owner;

    public Chopstick(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public synchronized void pickUp(Philosopher philosopher) {
        while (owner != null) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        owner = philosopher;
    }

    public synchronized void putDown(Philosopher philosopher) {
        if (owner == philosopher) {
            owner = null;
            notifyAll();
        }
    }

    public synchronized Philosopher getOwner() {
        return owner;
    }
}
