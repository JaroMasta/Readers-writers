package pl.edu.agh.kis.pz1;
/**
 * Represents a generic visitor in the readers and writers problem.
 * Serves as a base class for specific visitor types like Reader and Writer.
 *
 * Responsibilities:
 * - Provides common properties and methods shared by readers and writers.
 */
public abstract class Visitor{
    protected int status;
    String name;
    protected Visitor(String name) { // 1 for in library 0 for not in library
        this.name = name;
    }
    public String name(){
        return this.name;
    }

}
