package org.javaee7.ejb.async;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
@Asynchronous
public class MyAsyncBeanClassLevel {

    public Future<Integer> addNumbers(int n1, int n2) {
        try {
            // simulating a long running query
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyAsyncBeanClassLevel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new AsyncResult(n1 + n2);
    }
    
    public void doSomething(String what) {
        System.out.println("Just did " + what);
    }
}
