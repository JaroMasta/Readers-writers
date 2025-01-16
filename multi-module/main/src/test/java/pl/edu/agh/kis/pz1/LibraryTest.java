package pl.edu.agh.kis.pz1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;
    private ByteArrayOutputStream consoleOutput;
    private PrintStream originalOut;
    private List<Thread> threads;
    @BeforeEach
    void setUp() {
        library = new Library();
        threads = new ArrayList<>();
    }
    void cleanUp(List<Thread> threads) throws InterruptedException {
        threads.forEach(Thread::interrupt); // Przerwanie wątków czytelników i pisarzy
        for (Thread thread : threads) {
            thread.join();
        }
        threads.clear();
    }



    @ParameterizedTest
    @MethodSource("provideReaderAndWriterConfigs")
    void testWriterExclusivityInConsoleOutput(int numReaders, int numWriters) throws InterruptedException {
        int readersLeft = numReaders;
        int writersLeft = numWriters;
        consoleOutput = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(consoleOutput));
        while (readersLeft > 0 || writersLeft > 0) {
            if (Math.random() < 0.5 && readersLeft > 0) {
                Reader reader = new Reader(library, "Reader-" + (numReaders - readersLeft + 1));
                Thread thread = new Thread(reader);
                threads.add(thread);
                thread.start();
                readersLeft--;
            }
            if (Math.random() < 0.5 && writersLeft > 0) {
                Writer writer = new Writer(library, "Writer-" + (numWriters - writersLeft + 1));
                Thread thread = new Thread(writer);
                threads.add(thread);
                thread.start();
                writersLeft--;

            }
            Thread.sleep(50);
        }

        Thread.sleep(1000);



        TimeUnit.MILLISECONDS.sleep(10000);
        cleanUp(threads);
        String output = consoleOutput.toString();
        if (numWriters > 0) assertTrue(output.contains("pisze") && output.contains("kończy pisanie"), "Output should contain writer actions.  " + output);

        int writeStartIndex = 0;
        int writeEndIndex = 0;

        while (((writeStartIndex = output.indexOf("pisze", writeEndIndex)) != -1) ) {
            writeEndIndex = output.indexOf("kończy pisanie", writeStartIndex);
            if (writeEndIndex == -1) break;
            assertTrue(writeEndIndex > writeStartIndex, "Writer start should appear before writer end in output. " + output);

            String between = output.substring(writeStartIndex, writeEndIndex);
            assertFalse(between.contains(" czyta ") || between.contains(" WYCHODZI "),
                    "No reader actions should appear between writer actions but between them is: " + between + "WHOLE OUTPUT \n \n \n:" + output);
        }
        }
    private static Stream<Arguments> provideReaderAndWriterConfigs() {
        return Stream.of(
            Arguments.of(10, 3), Arguments.of(5, 1)

        );
    }
    @Test
    void testAddReaderToLibraryWithThreads() throws InterruptedException {
        Reader reader = new Reader(library, "Reader-1");
        Thread thread = new Thread(reader);
        threads.add(thread);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(500);
        assertTrue(library.getInLibrary().contains("Reader-1"), "Reader should be in the library.");
        cleanUp(threads);
    }






    @Test
    void testLibrarySize() throws InterruptedException {

        // Add 5 readers
        for (int i = 1; i <= 5; i++) {
            Reader reader = new Reader(library, "Reader-" + i);
            Thread thread = new Thread(reader);
            threads.add(thread);
            thread.start();
        }

        // Add 1 writer
        Writer writer = new Writer(library, "Writer-1");
        Thread thread = new Thread(writer);
        threads.add(thread);
        thread.start();

        TimeUnit.MILLISECONDS.sleep(10000);
        assertTrue(5 > library.getInLibrary().size(), "Library should contain less than  6 readers." + library.getInLibrary().size());
        cleanUp(threads);
    }

}
