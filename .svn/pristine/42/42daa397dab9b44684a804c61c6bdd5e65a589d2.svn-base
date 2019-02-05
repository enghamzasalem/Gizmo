/*
 * TaskClient.java 1.0 2012-06-27 
 */

package edu.cmu.gizmo.management.taskclient;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import edu.cmu.gizmo.management.taskbus.GizmoTaskBus;
import edu.cmu.gizmo.management.taskbus.messages.CancelTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityCompleteMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityInputMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityOutputMessage;
import edu.cmu.gizmo.management.taskbus.messages.HeloClientMessage;
import edu.cmu.gizmo.management.taskbus.messages.LoadTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.RejectTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.StartCapabilityMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskCompleteMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessageHandler;
import edu.cmu.gizmo.management.taskbus.messages.TaskReadyMessage;
import edu.cmu.gizmo.management.taskclient.CapabilityHandler.ClientCapabilityStatus;
import edu.cmu.gizmo.management.taskclient.TaskHandler.TaskHandlerStatus;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskType;
import edu.cmu.gizmo.management.taskmanager.TaskInputMap;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;


// TODO: Auto-generated Javadoc
/**
 * The main task client. The task client is the interface between the UI 
 * elements of the system and the 
 * 
 * @version 1.0 2012-06-27
 * @author Jeff Gennari
 * 
 */
