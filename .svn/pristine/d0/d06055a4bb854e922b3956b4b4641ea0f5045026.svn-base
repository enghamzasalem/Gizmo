/*
 * GizmoTaskBus.java 1.1 2012-06-18
 */

package edu.cmu.gizmo.management.taskbus;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import edu.cmu.gizmo.management.taskbus.messages.TaskMessage;


/**
 * This class is the primary API used for communication between the Task
 * Client and Task Manager. It is essentially an interface to a message bus.
 * <p>
 * The GizmoTaskBus serves as a uniform way to access a JMS event queue. If
 * performance becomes a problem, this interface can be tuned to filter messages
 * at the JMS level.
 * <p>
 * An asynchronous messaging system allows the Task Client and Task Manager to
 * both issue task control commands, which is critical for QAS11 (power
 * robustness) when the robot must end a task due to a low battery and QAS13
 * (Task interruption) when a user wishes to pause a task. This type of
 * application also greatly simplifies the logic with the Task Manager and
 * TaskClient.
 * <p>
 * Much of the communication in the system was deemed to be asynchronous; thus
 * we thought an event-style messaging system was more appropriate than
 * synchronous commands. Given that there is only one Task Client per robot,
 * message sequencing should not be a problem. However, it is up to the users of
 * this API to maintain message ordering and sequencing.
 * <p>
 * JMS imposes restrictions on threading and requires messy setup; therefore,
 * this class will serve as a wrapper around JMS setup. The JMS classes will be
 * used to publish/subscribe topics. Each publisher and subscriber will have
 * their own session to eliminate threading concerns and improve performance.
 * That said, connections are shared among threads as they are thread safe.
 * Finally, this interface is designed to allow it to work with both
 * <code>Topic</code> and <code>Queue</code> messaging.
 * 
 * @version 1.1 18 Jun 2012
 * @author Jeff Gennari
 * 
 */
public class GizmoTaskBus {

	/** This topic is used for input/output messages. */
	private final String GIZMO_TOPIC = "GizmoTopic";

	/*
	 * Two sessions are used because of JMS's threading restrictions. That is
	 * sessions cannot be shared between threads. So, to enable using the bus
	 * between threads, different sessions are used for reading/writing
	 */

	/** This is the publish session. */
	private Session pubSession;

	/** This is the subscription session. */
	private Session subSession;
	
	/** The open sessions for consumers. */ 
	private Hashtable<Integer,Session> subscriptions;
	
	/** The open sessions for producers. */
	private Hashtable<Integer,Session> publications;

	/**
	 * The connection to the bus. Unlike sessions, connections can be shared
	 * between threads
	 */
	private Connection conn;

	private Boolean connected;
	
	/**
	 * This is a private constructor for the GizmoTaskBus. It will
	 * create a new connection to the bus.
	 *
	 * @throws JMSException the jMS exception
	 */
	private GizmoTaskBus() throws JMSException {

		// create the connection to the task bus
		final ActiveMQConnectionFactory connectionFactory = 
			new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);

		conn = connectionFactory.createConnection();
		
		subscriptions = new Hashtable<Integer,Session>();
		publications = new Hashtable<Integer,Session>();

		// this will block until activeMQ is up and running
		conn.start();

