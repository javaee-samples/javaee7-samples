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
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Constructor;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class ConstructorParametersConstraintsTest {

    @Inject
    Validator validator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(MyBean2.class, MyParameter.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void constructorViolationsWhenNullParameters() throws NoSuchMethodException, SecurityException {
        final MyParameter parameter = new MyParameter();

        ExecutableValidator methodValidator = validator.forExecutables();
        Constructor<MyBean2> constructor = MyBean2.class
            .getConstructor(parameter.getClass());

        Set<ConstraintViolation<MyBean2>> constraints = methodValidator
            .validateConstructorParameters(constructor, new Object[] { parameter });

        ConstraintViolation<MyBean2> violation = constraints.iterator().next();
        assertThat(constraints.size(), equalTo(1));
        assertThat(violation.getMessageTemplate(), equalTo("{javax.validation.constraints.NotNull.message}"));
        assertThat(violation.getPropertyPath().toString(), equalTo("MyBean2.arg0.value"));
    }

    @Test
    public void constructorViolationsWhenNotNullParameters() throws NoSuchMethodException, SecurityException {
        final MyParameter parameter = new MyParameter();
        parameter.setValue("foo");

        ExecutableValidator methodValidator = validator.forExecutables();
        Constructor<MyBean2> constructor = MyBean2.class
            .getConstructor(parameter.getClass());

        Set<ConstraintViolation<MyBean2>> constraints = methodValidator
            .validateConstructorParameters(constructor, new Object[] { parameter });

        assertThat(constraints.isEmpty(), equalTo(true));
    }

}
