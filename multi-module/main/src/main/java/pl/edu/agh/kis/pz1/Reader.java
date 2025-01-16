package pl.edu.agh.kis.pz1;
/**
 * Represents a reader in the readers and writers problem.
 * Extends the Thread class to simulate concurrent behavior.
 *
 * Responsibilities:
 * - Requests access to the library for reading.
 * - Logs actions such as entering, reading, and exiting the library.
 * - Ensures fair access to the resource without starving writers.
 */
public class Reader extends Visitor implements Runnable {
    private final Library library;

    public Reader(Library library, String name) {
        super(name);
        this.library = library;
    }

    public void run() {
        try {
            while(true) {
                System.out.println(name() + " chce czytać.");
                library.requestReading(this);
                library.startReading(this);
                Thread.sleep((long) (Math.random() * 3000)); // Symulacja czytania (1-3 sekundy)
                library.finishReading(this);
            }
        } catch (InterruptedException e) {
            System.out.println(name() + " został przerwany.");
        }
    }

}
