package pl.edu.agh.kis.pz1;

import java.util.Scanner;

public class Main {
    /**
     * The entry point of the readers and writers problem simulation.
     *
     * Functionality:
     * - Initializes the library resource.
     * - Creates threads for readers and writers.
     * - Starts and manages the simulation.
     *
     * Usage:
     * Run the program with optional command-line arguments specifying the number of readers and writers.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj liczbę czytelników (Enter dla domyślnej wartości 10):");
        String readersInput = scanner.nextLine();
        int numberOfReaders = readersInput.isEmpty() ? 10 : Integer.parseInt(readersInput);

        System.out.println("Podaj liczbę pisarzy (Enter dla domyślnej wartości 3):");
        String writersInput = scanner.nextLine();
        int numberOfWriters = writersInput.isEmpty() ? 3 : Integer.parseInt(writersInput);

        Library library = new Library();

        // Tworzenie czytelników
        for (int i = 1; i <= numberOfReaders; i++) {
            new Thread(new Reader(library, "Czytelnik-" + i)).start();
        }

        // Tworzenie pisarzy
        for (int i = 1; i <= numberOfWriters; i++) {
            new Thread(new Writer(library, "Pisarz-" + i)).start();
        }
    }
}