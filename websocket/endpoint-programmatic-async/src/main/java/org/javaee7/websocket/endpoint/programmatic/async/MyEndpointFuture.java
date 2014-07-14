package org.javaee7.websocket.endpoint.programmatic.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author Arun Gupta
 */
public class MyEndpointFuture extends Endpoint {

    @Override
    public void onOpen(final Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String data) {
                System.out.println("Received (MyEndpointFuture) : " + data);
//                try {
//                    session.getBasicRemote().sendText(data);
//                } catch (IOException ex) {
//                    Logger.getLogger(MyEndpointFuture.class.getName()).log(Level.SEVERE, null, ex);
//                }
                
                Future f = session.getAsyncRemote().sendText(data);
                try {
                    Thread.sleep(3000);
                    if (f.isDone()) {
                        System.out.println("Message written to the socket (future)");
                    } else {
                        System.out.println("Message NOT written to the socket (future)");
                    }
                    Object o = f.get();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(MyEndpointFuture.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
