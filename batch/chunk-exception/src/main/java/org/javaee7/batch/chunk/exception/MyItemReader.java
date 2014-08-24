package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Named;
import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemReader extends AbstractItemReader {

    private StringTokenizer tokens;

    private MyInputRecord lastElement;
    private boolean alreadyFailed;

    @Override
    public void open(Serializable checkpoint) {
        tokens = new StringTokenizer("1,2,3,4,5,6,7,8,9,10", ",");

        // This will place the nextToken into the last batch checkpoint. Called on exception retry.
        if (checkpoint != null) {
            while (!Integer.valueOf(tokens.nextToken()).equals(((MyInputRecord) checkpoint).getId())) {
                System.out.println("Skipping already read elements");
            }
        }
    }

    @Override
    public Object readItem() {
        if (tokens.hasMoreTokens()) {
            int token = Integer.valueOf(tokens.nextToken());

            // Simulate a read exception when the token is equal to 5. Do it once only.
            if (token == 5 && !alreadyFailed) {
                alreadyFailed = true;
                throw new IllegalArgumentException("Could not read record");
            }

            lastElement = new MyInputRecord(token);
            System.out.println("MyItemReader.readItem " + lastElement);
            return lastElement;
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        // This is used internally by batch to stop the retry. Remember to implement equals on the read elements.
        return lastElement;
    }
}
