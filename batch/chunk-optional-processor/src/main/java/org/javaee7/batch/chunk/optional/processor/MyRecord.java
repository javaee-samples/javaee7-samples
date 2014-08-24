package org.javaee7.batch.chunk.optional.processor;

/**
 * @author Arun Gupta
 */
public class MyRecord {
    private int id;
            
    public MyRecord() { }
    
    public MyRecord(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "MyInputRecord: " + id;
    }
}
