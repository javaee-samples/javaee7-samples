package org.javaee7.validation.methods;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class ConstructorParametersInjectionTest {

    @Inject
    MyBean2 bean;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(MyBean2.class, MyParameter.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void constructorViolationsWhenNullParameters() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("javax.validation.constraints.NotNull");
        thrown.expectMessage("MyBean2.arg0.value");
        bean.getValue();
    }

}
