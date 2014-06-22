package org.javaee7.batch.chunk.checkpoint;

import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemWriter extends AbstractItemWriter {

    @Override
    public void writeItems(List list) throws Exception {
        System.out.println(list);
    }
}
