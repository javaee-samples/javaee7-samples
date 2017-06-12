package org.javaee7.jms.temp.destination;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

/**
 * Client receiving response to a message via temporary queue. The client has to be non-trasactional, as we need to send
 * message in the middle of the method.
 * 
 * @author Patrik Dudits
 */
@Stateless
public class JmsClient {

	@Resource(lookup = Resources.REQUEST_QUEUE)
	private Queue requestQueue;

	@Inject
	private JMSContext jms;

	// <1> we need to send message in the middle of the method, therefore we cannot be transactional
	@TransactionAttribute(NOT_SUPPORTED)
	public String process(String request) {

		TextMessage requestMessage = jms.createTextMessage(request);
		TemporaryQueue responseQueue = jms.createTemporaryQueue();
		
		jms.createProducer()
		   .setJMSReplyTo(responseQueue) // <2> set the temporary queue as replyToDestination
		   .send(requestQueue, requestMessage); // <3> immediately send the request message

		try (JMSConsumer consumer = jms.createConsumer(responseQueue)) { // <4> listen on the temporary queue

			String response = consumer.receiveBody(String.class, 20000); // <5> wait for a +TextMessage+ to arrive

			if (response == null) { // <6> +receiveBody+ returns +null+ in case of timeout
				throw new IllegalStateException("Message processing timed out");
			} 
			
			return response;
			
		}
	}
}
