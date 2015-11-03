package org.javaee7.el.standalone;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.el.ELProcessor;

import static org.junit.Assert.assertEquals;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class ELResolverTest {

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class);
    }

    private ELProcessor elProcessor;

    @Before
    public void setup() {
        elProcessor = new ELProcessor();
    }

    @Test
    public void should_pick_in_the_array() {
        Object result = elProcessor.eval("a = [1, 2, 3]; a[1]");
        assertEquals(2L, result);
    }

    @Test
    public void should_add() {
        Object result = elProcessor.eval("((x,y) -> x+y)(4, 5)");
        assertEquals(9L, result);
    }
}
