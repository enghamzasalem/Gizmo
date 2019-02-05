/* 
 * Capability.java 1.0 2012-06-18
 * 
 * [Jul-01] Jeff: added support for capability complete message
 */

package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import edu.cmu.gizmo.management.taskbus.GizmoTaskBus;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityCompleteMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityInputMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityOutputMessage;
import edu.cmu.gizmo.management.taskbus.messages.HeloClientMessage;
import edu.cmu.gizmo.management.taskbus.messages.StartCapabilityMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessageHandler;

/**
 * This is the superclass for all capabilities. This class provides services for
 * sending/receiving messages to/from the GizmoTaskBus, identifiers, and common
 * services.
 * <p>
 * The capability superclass serves as the capabilities interface to the rest of
 * the system.
 * <p>
 * System extenders are expected to use this class to install a new capability.
 * See the abstract methods below for specifics concerning extension.
 * 
 * @version 1.0 18 Jun 2012
 * @author Jeff Gennari
 */
public abstract class Capability implements Runnable, MessageListener,
		TaskMessageHandler {

	public static String UI_CLASS = "ui.class";

	public static String UI_DISPLAY = "ui.display";

	/**
	 * Values for capability state according to the capability life cycle.
	 * 
	 * <ul>
	 * <li>INIT: The capability is instantiated
	 * <li>LOADED: The capability has been loaded meaning all necessary
	 * resources are present and correct
	 * <li>RUNNING: The capability is executing
	 * <li>COMPLETE: The capability completed successfully
	 * <li>CANCELED: The capability has been canceled
	 * <li>ERROR: The capability has experienced an error
	 * </ul>
	 */
	public static enum CapabilityStatus {

		/** The initial state of a capability. */
		INIT,

		/**
		 * The loaded state of a capability. this state indicates that the
		 * capability has been properly loaded and configured
		 */
		LOADED,

		/**
		 * The running state of this capability. This state represents a running
		 * capability.
		 */
		RUNNING,

		/** The paused state. This state represents a suspended capability. */

		PAUSED,

		/** The complete state represents a successfully completed capability. */
		COMPLETE,
		/**
		 * The canceled state this is used to indicate that the capability has
		 * been forcefully terminated.
		 */

		CANCELED,

		/**
		 * The error state that is used to indicate the capability has
		 * experienced an error from which it cannot recover.
		 */
		ERROR
	};

	/** the task input receiver. */
	private MessageConsumer input;

	/** the task output sender. */
	private MessageProducer output;

	/** the unique ID for the task running this capability. */
	private Integer taskId;

	/** the unique ID for this capability. */
	private Integer capabilityId;

	/** the connection to the Gizmo Task Bus used for I/O. */
	private GizmoTaskBus bus;

	/** the current status of the capability - default to INIT. */
	private CapabilityStatus status;

	/** the status message associated with the status. */
	private String statusMessage;

	/** the capability has been executed. */
	private Boolean launched;

	/** the capability manifest file */
	private ConcurrentHashMap<Object, Object> configuration;

	/**
	 * Set initial conditions for all capabilities.
	 */
	public Capability() {
		status = CapabilityStatus.INIT;
		launched = false;
	}

	/**
	 * Load a capability - set the parameters needed by all capabilities.
	 * 
	 * @param tid
	 *            a unique identifier for this task.
	 * @param cid
	 *            a unique identifier for this capability.
	 * @return true if loaded OK, false otherwise
	 */
	public Boolean load(final Integer tid, final Integer cid,
			final ConcurrentHashMap<Object, Object> config) {

		// already loaded
		if (status == CapabilityStatus.LOADED) {
			return true;
		}

		// only accept valid IDs
		if ((tid == null) || (cid == null)) {
			return false;
		}

		input = null;
		output = null;
		launched = false;

		// the capability should have access to its manifest file
		configuration = config;

		// establish connection to the GizmoTaskBus on the Task topic
		bus = GizmoTaskBus.connect();

		output = bus.getTaskProducer();

		/*
		 * Tell the system what messages this capability will receive.
		 */
		final String[] subscriptions = { TaskMessage.CAPABILITY_INPUT,
				TaskMessage.START_CAPABILITY, };

		try {

			input = bus.getTaskConsumer(subscriptions);
			input.setMessageListener(this);

		} catch (final JMSException e) {

			e.printStackTrace();
			setStatus(CapabilityStatus.ERROR, "[" + getCapabilityName()
					+ "]: Could not get connection to GizmoTaskBus");
			return false;
		}

		taskId = tid;
		capabilityId = cid;

		System.out.println("[Capability] " + getCapabilityName() + " loaded  ("
				+ taskId + ":" + capabilityId + ")");

		// the connection to the task bus is made and all is well -
		// set the status of the capability to LOADED
		setStatus(CapabilityStatus.LOADED, "Capability loaded");

		return true;
	}

	/**
	 * Release the message producer and consumer. This method must be called
	 * explicitly when the capability is unloaded.
	 */
	public void unload() {

		// terminate the capability (by force)
		terminate();

		if (input != null) {
			bus.releaseConsumer(input);
			input = null;
		}
		if (output != null) {
			bus.releaseProducer(output);
			output = null;
		}

		launched = false;
		if (bus != null) {
			bus.disconnect();
		}
	}

	/**
	 * This method fetches events from the GizmoTaskBus.
	 * 
	 * @param busMessage
	 *            the bus message
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(final Message busMessage) {
		try {

			final ObjectMessage om = (ObjectMessage) busMessage;
			final TaskMessage message = (TaskMessage) om.getObject();

			handleMessage(message);

		} catch (final JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * handle input to this capability. The only message that must be handled by
	 * a Capability is a <code>CapabilityInputMessage</code>.
	 * 
	 * @param message
	 *            the input to this capability
	 * 
	 * @see CapabilityInputMessage
	 * @see CapabilityReadyMessage
	 * @see StartCapabilityMessage
	 */
	@Override
	public final void handleMessage(final TaskMessage message) {

		if (message.getMessageType() == TaskMessage.CAPABILITY_INPUT) {

			// capability input send it to the instantiated
			// capability
			final CapabilityInputMessage capInputMessage = (CapabilityInputMessage) message;

			final Integer tid = capInputMessage.getTaskId();
			final Integer cid = capInputMessage.getCapabilityId();

			// only process messages for this task/capability
			if ((tid.compareTo(taskId) == 0)
					&& (cid.compareTo(capabilityId) == 0)) {

				setInput(capInputMessage.getInput());
			}
		} else if (message.getMessageType() == TaskMessage.START_CAPABILITY) {

			final StartCapabilityMessage startMessage = (StartCapabilityMessage) message;

			final Integer tid = startMessage.getTaskId();
			final Integer cid = startMessage.getCapabilityId();

			// only process messages for this task/capability
			if ((tid.compareTo(taskId) == 0)
					&& (cid.compareTo(capabilityId) == 0)) {

				System.out.println("[Capability] " + getCapabilityName()
						+ " starting as task " + tid + ":" + cid);

				// the client has started the capability
				setStatus(CapabilityStatus.RUNNING, "Capability "
						+ getCapabilityName() + " running");
			}
		} else if (message.getMessageType() == TaskMessage.TERMINATE_CAPABILITY) {
			if ((getStatus() == CapabilityStatus.LOADED)
					|| (getStatus() == CapabilityStatus.RUNNING)) {
				terminate();
			}
		}
	}

	/**
	 * Send capability output to the task bus.
	 * 
	 * @param outputName
	 *            the output name
	 * @param outputVal
	 *            the output to send
	 * @see CapabilityOutputMessage the message to send
	 */
	public synchronized void sendOutput(final Object outputName,
			final Object outputVal) {

		final ConcurrentHashMap<Object, Object> payload = new ConcurrentHashMap<Object, Object>();

		payload.put(outputName, outputVal);

		try {
			final CapabilityOutputMessage outputMessage = new CapabilityOutputMessage(
					taskId, capabilityId, payload);

			final ObjectMessage m = bus.generateMessage(outputMessage,
					TaskMessage.CAPABILITY_OUTPUT);

			if (output != null) {
				output.send(m);
			}
		}

		catch (final Exception e) {
			/*
			 * Output is transient so we may wish to forgive failures
			 */
			setStatus(CapabilityStatus.ERROR, "[" + getCapabilityName() + "]: "
					+ "Error sending output ");
			System.out.println("[Capability] " + getCapabilityName());
			e.printStackTrace();
		}
	}

	/**
	 * Get a configuration setting capability manifest file.
	 * 
	 * @param parameter
	 *            the configuration parameter to get
	 * @return the value of the parameter or null if it is not found
	 */
	protected Object getConfigurationValue(final String parameter) {

		if (configuration.containsKey(parameter)) {
			return configuration.get(parameter);
		}
		return null;
	}

	/**
	 * Gets the status message.
	 * 
	 * @return the status message
	 */
	public final synchronized String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Set the status for this capability. All capabilities must set status to
	 * reflect operating state. This method will send a message to the bus
	 * reflecting the new status
	 * 
	 * @param newStatus
	 *            the status of the capability
	 * @param newStatusMessage
	 *            the message associated with the status
	 * 
	 *            Example:
	 *            <p>
	 *            setStatus(CapabilityStatus.RUNNING,"Running capability");
	 * 
	 */
	protected final synchronized void setStatus(
			final CapabilityStatus newStatus, final String newStatusMessage) {

		if (newStatus == CapabilityStatus.COMPLETE) {
			// the capability is completed
			final CapabilityCompleteMessage completeMsg = new CapabilityCompleteMessage(
					taskId, capabilityId);

			try {
				output.send(bus.generateMessage(completeMsg,
						TaskMessage.CAPABILITY_COMPLETE));
			} catch (final JMSException e) {
				e.printStackTrace();
			}
		} else if (newStatus == CapabilityStatus.LOADED) {
			// inform the client the capability is ready
			try {

				final ConcurrentHashMap<Object, Object> settings = new ConcurrentHashMap<Object, Object>();

				// send the settings (from the manifest) to the client. These
				// are required.
				try {
					settings.put(Capability.UI_CLASS,
							getConfigurationValue(Capability.UI_CLASS));
					settings.put(Capability.UI_DISPLAY,
							getConfigurationValue(Capability.UI_DISPLAY));
				} catch (final NullPointerException n) {
					/*
					 * The UI parts of the capability are not present
					 */
				}
				final HeloClientMessage heloClient = new HeloClientMessage(
						taskId, capabilityId, getCapabilityName(), settings);

				output.send(bus.generateMessage(heloClient,
						TaskMessage.HELO_CLIENT));

				System.out.println("[Capability] " + getCapabilityName()
						+ " says helo (" + taskId + ":" + capabilityId + ")");

			} catch (final JMSException e) {
				e.printStackTrace();
			}
		}

		// install the new status
		status = newStatus;
		statusMessage = newStatusMessage;
	}

	/**
	 * Get the status of this capability.
	 * 
	 * @return The status of this capability
	 */
	public final synchronized CapabilityStatus getStatus() {
		return status;
	}

	/**
	 * start the capability thread.
	 */
	@Override
	public final void run() {
		execute();
		launched = true;
	}

	/**
	 * Start the capability. This method will run the capability in a new
	 * thread, which will cause the execute method to run
	 */
	public synchronized void launch() {
		if (launched == false) {
			new Thread(this).start();
		}
	}

	/*
	 * The following methods must be overridden by each capability upon
	 * implementation.
	 */

	/**
	 * This is the body of the capability. Implementors should define this
	 * method to have the capability actually do something.
	 */
	public abstract void execute();

	/**
	 * Terminate the capability. Implementors should define this method to have
	 * the capability stop executing.
	 */
	public abstract void terminate();

	/**
	 * Get the name of this capability.
	 * 
	 * @return the capability name
	 */
	public abstract String getCapabilityName();

	/**
	 * Return the capability name. This must be overridden by all capabilities.
	 * 
	 * @return the capability name
	 */
	public abstract String getCapabilityDescription();

	/**
	 * Provide this capability with input.
	 * 
	 * @param param
	 *            the name of the input parameter
	 * @param value
	 *            the value of the input parameter
	 */
	public abstract void setInput(ConcurrentHashMap<Object, Object> input);

	/**
	 * Get the value of an input parameter.
	 * 
	 * @param param
	 *            the name of the input parameter to retrieve
	 * 
	 * @return the value of the input parameter
	 */
	public abstract Object getInputParameterValue(Object param);

	/**
	 * Get the input requirements for this capability.
	 * 
	 * @return an <code>ConcurrentHashMap</code> of input requirements as a
	 *         ConcurrentHashMap with name and type, or null if there is no
	 *         input
	 */
	public abstract ConcurrentHashMap<String, Class> getInputRequirements();

	/**
	 * Get the output requirements for this capability.
	 * 
	 * @return a <code>ConcurrentHashMap</code> of output requirements as a
	 *         ConcurrentHashMap with name and type, or null if there is no
	 *         output
	 */
	public abstract ConcurrentHashMap<String, Class> getOutputRequirements();

};