package org.javaee7.cdi.beanmanager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.naming.InitialContext;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(Greeting.class, SimpleGreeting.class, SmileyGreeting.class)
				.addAsManifestResource("beans.xml");
	}

    // First way to get BeanManager
	@Inject
	private BeanManager bm;

	@Test
	public void testInject() throws Exception {
		test(this.bm);
	}

	@Test
	public void testCurrent() throws Exception {
		// Second way to get BeanManager: current CDI container
		BeanManager bm = CDI.current().getBeanManager();

		test(bm);
	}

	@Test
	public void testJNDI() throws Exception {
		// Third way to get BeanManager: name service
		BeanManager bm = InitialContext.doLookup("java:comp/BeanManager");

		test(bm);
	}

	private void test(BeanManager bm) throws Exception {
		Set<Bean<?>> beans = bm.getBeans(Greeting.class);
		assertTrue(beans.size() == 2);

		Set<String> beanClassNames = new HashSet<>();
		for (Bean bean : beans) {
			beanClassNames.add(bean.getBeanClass().getName());
		}

		assertThat(beanClassNames, containsInAnyOrder(SimpleGreeting.class.getName(), SmileyGreeting.class.getName()));
	}
}