		pubSession = null;
		subSession = null;
		connected = true;
	}
	
	/**
	 * Return a new reference to the GizmoTaskBus. 
	 *
	 * @return a reference to a connected GizmoTaskBus
	 */
	public synchronized static GizmoTaskBus connect() {

		try {
			return new GizmoTaskBus();
		} catch (final JMSException e) {
			return null;
		}
	}
	
	/**
	 * Explicitly disconnect from the bus. Failing to close the connection 
	 * results in strange behavior in the broker and possibly gingivitis
	 */
	public synchronized void disconnect() {
		connected = false;
		try {
			if (conn != null) { 
				conn.close();
				conn = null;
				
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close a consumer.
	 *  
	 * @param consumer the consumer to close
	 */
	public void releaseConsumer(MessageConsumer consumer) {

		// close the publisher and subscriber
		try {
			// close the consumer
			if (consumer != null) {				
				Session session = subscriptions.get(consumer.hashCode());

				consumer.close();
				if (session != null) {
					session.close();
				}

				subscriptions.remove(consumer.hashCode());
				consumer = null;
			}
		} catch (final JMSException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Close a producer. 
	 *  
	 * @param producer the producer to close
	 */
	public void releaseProducer(MessageProducer producer) {
		// close the publisher
		try {
			if (producer != null) {

				Session session = publications.get(producer.hashCode());

				synchronized(producer) {
					producer.close();
					if (session != null) {
						session.close();
					}
				}
				publications.remove(producer.hashCode());
				producer = null;
			}
		} catch (final JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new MessageProducer to send messages to the task channel. The
	 * new producer will have its own session and send messages to the
	 * <code>TASK_TOPIC</code>.
	 *
	 * @return a message producer for the task channel
	 */
	public synchronized MessageProducer getTaskProducer() {
		try {
			return getProducer(GIZMO_TOPIC);
		} catch (final JMSException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create the selector that will determine what types of messages to
	 * receive.
	 *
	 * @param messageSelectors the list of message selectors
	 * @return the string
	 */
	private String generateSelector(String [] messageSelectors) {
		
		if (messageSelectors == null)
			return null;
		
		/*
		 * Create a selector of the form: 
		 * 
		 * gizmo = 'SELECTOR 1' OR gizmo = 'SELECTOR 2' OR ...
		 *
		 * to determine which message this consumer will accept
		 */
		
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < messageSelectors.length; i++) {
			buf.append("gizmo = '");
			buf.append(messageSelectors[i]);
			buf.append("'");
			if ((i+1) < messageSelectors.length) {
				buf.append(" OR ");
			}	
		}
		return buf.toString();
	}
	
	/**
	 * Create a message producer in a new session.
	 *
	 * @param topicName the topic to send messages to
	 * @return the connected message producer capable of sending messages
	 * to the task bus
	 * @throws JMSException the jMS exception
	 */
	private MessageProducer getProducer(final String topicName)
			throws JMSException {

		if (conn != null) {
			synchronized(conn) {
				pubSession =
					conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			}
		}
		final Topic topic = pubSession.createTopic(topicName);

		// Create a MessageProducer from the Session to the Topic or Queue
		final MessageProducer producer = pubSession.createProducer(topic);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		// remember the session to tear it down later
		publications.put(producer.hashCode(),pubSession);
		
		return producer;
	}

	/**
	 * Create a message consumer in a new session.
	 *
	 * @param topicName the name of the topic to subscribe to
	 * @param selector the selector for this consumer.
	 * @return the newly created MessageConsumer
	 * @throws JMSException the jMS exception
	 */
	private MessageConsumer getConsumer(final String topicName, 
			final String selector) throws JMSException {


		subSession = 
			conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

		final Topic topic = subSession.createTopic(topicName);
		final MessageConsumer consumer = 
			subSession.createConsumer(topic, selector);

		// remember the session to tear it down later
		subscriptions.put(consumer.hashCode(),subSession);
		
		return consumer;
	}

	/**
	 * return a message consumer for the task channel.
	 *
	 * @param messageSelectors the list of messages for the consumer to accept
	 * @return the newly created message consumer, or null if there is
	 * an error
	 */
	public synchronized MessageConsumer getTaskConsumer(
			String[] messageSelectors) {
		try {
			return getConsumer(
					GIZMO_TOPIC, generateSelector(
							messageSelectors
					)
			);
		} catch (final JMSException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate a new ObjectMessage that is ready to send.
	 *
	 * @param messageBody the object to send
	 * @param messageType the type of the message to send as take from
	 * <code>TaskMessage</code>
	 * @return the new <code>ObjectMessage</code> or null if the message could
	 * not be created
	 * @see TaskMessageType
	 */
	public synchronized ObjectMessage generateMessage(
			Serializable messageBody, 
			String messageType) {

		if (messageBody instanceof TaskMessage) {
			try {

				ObjectMessage message = 
					pubSession.createObjectMessage();

				if (messageType!= null)
					message.setStringProperty("gizmo", messageType);

				message.setObject(messageBody);
				return message;

			} catch (final JMSException e) { }
		}
		// On failure simply return null
		return null;
	}

	/**
	 * returns connected state of the task bus.
	 * 
	 * @return true of connected, false otherwise.
	 */
	public synchronized Boolean isConnected() {
		return connected;
	}
}