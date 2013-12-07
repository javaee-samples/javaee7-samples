package org.javaee7.ejb.async;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Arun Gupta
 */
@Stateless
@Asynchronous
public class MyAsyncBeanClassLevel {

    public static final long AWAIT = 3000;

    public Future<Integer> addNumbers(int n1, int n2) {
        try {
            // simulating a long running query
            Thread.sleep(AWAIT);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyAsyncBeanClassLevel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new AsyncResult(n1 + n2);
    }

}
