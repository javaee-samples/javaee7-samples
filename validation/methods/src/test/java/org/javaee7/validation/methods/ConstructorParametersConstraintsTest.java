package org.javaee7.validation.methods;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

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
		return ShrinkWrap.create(JavaArchive.class).addClasses(MyBean2.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void constructorViolationsWhenNullParameters() throws NoSuchMethodException, SecurityException {
		ExecutableValidator methodValidator = validator.forExecutables();
		Constructor<MyBean2> constructor = MyBean2.class
				.getConstructor(String.class);

		Set<ConstraintViolation<MyBean2>> constraints = methodValidator
				.validateConstructorParameters(constructor, new Object[] {null});

		ConstraintViolation<MyBean2> violarion = constraints.iterator().next();
		assertThat(constraints.size(), equalTo(1));
		assertThat(violarion.getMessageTemplate(), equalTo("{javax.validation.constraints.NotNull.message}"));
		assertThat(violarion.getPropertyPath().toString(), equalTo("MyBean2.arg0"));
	}

	@Test
	public void constructorViolationsWhenNotNullParameters() throws NoSuchMethodException, SecurityException {
		ExecutableValidator methodValidator = validator.forExecutables();
		Constructor<MyBean2> constructor = MyBean2.class
				.getConstructor(String.class);

		Set<ConstraintViolation<MyBean2>> constraints = methodValidator
				.validateConstructorParameters(constructor, new Object[] {"foo"});

		assertThat(constraints.isEmpty(), equalTo(true));
	}

}
