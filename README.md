# Project: Readers and Writers
# TODO:
This implementation is very defensive, try using built-in waiting in fair semaphore,
do not wait in requestWriting/requestReading instead rely on fair semaphore (it implements a FIFO queue)
## 1. Description of the Implemented Algorithm

The project implements the classical synchronization problem known as the "Readers and Writers Problem". The solution is based on the following rules:

- A maximum of 5 readers can access the library simultaneously.
- Only one writer can access the library at a time, exclusively.
- No thread (reader or writer) will experience starvation, assuming equal thread priority and a reasonable execution time (maximum of 3 seconds).

The `java.util.concurrent.Semaphore` class is used to ensure resource access synchronization.

Thread communication relies on logging events such as:

- Attempt to enter the library (reader/writer).
- Current number of readers and writers in the library and in the queue.
- Entry and exit from the library by readers and writers.

Each thread (reader or writer) operates in an infinite loop, performing tasks with random delays (1 to 3 seconds).

## 2. How to Run the Program

The program can be run from the command line using the following command:

```bash
java -jar package-name.jar [number_of_readers] [number_of_writers]
```
## 3. Communication Protocol Overview

Communication between threads is based on event logging. The following messages are possible:

### Messages Sent by Reader Threads:
- **"Reader X wants to enter the library."**  
  - **Parameters:** Thread identifier (X).  
  - **Situation:** The thread attempts to reserve a spot in the library.

- **"Reader X is reading in the library."**  
  - **Parameters:** Thread identifier (X).  
  - **Situation:** The thread has entered the library and is performing its task.

- **"Reader X leaves the library."**  
  - **Parameters:** Thread identifier (X).  
  - **Situation:** The thread finishes using the resource.

### Messages Sent by Writer Threads:
- **"Writer Y wants to enter the library."**  
  - **Parameters:** Thread identifier (Y).  
  - **Situation:** The thread attempts to reserve exclusive access to the library.

- **"Writer Y is writing in the library."**  
  - **Parameters:** Thread identifier (Y).  
  - **Situation:** The thread has entered the library and is performing its task.

- **"Writer Y leaves the library."**  
  - **Parameters:** Thread identifier (Y).  
  - **Situation:** The thread finishes using the resource.

Each message includes the current state of the resource (number of readers and writers in the library and in the queue).

---

## 4. Other Relevant Information

- The project includes **unit tests** to verify synchronization correctness and prevent thread starvation.
- **Class documentation (Javadoc)** is available in the `javadoc` directory.
- The **SonarQube analysis report** is located in the `sonar-cube` directory.

### Recommended Runtime Environment:
- **Java 17** or later.
