package pl.edu.agh.kis.pz1;

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
