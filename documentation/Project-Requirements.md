# Dining Philosophers Requirements Mapping

---

This project satisfies the core requirements of the dining philosophers problem by modeling independent philosophers, shared chopsticks, synchronized resource access, and a deadlock-avoidance strategy.

---

## Multiple philosophers run concurrently

The program creates one `Thread` per philosopher in `DiningTable`.

- `DiningTable` builds a `Philosopher[]` and a matching `Thread[]`.
- `startDinner()` starts every philosopher thread.
- Each philosopher executes independently through the `run()` method.

This matches the requirement that philosophers behave as concurrent actors rather than as sequential method calls.

---

## Philosophers alternate between thinking and eating

Each philosopher follows the expected dining philosophers life cycle.

- In `Philosopher.run()`, each philosopher repeats a loop for the configured number of meals.
- `philosophize()` sets the state to `PHILOSOPHIZING`, prints status, and pauses.
- `eat()` sets the state to `HUNGRY`, acquires chopsticks, changes to `EATING`, and pauses again.

This implements the standard think -> hungry -> eat cycle.

---

## Shared resources must be protected from simultaneous use

Each chopstick is represented by a shared `Chopstick` object with synchronized access.

- `Chopstick.pickUp()` is `synchronized`.
- If the chopstick already has an owner, the calling philosopher waits.
- When the chopstick becomes available, ownership is assigned to exactly one philosopher.
- `Chopstick.putDown()` clears the owner and notifies waiting threads.

This ensures that a chopstick cannot be used by more than one philosopher at the same time.

---

## A philosopher must hold both chopsticks before eating

The code enforces this directly in `Philosopher.eat(int mealNumber)`.

- The philosopher picks up the first chopstick.
- The philosopher then picks up the second chopstick.
- The state changes to `EATING` only after both chopsticks are held.
- Both chopsticks are released in `finally` blocks, which also protects against partial-release bugs.

This matches the requirement that eating only occurs when both adjacent resources are secured.

---

## Avoid deadlock

This implementation prevents deadlock by imposing a global resource-ordering rule.

- Each philosopher compares the two chopstick IDs.
- The lower-numbered chopstick is always picked up first.
- Because all philosophers follow the same ordering rule, circular wait is eliminated.

Deadlock in the dining philosophers problem typically occurs when every philosopher holds one chopstick and waits forever for the other. That circular dependency cannot form here because all threads request resources in a consistent order.

---

## Support waiting and notification between competing threads

The program uses Java monitor methods correctly for shared-resource coordination.

- `wait()` is used in `Chopstick.pickUp()` while a chopstick is owned.
- `notifyAll()` is used in `Chopstick.putDown()` so blocked philosophers can retry acquisition.
- `DiningTable` also uses `wait()`, `notify()`, and `notifyAll()` to track simulation completion.

This demonstrates thread coordination rather than busy-waiting.

---

## End the simulation cleanly

The program provides a clean completion path.

- Every philosopher stops after eating the configured number of meals.
- Each philosopher thread reports completion through `DiningTable.philosopherFinished()`.
- `DiningTable.startDinner()` waits until all philosophers have finished.
- The `main` method returns only after the full simulation is complete.

This satisfies the expectation that the program runs the scenario and terminates in a controlled way.

---

## Represent philosopher status clearly

The implementation includes explicit philosopher states through `PhilosopherState`.

- `PHILOSOPHIZING`
- `HUNGRY`
- `EATING`

These states make the simulation behavior easier to understand and verify.

---

## Summary

The code meets the dining philosophers requirements because it:

- models philosophers as concurrent threads
- models chopsticks as shared synchronized resources
- requires both chopsticks before eating
- uses `wait()` and notification for contention
- prevents deadlock through ordered resource acquisition
- terminates after all philosophers complete their meals

