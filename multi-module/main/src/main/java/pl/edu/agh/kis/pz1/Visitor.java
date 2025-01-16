package pl.edu.agh.kis.pz1;

public abstract class Visitor extends Thread{
    protected int status;
    String name;
    protected Visitor(String name, int status) { // 1 for in library 0 for not in library
        this.name = name;
        this.status = status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String name(){
        return this.name;
    }
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
