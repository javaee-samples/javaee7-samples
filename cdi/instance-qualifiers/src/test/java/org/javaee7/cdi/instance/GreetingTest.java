package org.javaee7.cdi.instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeting.class, SimpleGreeting.class, FormalGreeting.class, Business.class, Personal.class)
				.addAsManifestResource("beans.xml");
	}

	/**
	 * Container will assume built-in @Default qualifier here as well as for beans that don't declare a qualifier.
	 */
	@Inject
	private Instance<Greeting> defaultInstance;

	/**
	 * Qualifier @Personal is not qualifying any bean.
	 */
	@Inject @Personal
	private Instance<Greeting> personalInstance;

	/**
	 * Built-in qualifier @Any is assumed on each bean regardless other qualifiers specified.
	 */
	@Inject @Any
	private Instance<Greeting> anyInstance;

	@Test
	public void test() throws Exception {
		// only SimpleGreeting instance should be available
		assertFalse(defaultInstance.isUnsatisfied());
		assertFalse(defaultInstance.isAmbiguous());
		assertThat(defaultInstance.get(), instanceOf(SimpleGreeting.class));
		assertThat(defaultInstance.select(new AnnotationLiteral<Default>() {}).get(), instanceOf(SimpleGreeting.class));

		// no instance should be available
		assertTrue(personalInstance.isUnsatisfied());

		// both Greeting instances should be available
		assertFalse(anyInstance.isUnsatisfied());
		assertTrue(anyInstance.isAmbiguous());
		assertThat(anyInstance.select(new AnnotationLiteral<Business>() {}).get(), instanceOf(FormalGreeting.class));
		assertThat(anyInstance.select(new AnnotationLiteral<Default>() {}).get(), instanceOf(SimpleGreeting.class));
	}
}

