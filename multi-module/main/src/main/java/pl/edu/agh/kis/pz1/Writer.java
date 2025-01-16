package pl.edu.agh.kis.pz1;
/**
 * Represents a writer in the readers and writers problem.
 * Extends the Thread class to simulate concurrent behavior.
 *
 * Responsibilities:
 * - Requests exclusive access to the library for writing.
 * - Logs actions such as entering, writing, and exiting the library.
 * - Ensures fair access to the resource without starving readers.
 */
public class Writer extends Visitor implements Runnable{
    private final Library library;

    public Writer(Library library, String name) {
        super(name);
        this.library = library;
    }


    public void run() {
        try {
            while (true) {
                System.out.println(name() + " chce pisać.");
                library.requestWriting(this);
                library.startWriting(this);
                Thread.sleep((long) (Math.random() * 3000)); // Symulacja pisania (1-3 sekundy)
                library.finishWriting(this);
            }
        } catch (InterruptedException e) {
            System.out.println(name() + " został przerwany.");
        }
    }
}
