package pl.edu.agh.kis.pz1;

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
