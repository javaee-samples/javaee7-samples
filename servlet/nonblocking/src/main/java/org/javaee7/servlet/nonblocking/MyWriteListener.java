package org.javaee7.servlet.nonblocking;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * @author Arun Gupta
 */
public class MyWriteListener implements WriteListener {

    private ServletOutputStream output = null;
    private AsyncContext context = null;

    public MyWriteListener(ServletOutputStream out, AsyncContext ac) {
        this.output = out;
        this.context = ac;
    }

    @Override
    public void onWritePossible() {
        if (output.isReady()) {
            try {
                byte[] b = new byte[100];
                Arrays.fill(b, 0, 100, (byte) 'a');
                output.write(b);
            } catch (IOException ex) {
                Logger.getLogger(MyWriteListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        context.complete();
    }
}
