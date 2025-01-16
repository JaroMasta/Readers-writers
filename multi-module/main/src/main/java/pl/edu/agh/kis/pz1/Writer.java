package pl.edu.agh.kis.pz1;

public class Writer extends Visitor {
    private final Library library;

    public Writer(Library library, String name) {
        super(name, 0);
        this.library = library;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(name() + " chce pisać.");
                library.requestWriting(this);
                while(status != 1 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep((long) 50); // Symulacja czekania na pisanie
                }

                library.startWriting(this);
                Thread.sleep((long) 3000); // Symulacja pisania (1-3 sekundy)
                library.finishWriting(this);
                this.setStatus(0);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " został przerwany.");
            Thread.currentThread().interrupt();
        }
    }
}
