/**
 * Enumerates the possible high-level states for a philosopher.
 */
public enum PhilosopherState {
    /** The philosopher is waiting to acquire both chopsticks. */
    HUNGRY,
    /** The philosopher currently holds both chopsticks and is eating. */
    EATING,
    /** The philosopher is thinking between meals. */
    PHILOSOPHIZING
}
