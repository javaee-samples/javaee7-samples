package org.javaee7.cdi.instance;

import javax.enterprise.context.RequestScoped;

/**
 * @author Arun Gupta
 * @author Radim Hanus
 */
@RequestScoped
public class FancyGreeting implements Greeting {
	@Override
	public String greet(String name) {
		return "Nice to meet you, hello" + name;
	}
}
