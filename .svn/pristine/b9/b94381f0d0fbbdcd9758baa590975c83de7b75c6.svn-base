/*
 * Cobot3TaskProxy.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import edu.cmu.gizmo.management.robot.RobotTaskProxy;
import edu.cmu.gizmo.management.taskbus.GizmoTaskBus;
import edu.cmu.gizmo.management.taskbus.messages.CancelTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityInputMessage;
import edu.cmu.gizmo.management.taskbus.messages.CapabilityOutputMessage;
import edu.cmu.gizmo.management.taskbus.messages.HeloClientMessage;
import edu.cmu.gizmo.management.taskbus.messages.LoadTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.StartCapabilityMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskCompleteMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskReadyMessage;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskType;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;
/**
 * The Class Cobot3TaskProxy. This class serves as the robot's interface to the
 * tasking system.
 * 
 * @version 1.0
 * @author Jeff Gennari	
 */
public class Cobot3TaskClient 
implements Runnable, RobotTaskProxy, GizmoTaskClient, MessageListener {

	/** the ID for the new task started by this robot */
	private Integer taskId;

	/** the ID for the new capability started by this robot */
	private Integer capabilityId;

	/** A flag to indicate this capability is running. */
	private Boolean running;

	/** The publisher to the task bus. */
	private MessageProducer taskPublisher;

	/** The task message subscriber. */
	private MessageConsumer taskSubscriber;

	/** The action to take when the robot gets in trouble */
	private final String CONTINGENCY_PLAN = "GoToRoomCapability";
	
	/** Home is where the power is. */
	private final String HOME_BASE_VAL = "274";
	
	private final String HOME_BASE_PARAM = "room";
	
	/** Reasons for why the cobot cannot continue with a task */
	private ConcurrentHashMap<String, String> cannedCancelReasons;
	
	/** The task bus. */
	private GizmoTaskBus bus;

	/** A connection to the robot on which to receive tasking */
	private BufferedReader taskInput; 

	/** A connection to the robot on which to send task output */
	private PrintWriter taskOutput;
	/**
	 * Instantiates a new cobot3 task proxy capability.
	 */
	public Cobot3TaskClient() {

		running = false;

		bus = GizmoTaskBus.connect();
		taskInput = null;
		taskId = null;
		capabilityId = null;

		cannedCancelReasons = new ConcurrentHashMap<String, String>() {{
			put("batteryLow", 
					"Sorry, CoBot 3 cannot complete the current task because its battery is too low");
			put("atBoundary", 
					"Sorry, CoBot 3 cannot move past this point");
		}};
		
		/* 
		 * this class should behave as a simple client
		 * it simply runs until it doesn't
		 */

		final String[] Cobot3ClientTaskProtocol = { 
				TaskMessage.TASK_READY,
				TaskMessage.HELO_CLIENT,
				TaskMessage.TASK_COMPLETE,
				TaskMessage.CAPABILITY_OUTPUT
		};

		taskPublisher = bus.getTaskProducer();
		taskSubscriber = bus.getTaskConsumer(Cobot3ClientTaskProtocol);

		try {
			taskSubscriber.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadNewTask(TaskReservation rsvp) {

		/*
		 * Accept and process the new task
		 */

		// PROB,[String describing the problem],[parameters]

		System.out.println("[Cobot3TaskClient] starting robot task");
		try {
			
			if (taskPublisher != null) {
			
				// send the new request
				final LoadTaskMessage loadMessage =
					new LoadTaskMessage(rsvp);
				
				taskPublisher.send(
						bus.generateMessage(
								loadMessage, 
								TaskMessage.LOAD_TASK
						)
				);
			}
			else {
				System.out.println("[Cobot3TaskClient] cannot start task");
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endTasks() {

		final CancelTaskMessage kill =	
			new CancelTaskMessage(taskId, 
					TaskRequester.ROBOT_CLIENT,
					"Robot client canceled needs to end all tasks");

		try {
			if (taskPublisher != null) {
				// send the message
				taskPublisher.send(
						bus.generateMessage(kill,
								TaskMessage.CANCEL_TASK));
			}
		} 
		catch (JMSException e) {
			e.printStackTrace();
		} 
	}	

	@Override
	public final void onMessage(final Message message) {

		try { 
			TaskMessage taskMsg = 
				(TaskMessage) ((ObjectMessage) message).getObject();

			if (taskMsg.getMessageType() == TaskMessage.TASK_READY) {
				handleTaskReady( (TaskReadyMessage) taskMsg);

			}
			else if (taskMsg.getMessageType() == TaskMessage.HELO_CLIENT) {
				handleHeloClient((HeloClientMessage) taskMsg);
			}
			else if (taskMsg.getMessageType() == TaskMessage.CAPABILITY_OUTPUT) {
				handleCapabilityOutput((CapabilityOutputMessage) taskMsg);
			}
		} catch (final JMSException e) {
			e.printStackTrace();
		}
	}
	private void handleCapabilityOutput(CapabilityOutputMessage outputMsg) {
		
		if (taskId == null || capabilityId == null) {
			return;
		}
		
		if (outputMsg.getTaskId().compareTo(taskId) == 0) {
			
			
			if (outputMsg.getCapabilityId().compareTo(capabilityId) == 0) {
			
				/*
				 * Found the task/capability
				 */
				final ConcurrentHashMap<Object, Object> output = 
					outputMsg.getOutput();
				
				final Iterator<Map.Entry<Object, Object>> entries = 
					output.entrySet().iterator();

				final StringBuffer buf = new StringBuffer();
				
				while (entries.hasNext()) {
					final Map.Entry<Object, Object> entry = entries.next();
					buf.append((String) entry.getKey());
					buf.append(" = ");
					buf.append((String) entry.getValue());
					
					// in the future this will be sent back to the robot
					System.out.println("[Cobot3TaskClient] Task output: " 
							+ buf.toString());
				}				
			}		
		}
	}

	private void handleHeloClient(HeloClientMessage heloMsg) {

		// if the taskId is null then this client has yet to be tasked
		if (taskId == null) {
			return;
		}
		if (heloMsg.getTaskId().compareTo(taskId) == 0) {

			try {
				
				capabilityId = heloMsg.getCapabilityId();
				
				// start the capability
				final StartCapabilityMessage startMsg = 
					new StartCapabilityMessage(taskId, capabilityId);

				System.out.println("[Cobot3TaskClient] Starting task (" 
						+ taskId + ":" + capabilityId +")" );

				taskPublisher.send(
						bus.generateMessage(startMsg,
								TaskMessage.START_CAPABILITY));
				
				/*
				 * Send the input to the new task
				 */
				
				ConcurrentHashMap<Object, Object> input = 
					new ConcurrentHashMap<Object, Object>();

				// currently, we will simply send the CoBot home 
				input.put(HOME_BASE_PARAM, HOME_BASE_VAL);

				final CapabilityInputMessage inputMsg = 
					new CapabilityInputMessage(
							taskId, capabilityId, input);
				
				taskPublisher.send(
						bus.generateMessage(inputMsg,
								TaskMessage.CAPABILITY_INPUT));
			} 
			catch (final JMSException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleTaskReady(TaskReadyMessage readyMsg) {
		
		if (readyMsg.getReservation().getTaskRequester() == TaskRequester.ROBOT_CLIENT) {
			System.out.println("[Cobot3TaskClient] Got Task Ready");
			taskId = readyMsg.getTaskId();			
		}
	}
	
	/**
	 * terminate the task proxy.
	 */
	public void uninstallTaskProxy() { 

		try {

			running = false;

			// end the running tasks, close connections, and terminate
			endTasks();

			bus.releaseConsumer(taskSubscriber);
			taskSubscriber = null;

			bus.releaseProducer(taskPublisher);	
			taskPublisher = null;

			bus.disconnect();

			if (taskInput != null) {
				taskInput.close();
				taskInput = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void installTaskProxy(Socket taskChannel) {
		try {

			taskInput = 
				new BufferedReader(
						new InputStreamReader(
								taskChannel.getInputStream()
						)
				);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {

		System.out.println("[Cobot3TaskClient] executing");

		running = true;		
		while (running == true) {
			try {
				if ( taskInput != null) {
					
					System.out.println("[Cobot3TaskClient] waiting for task request...");
					String request = taskInput.readLine();
					
					if (request != null) {
						
						System.out.println("[Cobot3TaskClient] Received a new robot task request: "  + 
								request);
						
						/* 
						 * parse request into reservation using the data from the  
						 * request to decide how to respond requestTask(rsvp);
						 */
						
						ConcurrentHashMap<Object,Object> reason = 
							new	ConcurrentHashMap<Object,Object>();
						
						// select the correct error message
						reason.put("reason", cannedCancelReasons.get("batteryLow"));
						
						TaskReservation req = 
							new TaskReservation(
									"Battery Fault",
									1,
									TaskRequester.ROBOT_CLIENT,
									reason,
									TaskType.LOGIC_TASK, 
									CONTINGENCY_PLAN
							); 	

						// issue a new request to send the robot home.
						loadNewTask(req);
						System.out.println("[Cobot3TaskClient] new task submitted");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
