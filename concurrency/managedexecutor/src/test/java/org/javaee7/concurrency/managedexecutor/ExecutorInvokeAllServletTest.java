/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javaee7.concurrency.managedexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
//@RunWith(Arquillian.class)
public class ExecutorInvokeAllServletTest {

    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService executor;

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
//    @Deployment
//    @TargetsContainer("wildfly-arquillian")
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(MyCallableTask.class).
                addClass(Product.class);
        System.out.println(war.toString(true));

        return war;
    }

    Collection<Callable<Product>> tasks = new ArrayList<>();

    public ExecutorInvokeAllServletTest() {
        for (int i = 0; i < 5; i++) {
            tasks.add(new MyCallableTask(i));
        }
    }

    /**
     * Test of invokeAll method.
     */
//    @Test
    public void testProcessRequest() throws Exception {
        List<Future<Product>> results = executor.invokeAll(tasks);
        int count = 0;
        for (Future<Product> f : results) {
            assertEquals(count++, f.get().getId());
        }
    }

}
