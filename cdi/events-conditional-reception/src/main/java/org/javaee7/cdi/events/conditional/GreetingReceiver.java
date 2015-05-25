package org.javaee7.cdi.events.conditional;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import java.io.Serializable;

/**
 * @author Radim Hanus
 */
@SessionScoped
public class GreetingReceiver implements EventReceiver, Serializable {
	private String greet = "Willkommen";

	void receive(@Observes(notifyObserver = Reception.IF_EXISTS) String greet) {
		this.greet = greet;
	}

	@Override
	public String getGreet() {
		return greet;
	}
}