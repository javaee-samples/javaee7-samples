package org.javaee7.cdi.instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeting.class, SimpleGreeting.class, FancyGreeting.class)
				.addAsManifestResource("beans.xml");
	}

	@Inject
	private Instance<Greeting> instance;

	@Test
	public void test() throws Exception {
		// there should be both request scoped bean instances available
		assertThat(instance, containsInAnyOrder(instanceOf(SimpleGreeting.class), instanceOf(FancyGreeting.class)));
	}
}

