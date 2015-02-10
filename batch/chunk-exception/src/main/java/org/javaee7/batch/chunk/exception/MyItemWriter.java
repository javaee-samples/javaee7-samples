package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import java.util.List;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemWriter extends AbstractItemWriter {
    private static int retries = 0;

    @Override
    public void writeItems(List list) {
        if (retries <= 3 && list.contains(new MyOutputRecord(8))) {
            retries++;
            System.out.println("Throw UnsupportedOperationException in MyItemWriter");
            throw new UnsupportedOperationException();
        }

        System.out.println("MyItemWriter.writeItems: " + list);
    }
}
