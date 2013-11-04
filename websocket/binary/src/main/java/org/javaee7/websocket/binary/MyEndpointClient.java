/**
 * 
 */
package org.javaee7.websocket.binary;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * @author Nikolaos Ballas
 *
 */
@ClientEndpoint
public class MyEndpointClient {
	@OnOpen
	public void onOpen(Session session){
		System.out.println("[Action]->Invokint method onOpen of the class:"+this.getClass().getCanonicalName());
		try{
			session.getBasicRemote().sendBinary(ByteBuffer.wrap("Hello World!".getBytes()));
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}
