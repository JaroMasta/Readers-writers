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



    public void requestReading(Reader reader) throws InterruptedException{
        synchronized (waitingQueue) {
            waitingQueue.add(reader);
            System.out.println(reader.name() + " Dodany do kolejki");
            System.out.println("Kolejka do czytelni: " + waitingQueue.stream()
                    .map(Visitor::name) // Zamiana elementów na ich reprezentacje tekstowe
                    .reduce((a, b) -> a + ", " + b) // Połączenie elementów w jeden ciąg
                    .orElse("Kolejka jest pusta.")); // Obsługa pustej kolejki
            while( (!waitingQueue.peek().equals(reader)) || writersTime || readersInLibrary >= 5 ){
               waitingQueue.wait();
            }

        }


    }

    public void startReading(Reader reader) throws InterruptedException {

        synchronized (waitingQueue) {
            readerSemaphore.acquire(); // Czytelnik chce wejść
            readersInLibrary++;
            if (readersInLibrary == 1) {
                libraryAccess.acquire(); // Pierwszy czytelnik blokuje pisarzy
            }
            waitingQueue.poll(); // usuwam z kolejki
            inLibrary.add(reader.name());
            System.out.println("Czytelnik "+ reader.name() +" czyta. Czytelników w czytelni: " + readersInLibrary + " czytelnicy: " + inLibrary);
        }

    }

    public void finishReading(Reader reader) throws InterruptedException {
        synchronized (waitingQueue) {
            waitingQueue.notifyAll(); //moge to dac gdzie chce dopóki jest w synchronized wykona sie ostatnie
            readersInLibrary--;
            if (readersInLibrary == 0) {
                libraryAccess.release(); // Ostatni czytelnik odblokowuje pisarzy
            }
            inLibrary.remove(reader.name());
            System.out.println("Czytelnik "+ reader.name() + " WYCHODZI" + " Czytelników w czytelni: " + readersInLibrary + "czytelnicy:" + inLibrary.toString());
            readerSemaphore.release(); // Czytelnik wychodzi
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
            while( (!waitingQueue.peek().equals(writer)) || !inLibrary.isEmpty()){
                waitingQueue.wait();
            }
        }


    }

    public void startWriting(Writer writer) throws InterruptedException {
        synchronized (waitingQueue) {
            writersTime = true;
            libraryAccess.acquire(); // Pisarz zajmuje czytelnię na wyłączność
            inLibrary.add(writer.name());
            waitingQueue.poll(); // usuwam z kolejki
            System.out.println("Pisarz " + writer.name() + " pisze." + " W bibliotece: " + inLibrary);
        }
    }

    public void finishWriting(Writer writer) throws InterruptedException {
        synchronized(waitingQueue) {
            System.out.println("Pisarz " + writer.name() + " kończy pisanie. " + " W bibliotece: " + inLibrary);
            libraryAccess.release(); // Pisarz opuszcza czytelnię
            inLibrary.remove(writer.name());
            writersTime = false;
            waitingQueue.notifyAll();
        }
    }

    public void setInLibrary(List<String> inLibrary) {
        this.inLibrary = inLibrary;
    }

    public List<String> getInLibrary() {
        return inLibrary;
    }
}
