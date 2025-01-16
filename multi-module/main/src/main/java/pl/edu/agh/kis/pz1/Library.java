package pl.edu.agh.kis.pz1;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Library {
    private final Semaphore readerSemaphore = new Semaphore(5); // Max 5 czytelników
    private final Semaphore libraryAccess = new Semaphore(1, true);
    private int readersInLibrary = 0;
    private Queue<Visitor> waitingQueue = new LinkedList<>();
    private List<String> inLibrary = new LinkedList<>();
    private boolean writersTime = false;


    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (waitingQueue) {
            if ( waitingQueue.peek() != null) {

                    Visitor visitor = waitingQueue.poll();

                    if (visitor.getClass() == Reader.class) {
                        while(writersTime || readersInLibrary >= 5) {
                            try{Thread.sleep((long) 30);} catch (InterruptedException e) {
                                System.err.println(e.getMessage());
                                Thread.currentThread().interrupt();

                            }
                        }

                        try{Thread.sleep((long) 30);} catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        }
                        System.out.println(visitor.name() + " może wchodzić");
                        visitor.setStatus(1);
                        notify();

                    } else if (visitor.getClass() == Writer.class) {

                        while(!inLibrary.isEmpty()) {
                            try{Thread.sleep((long) 30);} catch (InterruptedException e) {
                                System.err.println(e.getMessage());
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        writersTime = true;
                        try{Thread.sleep((long) 30);} catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        }
                        System.out.println(visitor.name() + " może wchodzić.");
                        visitor.setStatus(1);
                        notify();



                    }
                    try{Thread.sleep((long) 300);} catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            }
        }
    }
    public void requestReading(Reader reader)  {
        synchronized (waitingQueue) {
            waitingQueue.add(reader);
            System.out.println(reader.name() + " Dodany do kolejki");
            System.out.println("Kolejka do czytelni: " + waitingQueue.stream()
                    .map(Visitor::name) // Zamiana elementów na ich reprezentacje tekstowe
                    .reduce((a, b) -> a + ", " + b) // Połączenie elementów w jeden ciąg
                    .orElse("Kolejka jest pusta.")); // Obsługa pustej kolejki
        }


    }
    public void requestWriting(Writer writer) throws InterruptedException {
        synchronized (waitingQueue) {
            waitingQueue.add(writer);
            System.out.println(writer.name() + " Dodany do kolejki");
            System.out.println("Kolejka do czytelni: " + waitingQueue.stream()
                    .map(Visitor::name) // Zamiana elementów na ich reprezentacje tekstowe
                    .reduce((a, b) -> a + ", " + b) // Połączenie elementów w jeden ciąg
                    .orElse("Kolejka jest pusta.")); // Obsługa pustej kolejki
        }


    }
    public void startReading(Reader reader) throws InterruptedException {
        readerSemaphore.acquire(); // Czytelnik chce wejść
        synchronized (this) {
            readersInLibrary++;
            if (readersInLibrary == 1) {
                libraryAccess.acquire(); // Pierwszy czytelnik blokuje pisarzy
            }
            inLibrary.add(reader.name());
            System.out.println("Czytelnik czyta. Czytelników w czytelni: " + readersInLibrary + " czytelnicy:" + inLibrary);
        }

    }

    public void finishReading(Reader reader) throws InterruptedException {
        synchronized (this) {
            readersInLibrary--;
            if (readersInLibrary == 0) {
                try {Thread.sleep(30);}
                catch (InterruptedException e) {System.out.println(e);
                    Thread.currentThread().interrupt();
                }
                libraryAccess.release(); // Ostatni czytelnik odblokowuje pisarzy
            }
            inLibrary.remove(reader.name());
            System.out.println("Czytelnik wychodzi."+ reader.name() +" Czytelników w czytelni: " + readersInLibrary + "czytelnicy:" + inLibrary.toString());

        }

        readerSemaphore.release(); // Czytelnik wychodzi

    }

    public void startWriting(Writer writer) throws InterruptedException {
        libraryAccess.acquire(); // Pisarz zajmuje czytelnię na wyłączność

        inLibrary.add(writer.name());
        System.out.println("Pisarz " + writer.name() + " pisze.");
    }

    public void finishWriting(Writer writer) throws InterruptedException {
        synchronized(this) {
            System.out.println("Pisarz " + writer.name() + " kończy pisanie.");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                System.out.println(e);
                Thread.currentThread().interrupt();
            }
            libraryAccess.release(); // Pisarz opuszcza czytelnię
            inLibrary.remove(writer.name());
            writersTime = false;
        }
    }

    public void setInLibrary(List<String> inLibrary) {
        this.inLibrary = inLibrary;
    }

    public List<String> getInLibrary() {
        return inLibrary;
    }
}
