package org.javaee7.jaxrpc.endpoint;

public class HelloServiceImpl implements HelloService {

    public String sayHello(String input) {
        return "Hello " + input;
    }
}