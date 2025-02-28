# Juice Bottling System

## Overview
This Java-based multithreaded system simulates an automated orange juice bottling plant. The program consists of three main classes:

1. **Orange** - Represents an orange and its processing stages.
2. **Plant** - Represents a juice processing plant with multiple worker threads.
3. **MultiPlant** - Manages multiple plants processing oranges simultaneously.

## Class Descriptions

### Orange.java
The `Orange` class models the lifecycle of an orange as it moves through different processing stages:
- **Fetched** 
- **Peeled** 
- **Squeezed** 
- **Bottled**
- **Processed** 

#### Key Features:
- Uses an enum `State` to track the processing stage.
- Implements a `runProcess()` method to transition between states.
- Simulates processing time using `Thread.sleep()`.

### Plant.java
The `Plant` class represents a juice bottling facility. Each plant runs multiple worker threads to process oranges.

#### Key Features:
- Uses **multithreading** to process oranges in parallel.
- Allows multiple workers per plant (configured via `PLANT_THREADS`).
- Tracks the number of oranges processed, bottled, and wasted.
- Uses `synchronized` methods to ensure thread safety.

#### Execution Flow:
1. Multiple `Plant` instances are created.
2. Each plant runs multiple worker threads.
3. Workers continuously fetch and process oranges until the plant is stopped.
4. Results (total oranges processed, bottles produced, waste) are displayed.

### MultiPlant.java
The `MultiPlant` class manages multiple `Plant` instances and runs them in parallel.

#### Key Features:
- Manages multiple plants, each with its own workers.
- Controls the lifecycle of plants (start, process, stop).
- Aggregates and summarizes processing results.

#### Execution Flow:
1. Creates multiple `Plant` instances.
2. Starts processing in each plant.
3. Waits for a fixed processing time.
4. Stops plants and collects results.

## How to Run
1. Compile all Java files:
   ```sh
   javac Orange.java Plant.java MultiPlant.java
   ```
2. Run the `MultiPlant` class:
   ```sh
   java MultiPlant
   ```
3. The program will run for a set duration, process oranges, and output statistics.

## References
- [Multithreading in Java (GeeksforGeeks)](https://www.geeksforgeeks.org/multithreading-in-java/)
- [Java Multithreading (TutorialsPoint)](https://www.tutorialspoint.com/java/java_multithreading.htm)

