# Project Challenges

---

This project involved several common concurrency challenges that had to be handled carefully to produce a working dining philosophers solution.

---

## Coordinating multiple threads safely

The first major challenge was making sure several philosophers could run at the same time without corrupting shared state.

- Every philosopher runs on its own thread.
- Chopsticks are shared resources between neighboring philosophers.
- Without synchronization, two philosophers could attempt to use the same chopstick at once.

This was addressed by making each `Chopstick` act as its own monitor and synchronizing the `pickUp()` and `putDown()` methods.

---

## Preventing deadlock

Deadlock is the most well-known challenge in the dining philosophers problem.

- If every philosopher picks up one chopstick first and waits for the second, the program can freeze permanently.
- That kind of circular wait is easy to create in a naive implementation.

This project solves that by forcing philosophers to always pick up the lower-numbered chopstick first. That consistent ordering removes the circular dependency that causes deadlock.

---

## Releasing resources reliably

Another challenge was making sure chopsticks are always released, even if a thread is interrupted or something unexpected happens during the eating sequence.

- If a philosopher keeps one chopstick locked by mistake, nearby philosophers may block indefinitely.
- Resource leaks in concurrent code are especially hard to debug because they often look like random stalls.

This was handled with `try`/`finally` blocks in `Philosopher.eat()`, which guarantees that chopsticks are put down in the correct order after use.

---

## Tracking when the simulation is finished

It is not enough to start the threads; the main program also needs to know when all philosophers are done.

- If `main` exits too early, the simulation would not be managed cleanly.
- If completion is tracked incorrectly, the program could wait forever.

This challenge was solved in `DiningTable` by counting completed philosophers and using `wait()` and notification so the table can pause until all threads finish.
