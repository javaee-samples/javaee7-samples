package org.javaee7.cdi.instance;

/**
 * @author Arun Gupta
 * @author Radim Hanus
 */
public class FancyGreeting implements Greeting {
	@Override
	public String greet(String name) {
		return "Nice to meet you, hello" + name;
	}
}
