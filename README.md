# Dining Philosophers

This project is a Java implementation of the classic dining philosophers concurrency problem. It simulates a group of philosophers sitting around a table, alternating between thinking and eating while sharing a limited number of chopsticks.

The program demonstrates how multiple threads compete for shared resources and how synchronization can be used to coordinate access safely. Each philosopher runs on its own thread, each chopstick is modeled as a shared synchronized object, and the solution prevents deadlock by always acquiring the lower-numbered chopstick first.

When the program runs, philosophers repeatedly:

- philosophize
- become hungry
- pick up two chopsticks
- eat
- put the chopsticks down

The simulation ends after each philosopher has eaten a fixed number of meals.

Additional project documentation is available in the `documentation` folder, including UML and requirement-mapping files.

---

## Requirements

- [Java JDK 21.0.7](https://www.oracle.com/java/technologies/downloads/)
- [Apache Ant 1.10.15](https://ant.apache.org/bindownload.html)
- A terminal, command prompt, or IDE capable of compiling and running Java code or ANT

---

## How to Run

You can run the project with Apache Ant from the root directory:

```powershell
ant run
```

This will:

- compile the Java source files in `src`
- place the compiled `.class` files in `build`
- run the `DiningTable` main class

If you do not want to use Ant, you can compile and run it manually:

```powershell
javac -d build src\*.java
java -cp build DiningTable
```

---

## Expected Output

The program prints status messages showing what each philosopher is doing. Because the philosophers run on separate threads, the exact order of lines will vary from run to run.

Typical output will look similar to this:

```text
Philosopher 0 is philosophizing.
Philosopher 3 is philosophizing.
Philosopher 1 is hungry.
Philosopher 1 picked up chopstick 1.
Philosopher 1 picked up chopstick 2.
Philosopher 1 is eating meal 1.
Philosopher 1 put down chopstick 2.
Philosopher 1 put down chopstick 1.
Philosopher 1 leaves the table.
```

You should expect to see messages for:

- philosophers philosophizing
- philosophers becoming hungry
- chopsticks being picked up and put down
- meals being eaten
- philosophers leaving the table after finishing all meals

The exact sequence is non-deterministic, but the program should complete without deadlocking.

---

## Final Comments

This project demonstrates the core ideas behind concurrent programming in a compact and understandable way. The exact print order will vary between runs because thread scheduling is non-deterministic, but the program is designed to avoid deadlock and finish cleanly after all philosophers complete their meals.
