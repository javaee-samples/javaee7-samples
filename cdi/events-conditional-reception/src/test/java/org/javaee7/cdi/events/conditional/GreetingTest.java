package org.javaee7.cdi.events.conditional;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
	@Deployment
	public static Archive<?> deploy() {
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(EventReceiver.class, EventSender.class, GreetingReceiver.class, GreetingSender.class)
				.addAsManifestResource("beans.xml");
	}

	@Inject
	private EventSender sender;

	@Inject
	private EventReceiver receiver;

	@Test
	public void test() throws Exception {
		assertThat(sender, is(notNullValue()));
		assertThat(sender, instanceOf(GreetingSender.class));

		assertThat(receiver, is(notNullValue()));
		assertThat(receiver, instanceOf(GreetingReceiver.class));

		// send a new greet but the receiver is not instantiated yet
		sender.send("Welcome");
		// default greet should be available (note that receiver has just been instantiated)
		assertEquals("Willkommen", receiver.getGreet());
		// send a new greet again
		sender.send("Welcome");
		// observer method was called so that new greet should be available
		assertEquals("Welcome", receiver.getGreet());
	}
}
