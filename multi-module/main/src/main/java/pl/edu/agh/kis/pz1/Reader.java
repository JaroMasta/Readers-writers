package pl.edu.agh.kis.pz1;

public class Reader extends Visitor {
    private final Library library;

    public Reader(Library library, String name) {
        super(name, 0);
        this.library = library;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(name() + " chce czytać.");
                library.requestReading(this);
                while(status != 1 && !Thread.currentThread().isInterrupted()) {
                   Thread.sleep((long) 50); // Symulacja czekania na czytanie
                }

                library.startReading(this);
                Thread.sleep((long) 3000); // Symulacja czytania (1-3 sekundy)
                library.finishReading(this);
                this.setStatus(0);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " został przerwany.");
            Thread.currentThread().interrupt();
        }
    }
}
