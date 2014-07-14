package org.javaee7.servlet.nonblocking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * @author Arun Gupta
 */
public class MyReadListener implements ReadListener {

    private ServletInputStream input = null;
    private AsyncContext context = null;

    public MyReadListener(ServletInputStream in, AsyncContext ac) {
        this.input = in;
        this.context = ac;
    }

    @Override
    public void onDataAvailable() {
        try {
            StringBuilder sb = new StringBuilder();
            int len = -1;
            byte b[] = new byte[1024];
            while (input.isReady()
                    && (len = input.read(b)) != -1) {
                String data = new String(b, 0, len);
                System.out.println("--> " + data);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyReadListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onAllDataRead() {
        System.out.println("onAllDataRead");
        context.complete();
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        context.complete();
    }
}
