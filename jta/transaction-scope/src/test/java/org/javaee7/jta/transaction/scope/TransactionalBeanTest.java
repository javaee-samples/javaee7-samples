/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jta.transaction.scope;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class TransactionalBeanTest {
    
    /**
     * Test of scenario1 method, of class MyTransactionalBean.
     */
    @Test
    public void testScenario1() {
        System.out.println("scenario1");
        MyTransactionalBean instance = new MyTransactionalBean();
        instance.scenario1();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of scenario2 method, of class MyTransactionalBean.
     */
    @Test
    public void testScenario2() {
        System.out.println("scenario2");
        MyTransactionalBean instance = new MyTransactionalBean();
        instance.scenario2();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of scenario3 method, of class MyTransactionalBean.
     */
    @Test
    public void testScenario3() {
        System.out.println("scenario3");
        MyTransactionalBean instance = new MyTransactionalBean();
        instance.scenario3();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
