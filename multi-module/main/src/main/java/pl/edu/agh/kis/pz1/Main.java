package pl.edu.agh.kis.pz1;

import java.util.Scanner;

public class Main {
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