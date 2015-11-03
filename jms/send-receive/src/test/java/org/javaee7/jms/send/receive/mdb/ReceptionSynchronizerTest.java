package org.javaee7.jms.send.receive.mdb;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Patrik Dudits
 */
public class ReceptionSynchronizerTest {
    @Test
    public void testWaiting() throws NoSuchMethodException, InterruptedException {
        final ReceptionSynchronizer cut = new ReceptionSynchronizer();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        final Method method = ReceptionSynchronizerTest.class.getDeclaredMethod("testWaiting");
        long startTime = System.currentTimeMillis();
        pool.schedule(new Runnable() {

            @Override
            public void run() {
                cut.registerInvocation(method);
            }
        }, 1, TimeUnit.SECONDS);
        ReceptionSynchronizer.waitFor(ReceptionSynchronizerTest.class, "testWaiting");
        long waitTime = System.currentTimeMillis() - startTime;
        assertTrue("Waited more than 950ms", waitTime > 950);
        assertTrue("Waited no longer than 1050ms", waitTime < 1050);
    }
}