public class TaskClient extends Observable 
implements MessageListener, GizmoTaskClient {

	/**
	 * The Enum TaskClientCommands.
	 */
	public static enum TaskClientCommands {

		/** The load UI message that tells interface components to load 
		 *  needed UI elements. */
		LOAD_UI, 

		/** The unload UI message that tells interface components to unload  
		 * UI elements. */
		UNLOAD_UI, 

		/** The output message for task output */
		OUTPUT,		
		
		/** Task was rejected */
		REJECT_TASK,
		
		/** command to cancel a running task */
		CANCEL_TASK,
		
		/** command to indicate that a task is completed */
		COMPLETE_TASK
	}
	
	/**
	 * The elements of the protocol between the UI and the client protocol 
	 */
	public static final String UI_PROTOCOL_CMD = "cmd";
	public static final String UI_PROTOCOL_TASK_ID = "taskId";
	public static final String UI_PROTOCOL_CAPABILITY_ID = "capabilityId";
	public static final String UI_PROTOCOL_REASON = "reason";
	public static final String UI_PROTOCOL_OUTPUT = "output";
	public static final String UI_PROTOCOL_NAME = "name";
	public static final String UI_PROTOCOL_DEFAULT_INPUT = "defaultInput";
	public static final String UI_PROTOCOL_TASK_HISTORY = "taskHistory";
	
	private static final String ROBOT_DASHBOARD = "dashboard";
	
	/** a reference to the Gizmo Task Bus. */
	private final GizmoTaskBus bus;

	/** The list of active tasks. */
	private final ConcurrentHashMap<Integer, TaskHandler> tasks;
	
	/** The list of active tasks. */
	private final Vector<TaskReservation> pendingRequests;

	/** The output connection to the task bus. */
	private MessageProducer taskOutputChannel;

	/** The input connection to the task bus. */
	private MessageConsumer taskInputChannel;

	/** handler for task messages. */
	private ClientTaskMessageHandler messageHandler;

	/** The requester id. */
	private final TaskRequester REQUESTER_ID = TaskRequester.TASK_CLIENT;

	/** Boolean to determine if the dashboard is loaded. */
	private Boolean dashboardLoaded;

	/** default input for a task. */
	private ConcurrentHashMap<Object, Object> defaultInput;
	
	/** the history of tasks (by name) */
	private Queue<String>taskHistory;

	/**
	 * Create a new TaskClient and connect it to the TaskBus.
	 */
	public TaskClient() {

		// connect to the bus
		bus = GizmoTaskBus.connect();
		
		tasks = new ConcurrentHashMap<Integer, TaskHandler>();
		taskOutputChannel = bus.getTaskProducer();
		new ConcurrentHashMap<Integer,TaskInputMap>();
		dashboardLoaded = false;
		pendingRequests = new Vector<TaskReservation>();
		taskHistory = new LinkedList<String>();
		defaultInput = new ConcurrentHashMap<Object, Object>();

		/*
		 * These are the messages that the Task Client must handle
		 */
		final String[] taskControlProtocol = {
				TaskMessage.TASK_READY,
				TaskMessage.HELO_CLIENT, 
				TaskMessage.CAPABILITY_COMPLETE,
				TaskMessage.PAUSE_TASK_COMPLETE, 
				TaskMessage.PAUSE_LIST,
				TaskMessage.PAUSE_TASK,
				TaskMessage.RESUME_TASK_COMPLETE, 
				TaskMessage.TASK_COMPLETE,
				TaskMessage.CAPABILITY_OUTPUT, 
				TaskMessage.CANCEL_TASK,
				TaskMessage.SYSTEM_REPLAN,
				TaskMessage.REJECT_TASK
		};

		// install the message listener
		try {
			taskInputChannel = bus.getTaskConsumer(taskControlProtocol);
			taskInputChannel.setMessageListener(this);
			messageHandler = new ClientTaskMessageHandler();

		} catch (final JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive messages from the Task Bus.
	 *
	 * @param message the message
	 */
	@Override
	public void onMessage(final Message message) {
		// unwrap and handle the message and handle it
		try {

			final ObjectMessage objMessage = (ObjectMessage) message;
			messageHandler.handleMessage((TaskMessage) objMessage.getObject());

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Private utility class to handle incoming messages.
	 * 
	 * @version 1.0 2012-06-27
	 * @author Jeff Gennari
	 * 
	 */
	private class ClientTaskMessageHandler implements TaskMessageHandler {

		/**
		 * Handle Gizmo messages.
		 * 
		 * @param message
		 *            the message to handle
		 */
		@Override
		public synchronized void handleMessage(final TaskMessage message) {

			// invalid message
			if (message == null) {
				return;
			}

			/*
			 * handle the reject client message
			 */
			else if (message.getMessageType() == TaskMessage.REJECT_TASK) {

				handleRejectTaskMessage((RejectTaskMessage) message);
			}
			/*
			 * handle the helo client message
			 */
			else if (message.getMessageType() == TaskMessage.HELO_CLIENT) {

				handleHeloClientMessage((HeloClientMessage) message);
			}

			/*
			 * handle the helo client message
			 */
			else if (message.getMessageType() == TaskMessage.TASK_READY) {

				handleTaskReadyMessage(
						(TaskReadyMessage) message);
			}

			/*
			 * Handle the pause complete message
			 */
			else if (message.getMessageType() ==  TaskMessage.PAUSE_TASK_COMPLETE) {
				// TBD ... 
			}

			/*
			 * Handle the task complete message
			 */
			else if (message.getMessageType() == TaskMessage.TASK_COMPLETE) {

				handleTaskCompleteMessage((TaskCompleteMessage) message);
			}

			/*
			 * handle the capability complete message
			 */
			else if (message.getMessageType() == TaskMessage.CAPABILITY_COMPLETE) {

				handleCapabilityCompleteMessage(
						(CapabilityCompleteMessage) message);
			}

			/*
			 * handle capability output
			 */
			else if (message.getMessageType() == TaskMessage.CAPABILITY_OUTPUT) {

				handleCapabilityOutputMessage(
						(CapabilityOutputMessage) message);
			}

			/*
			 * handle the cancel task message 
			 */
			else if (message.getMessageType() ==  TaskMessage.CANCEL_TASK) {

				handleCancelTaskMessage(
						(CancelTaskMessage) message);
			}
		}

		/**
		 * The newly requested task was rejected.
		 * 
		 * @param message the reject message.
		 */
		private void handleRejectTaskMessage(RejectTaskMessage message) {
			
			
			/*
			 *  Check if this is the right task (i.e. it is not from the client)
			 */
			
			final TaskReservation rejectedRequest = message.getReservation();
			final String reason = message.getReason();
			
			if (rejectedRequest.getTaskRequester() == TaskRequester.TASK_CLIENT) {
				for (TaskReservation t : pendingRequests) {
					if (t.compareTo(rejectedRequest) == 0) {

						// handle the rejected request
						rejectTaskRequest(rejectedRequest, reason);	
						return;
					}
				}
			}			
		}
		
		/**
		 * Terminate the capabilities associated with the currently running 
		 * task.
		 * 
		 * @param message the cancel message
		 */
		private void handleCancelTaskMessage(CancelTaskMessage message) {

			// check if this is the right task (i.e. it is not from the client
			if (message.getRequester() != TaskRequester.TASK_CLIENT) { 

				// find the task to kill ... 
				if (tasks.containsKey(message.getTaskId())) {
					cancelTask(message.getTaskId(), message.getReason());	
				}					
			}
		}
		
		/**
		 * Handle the task complete message.
		 *
		 * @param message the task complete message
		 */
		private void handleTaskCompleteMessage(
				final TaskCompleteMessage message) {

			// check if this is the right task
			if (!tasks.containsKey(message.getTaskId())) {
				return;
			}

			final Integer tid = message.getTaskId();
			TaskHandler handler = tasks.get(tid);

			if (handler != null) {

				System.out.println(
						"[TaskClient] Task Complete (task:"
						+message.getTaskId()+")");

				// mark all the capabilities as complete
				final Vector<CapabilityHandler> capabilities = handler
				.getAllCapabilities();

				final ConcurrentHashMap<String, Object> packet =
					new ConcurrentHashMap<String, Object>();
				
				// unload all the running capabilities
				for (final CapabilityHandler c : capabilities) {
					if (c.getStatus() == ClientCapabilityStatus.RUNNING) {
						c.setStatus(ClientCapabilityStatus.COMPLETE);

						// unload the running capability
						
						packet.put(UI_PROTOCOL_CMD, TaskClientCommands.UNLOAD_UI);
						packet.put(UI_PROTOCOL_TASK_ID, tid);
						packet.put(UI_PROTOCOL_CAPABILITY_ID, c.getCapabilityId());

						setChanged();
						notifyObservers(packet);
					}
				}
				
				/*
				 * All the individual capabilities have been unloaded - now 
				 * signal the task as complete 
				 */
												
				packet.clear();

				packet.put(UI_PROTOCOL_CMD, TaskClientCommands.COMPLETE_TASK);
				packet.put(UI_PROTOCOL_TASK_ID, tid);
				
				setChanged();
				notifyObservers(packet);
				
				// remove the completed task - should we keep a history?
				tasks.remove(tid);
				handler = null;
			}
		}

		/**
		 * The capability is completed.
		 * 
		 * @param message
		 *            the capability complete message
		 */
		private void handleCapabilityCompleteMessage(
				final CapabilityCompleteMessage message) {

			final Integer tid = message.getTaskId();
			final Integer cid = message.getCapabilityId();
			final TaskHandler handler = tasks.get(tid);

			if (handler != null) {
				// mark the capability as complete
				handler.setCapabilityStatus(cid,
						ClientCapabilityStatus.COMPLETE);
				
				System.out.println(
						"[TaskClient] Received Capability Complete (task="
						+tid+":"+cid+")");

				
				// add the capability to the task history
				taskHistory.add(handler.getCapabilityName(cid));
				
				final ConcurrentHashMap<String, Object> packet = 
					new ConcurrentHashMap<String, Object>();

				packet.put(UI_PROTOCOL_CMD, TaskClientCommands.UNLOAD_UI);
				packet.put(UI_PROTOCOL_TASK_ID, tid);
				packet.put(UI_PROTOCOL_CAPABILITY_ID, cid);
				packet.put(UI_PROTOCOL_TASK_HISTORY, taskHistory);
				packet.put(UI_PROTOCOL_REASON, 
						"capability completed successfully");
				 
				setChanged();
				notifyObservers(packet);
			}
		}

		/**
		 * Handle capability output messages.
		 * 
		 * @param message
		 *            the CapabilityOutputMessage
		 */
		private void handleCapabilityOutputMessage(
				final CapabilityOutputMessage message) {

			if (!tasks.containsKey(message.getTaskId())) {				
				return;
			}

			final Integer tid = message.getTaskId();
			final Integer cid = message.getCapabilityId();
			final TaskHandler handler = tasks.get(tid);

			if (handler != null) {

				final ConcurrentHashMap<Object, Object> out =
					message.getOutput();

				/*
				 * Send the output to those who need it.
				 */

				final ConcurrentHashMap<Object,Object> packet = 
					new ConcurrentHashMap<Object,Object>();

				packet.put(UI_PROTOCOL_CMD, TaskClientCommands.OUTPUT);
				packet.put(UI_PROTOCOL_TASK_ID, tid);
				packet.put(UI_PROTOCOL_CAPABILITY_ID, cid);
				packet.put(UI_PROTOCOL_OUTPUT,out);

				/* 
				 * check to see if there is any output that should be
				 * passed on to subsequent capabilities
				 */

				if (handler.getOutputFromCapability(cid) != null) { 
					
					ConcurrentHashMap<String,String> defIn = 
						new ConcurrentHashMap<String,String>();
					
					TaskInputMap map = handler.getOutputFromCapability(cid);
					if (map != null) {
						ConcurrentHashMap<String,String> route = map.getRoute();
						for (String r : route.keySet()) {
							if (out.containsKey(r)) {
								
								/* 
								 * the destination of the route is the input 
								 * to the next capability. That is: 
								 * so route.key[r] = output.value[r]
								 * 
								 */ 
								defIn.put(route.get(r), (String) out.get(r));
							}
						}
						
						if (!defIn.isEmpty()) {
							
							// found output to pass on
							defaultInput.put(cid, defIn);
						}
					}
				}

				setChanged();
				notifyObservers(packet);
			}
		}

		/**
		 * Handle the task ready message.
		 *
		 * @param message the message
		 */
		private void handleTaskReadyMessage(TaskReadyMessage message) {

			// make sure this is for us
			if (message.getReservation().getTaskRequester() == REQUESTER_ID) {	

				System.out.println("[TaskClient] Received Task Ready (Task:" 
						+message.getTaskId() + ")");

				/*
				 * create a new task handler to manage task state 
				 * from the client's perspective				
				 */
				
				final Integer tid = message.getTaskId();
				
				TaskHandler handler =  
					new TaskHandler(tid,message.getReservation());
				// record the I/O routes for this task
				if (message.getTaskRoutes() != null) {
					handler.addIoRoute(message.getTaskRoutes());
				}
				
				handler.setStatus(TaskHandlerStatus.READY);
				tasks.put(tid, handler);		
				
				// Once the task is ready, it is no longer pending
				pendingRequests.remove(message.getReservation());
			}
		}

		/**
		 * Handle the Helo Client Message. This message should force the client
		 * to load the UI for the capability, gather any needed input, and send
		 * that input to the capability
		 * 
		 * @param message
		 *            the HELO message
		 */
		private void handleHeloClientMessage(
				final HeloClientMessage message) {

			// only process things destined for this task
			if (!tasks.containsKey(message.getTaskId())) {
				return;
			}

			// on Helo, present the UI to the
			final Integer tid = message.getTaskId();
			final TaskHandler handler = tasks.get(tid);

			if (handler != null) {

				System.out.println(
						"[TaskClient] Received Helo Client (task="
						+ message.getTaskId()+":" 
						+ message.getCapabilityId() + ")");

				// Load the capability's UI
				final String cName = message.getCapabilityName();
				final Integer cid = message.getCapabilityId();

				final ConcurrentHashMap<Object, Object> settings = 
					message.getCapabilitySettings();


				// record this capability as part of the ongoing task
				handler.addCapabilityToTask(cid, cName, settings);

				// mark the capability as running
				handler.setCapabilityStatus(cid,
						ClientCapabilityStatus.READY);

				/* start the tasks that are marked as ready (i.e. 
				 * are not yet running). This allows us to start 
				 * initial tasks
				 */ 
				for (TaskHandler t : tasks.values()) {
					if (t.getStatus() == TaskHandlerStatus.READY) {
						handler.setStatus(TaskHandlerStatus.RUNNING);
						continueTask();
					}
				}			
			}			
		}			
	};

	/**
	 * This method will start a new task.
	 *
	 * @param rsvp the <code>TaskReservation</code> to execute
	 */
	public void loadNewTask(TaskReservation rsvp) {

		System.out.println("[TaskClient] Starting new task");

		pendingRequests.add(rsvp);
		// send the load task message for this reservation
		try {

			final LoadTaskMessage loadMessage =
				new LoadTaskMessage(rsvp);

			taskOutputChannel.send(bus.generateMessage(loadMessage,
					TaskMessage.LOAD_TASK));

		} 
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Proceed with a task. This method is called to begin the next step in a 
	 * task. 
	 */
	public void continueTask() { 

		System.out.println("[TaskClient] Continuing task");

		/*
		 *  Find all the capabilities for running tasks that are marked as 
		 *  ready. Once they are found, start them and set them as running. 
		 */
		for (TaskHandler handler : tasks.values()) {

			if (handler.getStatus() == TaskHandlerStatus.RUNNING) { 

				for (CapabilityHandler cap : handler.getAllCapabilities()) { 

					if (cap.getStatus() == ClientCapabilityStatus.READY) {

						// start the capability running
						final Integer tid = handler.getTaskId();
						final Integer cid = cap.getCapabilityId();

						try {

							final StartCapabilityMessage startMsg = 
								new StartCapabilityMessage(tid, cid);

							System.out.println("[TaskClient] Starting task (" 
									+tid + ":" + cid +")" );

							taskOutputChannel.send(bus.generateMessage(startMsg,
									TaskMessage.START_CAPABILITY));

						} 
						catch (final JMSException e) {
							e.printStackTrace();
						}				

						final ConcurrentHashMap<String, Object> packet = 
							new ConcurrentHashMap<String, Object>();

						// Load the UI for this capability 

						// send any default output from the previous capability 
						// to the new capability
						TaskInputMap ioRoute = 
							handler.getInputToThisCapability(cid);
						
						// send any default output from the previous capability 
						// to the new capability
						if (ioRoute != null) { 
							
							if (defaultInput.size() > 0) {
						
								Integer fromId = ioRoute.getFromCapabilityId(); 
								if (defaultInput.containsKey(fromId)) {
									packet.put(UI_PROTOCOL_DEFAULT_INPUT,
											defaultInput.get(fromId));
									
									final Iterator<Map.Entry<Object, Object>> entries = 
										defaultInput.entrySet().iterator();

									String route = "";
									// send each input parameter to the extending capability
									while (entries.hasNext()) {
										final Map.Entry<Object, Object> entry = 
											entries.next();

										route += entry.getKey() 
										+"->"+ entry.getValue();
										defaultInput.remove(fromId);
									}

									System.out.println(
											"[TaskClient] Default I/O map: " 
											+ route);
								}
							}
						}	
						


						ConcurrentHashMap<Object, Object> settings = 
							handler.getCapabilitySettings(cid);

						final Iterator<Map.Entry<Object, Object>> entries = 
							settings.entrySet().iterator();

						// send each input parameter to the extending capability
						while (entries.hasNext()) {
							final Map.Entry<Object, Object> entry = 
								entries.next();

							packet.put(
									(String) entry.getKey(), 
									entry.getValue());
						}

						packet.put(UI_PROTOCOL_CMD, TaskClientCommands.LOAD_UI);
						packet.put(UI_PROTOCOL_TASK_ID, tid);
						packet.put(UI_PROTOCOL_CAPABILITY_ID, cid);
						packet.put(UI_PROTOCOL_NAME, cap.getCapabilityName());

						// inform the UI that the interface is loaded

						setChanged();
						notifyObservers(packet);

						// this capability is now running
						cap.setStatus(ClientCapabilityStatus.RUNNING);
					}
				}
			}	   			    		
		}
	}

	/** 
	 * Terminate the running tasks.
	 * 
	 */
	public void endTasks() {

		/*
		 * Any task that is in running STATE should be killed via a CANCEL_TASK
		 * message
		 */
		for (TaskHandler handler : tasks.values()) {
			if (handler.getStatus() == TaskHandlerStatus.RUNNING) {
				
				final CancelTaskMessage kill =	
					new CancelTaskMessage(handler.getTaskId(), 
							TaskRequester.TASK_CLIENT,
							"The user decided to cancel all tasks");

				try {
					// send the message
					taskOutputChannel.send(bus.generateMessage(kill,
							TaskMessage.CANCEL_TASK));
					
					// terminate the task
					cancelTask(handler.getTaskId(), 
							"Task Client ending all tasks");

				} catch (JMSException e) {
					e.printStackTrace();
				}				
			}
		}
	}
	
	/**
	 * Inform the client that the task has been rejected.
	 * 
	 * @param rsvp the reservation for the rejected task.
 	 * @param reason the reason that the task was rejected.
	 */
	private void rejectTaskRequest(TaskReservation rsvp, String reason) {
		
		final ConcurrentHashMap<String, Object> packet =
			new ConcurrentHashMap<String, Object>();

		packet.put(UI_PROTOCOL_CMD, TaskClientCommands.REJECT_TASK);
		packet.put(UI_PROTOCOL_NAME, rsvp.getTaskName());
		packet.put(UI_PROTOCOL_REASON, reason);		
		
		// tell the client of the rejection
		setChanged();
		notifyObservers(packet);
	}
	
	/**
	 * Terminate and remove the task from the client.
	 * 
	 * @param tid the ID of the task to cancel
	 */
	private void cancelTask(Integer tid, String reason) {
		
		TaskHandler handler = tasks.get(tid);
		if (handler != null) {

			System.out.println(
					"[TaskClient] Task Canceled (task:"
					+ tid + ")");

			// send the cancel command to the UI
			final ConcurrentHashMap<String, Object> packet =
				new ConcurrentHashMap<String, Object>();

			packet.put(UI_PROTOCOL_CMD, TaskClientCommands.CANCEL_TASK);
			packet.put(UI_PROTOCOL_TASK_ID, tid);
			
			// tell the user of the reason (if it exists)
			if (reason != null) {
				packet.put(UI_PROTOCOL_REASON, reason);
			}

			setChanged();
			notifyObservers(packet);

			// remove the completed task - should we keep a history?
			tasks.remove(tid);
			handler = null;
		}							
	}

	/**
	 * Send input to the capability. This method generates a Capability Input 
	 * message destined for the target capability
	 * 
	 * @param taskId
	 *            the task ID
	 * @param capId
	 *            the capability ID
	 * @param input
	 *            the input as a set of key/values in a ConcurrentHashMap
	 */
	public void sendInput(final Integer taskId, 
			final Integer capId,
			final ConcurrentHashMap<Object, Object> input) {

		// For which task are we sending input
		final TaskHandler handler = tasks.get(taskId);

		if (handler != null) {

			// send input to the correct capability
			if (handler.hasCapability(capId)) {

				/*
				 * Send the input to the target capability
				 */
				final CapabilityInputMessage inputMsg = 
					new CapabilityInputMessage(
							taskId, capId, input);

				try {

					taskOutputChannel.send(bus.generateMessage(inputMsg,
							TaskMessage.CAPABILITY_INPUT));

				} catch (final JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Load the robot's dashboard if it has not yet been loaded.
	 */
	public void loadDashboard() {

		if (dashboardLoaded == false) {

			System.out.println("[TaskClient] Starting CoBot dashboard");
			loadNewTask( 
					new TaskReservation(
							ROBOT_DASHBOARD,
							1,
							REQUESTER_ID,
							(ConcurrentHashMap<Object,Object>)null,
							TaskType.LOGIC_TASK,
							"dashboard"
					)
			);

			// the dashboard may have to wait until the robot's camera is ready
			try { Thread.sleep(5000); } 
			catch (InterruptedException e) {  }

			continueTask();

			dashboardLoaded = true;
		}	
	}
	
	/**
	 * Terminate the dashboard task if it is running.
	 */
	public void unloadDashboard() { 
		for (TaskHandler handler : tasks.values()) {
			
			// Find the robot dashboard and kill it
			if (handler.getTaskName().equals(ROBOT_DASHBOARD)) {
				
				if (handler.getStatus() == TaskHandlerStatus.RUNNING) {
					
					final CancelTaskMessage kill =	
						new CancelTaskMessage(handler.getTaskId(), 
								TaskRequester.TASK_CLIENT,
								"The user decided to cancel the dashboard");

					try {
						// send the message
						taskOutputChannel.send(bus.generateMessage(kill,
								TaskMessage.CANCEL_TASK));
						
						// terminate the task
						cancelTask(handler.getTaskId(), 
								"Task Client ending dashboard");
						
						return;
						
					} catch (JMSException e) {
						e.printStackTrace();
					}				
				}
			}
		}		
	}

	/**
	 * Release the connection/resources to the task bus.
	 */
	public void unLoadClient() {
		bus.releaseConsumer(taskInputChannel);
		bus.releaseProducer(taskOutputChannel);
		bus.disconnect();
	}
}