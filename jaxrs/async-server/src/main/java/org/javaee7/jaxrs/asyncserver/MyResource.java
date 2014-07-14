package org.javaee7.jaxrs.asyncserver;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;

/**
 * @author Arun Gupta
 */
@Path("fruits")
public class MyResource {
    private final String[] response = { "apple", "banana", "mango" };
    
//    @Resource(name = "DefaultManagedThreadFactory")
//    ManagedThreadFactory threadFactory;
    
    @GET
    public void getList(@Suspended final AsyncResponse ar) throws NamingException {
        ar.setTimeoutHandler(new TimeoutHandler() {

            @Override
            public void handleTimeout(AsyncResponse ar) {
                ar.resume("Operation timed out");
            }
        });
        ar.setTimeout(4000, TimeUnit.MILLISECONDS);
        
        ar.register(new MyCompletionCallback());
        ar.register(new MyConnectionCallback());
        
        ManagedThreadFactory threadFactory = (ManagedThreadFactory) new InitialContext()
                .lookup("java:comp/DefaultManagedThreadFactory");
        
        Executors.newSingleThreadExecutor(threadFactory).submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    ar.resume(response[0]);
                } catch (InterruptedException ex) {
                    
                }
            }
            
        });
    }
    
    class MyCompletionCallback implements CompletionCallback {

        @Override
        public void onComplete(Throwable t) {
            System.out.println("onComplete");
        }
        
    }
    
    class MyConnectionCallback implements ConnectionCallback {

        @Override
        public void onDisconnect(AsyncResponse ar) {
            System.out.println("onDisconnect");
        }
        
    }

}
