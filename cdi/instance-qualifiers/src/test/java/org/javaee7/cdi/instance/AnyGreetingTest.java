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
public class AnyGreetingTest {
	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeting.class, SimpleGreeting.class, FormalGreeting.class, Business.class, Personal.class)
				.addAsManifestResource("beans.xml");
	}

	/**
	 * Built-in qualifier @Any is assumed on each bean regardless other qualifiers specified.
	 */
	@Inject @Any
	private Instance<Greeting> instance;

	/**
	 * Both bean instances of Greeting interface should be available.<br/>
	 *
	 * When dependent scoped bean is retrieved via an instance then explicit destroy action should be taken.
	 * This is a known memory leak in CDI 1.0 fixed in CDI 1.1 see the link bellow for details.
	 *
	 * @see  <a href="https://issues.jboss.org/browse/CDI-139">CDI-139</a>
	 */
	@Test
	public void test() throws Exception {
		assertFalse(instance.isUnsatisfied());
		assertTrue(instance.isAmbiguous());

		// use Instance<T>#select()
		Greeting businessBean = instance.select(new AnnotationLiteral<Business>() {}).get();
		assertThat(businessBean, instanceOf(FormalGreeting.class));
		instance.destroy(businessBean);

		Greeting defaultBean = instance.select(new AnnotationLiteral<Default>() {}).get();
		assertThat(defaultBean, instanceOf(SimpleGreeting.class));
		instance.destroy(defaultBean);
	}
}

