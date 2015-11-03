package org.javaee7.concurrency.managedthreadfactory;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class MyTaskTest {

    @Resource
    ManagedThreadFactory factory;

    @Resource(name = "DefaultManagedThreadFactory")
    ManagedThreadFactory factory2;

    /**
     * Test of run method, of class MyTask.
     * 
     * using JNDI lookup
     */
    //    @Test
    public void testJNDILookup() {
        try {
            InitialContext ctx = new InitialContext();

            //            ManagedExecutorService executor = (ManagedExecutorService) ctx.lookup("concurrent/myExecutor");
            ManagedThreadFactory myFactory = (ManagedThreadFactory) ctx.lookup("java:comp/DefaultManagedThreadFactory");
            assertNotNull(myFactory);
            Thread thread = myFactory.newThread(new MyTask(1));
            assertNotNull(thread);
            thread.start();
        } catch (NamingException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of run method, of class MyTask.
     * 
     * using @Resource, with no name
     */
    //    @Test
    public void testResourceNoName() {
        Thread thread = factory.newThread(new MyTask(1));
        assertNotNull(thread);
        thread.start();
    }

    /**
     * Test of run method, of class MyTask.
     * 
     * using @Resource, with no name
     */
    //    @Test
    public void testResourceWithName() {
        Thread thread = factory2.newThread(new MyTask(1));
        assertNotNull(thread);
        thread.start();
    }
}
