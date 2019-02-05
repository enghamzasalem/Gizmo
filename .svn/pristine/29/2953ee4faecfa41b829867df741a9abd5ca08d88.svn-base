/*
 * TaskManager.java 1.0 2012-06-29
 */

package edu.cmu.gizmo.management.taskmanager;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import edu.cmu.gizmo.management.robot.Robot;
import edu.cmu.gizmo.management.robot.RobotFactory;
import edu.cmu.gizmo.management.robot.RobotFactory.RobotModel;
import edu.cmu.gizmo.management.robot.RobotTaskProxy;
import edu.cmu.gizmo.management.taskbus.GizmoTaskBus;
import edu.cmu.gizmo.management.taskbus.messages.CancelTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.LoadTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.RejectTaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskCompleteMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskMessage;
import edu.cmu.gizmo.management.taskbus.messages.TaskReadyMessage;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskParameter;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskType;
import edu.cmu.gizmo.management.taskmanager.TaskStatus.TaskStatusValue;
import edu.cmu.gizmo.management.taskmanager.exceptions.TaskConfigurationIncompleteException;
import edu.cmu.gizmo.management.taskmanager.taskplanner.RobotFocusedTaskPlanEvaluator;
import edu.cmu.gizmo.management.taskmanager.taskplanner.TaskPlan;
import edu.cmu.gizmo.management.taskmanager.taskplanner.TaskPlanner;

/**
 * The main task manager. The responsibilities of the Task Manager include
 * starting tasks, ending tasks, and conflict management
 * 
 * @verson 1.0 2012-060-29
 * @author Arpita Shrivastava
 */
public class TaskManager implements Observer, MessageListener {

	/**
	 * The list of entities asking for tasks.
	 */
	public static enum TaskRequester {

		/** The task client. */
		TASK_CLIENT,

		/** The robot proxy. */
		ROBOT_CLIENT,

		/** The task manager */
		TASK_MANAGER
	};

	/** the task planner responsible for managing task conflicts */
	private final TaskPlanner taskPlanner;

	/** The connection to the task bus. */
	private final GizmoTaskBus bus;

	/** The CoBot 3 robot. */
	public Robot robot;

	/** provide a task interface to the robot */
	private RobotTaskProxy robotTaskProxy;

	/** the list of running tasks. */
	private ConcurrentHashMap<Integer, TaskExecutor> runningTasks;

	/** The task input channel. */
	private MessageConsumer input;	

	/** The pool of task IDs. */
	private Integer taskIdPool;

	/** task messages fielded by the TaskManager */
	private final String[] subscriptions = { 
			TaskMessage.LOAD_TASK,
			TaskMessage.CANCEL_TASK 
	};

	/**
	 * Create a new TaskManager.
	 */
	public TaskManager() {

		bus = GizmoTaskBus.connect();

		taskPlanner = new TaskPlanner(new RobotFocusedTaskPlanEvaluator());

		if (bus == null) {
			System.out
			.println("[TaskManager] Could not connect to the Task bus");
		} 
		else {

			// connect to the bus
			input = bus.getTaskConsumer(subscriptions);

			try {
				input.setMessageListener(this);
			} catch (final JMSException e) {
				e.printStackTrace();
			}

			taskIdPool = 0;

			runningTasks = new ConcurrentHashMap<Integer, TaskExecutor>();

			robotTaskProxy = null;

			robot = null;

			/*
			 * start a new thread to connect the robot ... in the future this
			 * will become more sophisticated
			 */
			new Thread(new Runnable() {

				@Override
				public void run() {

					// connect the robot and task proxy
					robot = 
						RobotFactory.newRobot(RobotModel.COBOT3);

					robotTaskProxy = 
						RobotFactory.newTaskProxy(RobotModel.COBOT3); 

					if (robotTaskProxy == null) {
						throw new 
						NullPointerException(
						"RobotTaskProxy could not be connected!");
					}

					new Thread((Runnable) robotTaskProxy).start();

					System.out.println("[TaskManager] Starting Task proxy: "  + 
							robotTaskProxy.getClass());
				}

			}).start();
		}
	}

	/**
	 * Load a new task. This method loads the new task and creates the task
	 * executor. The newly created task executor is not yet running.
	 * 
	 * @param rsvp
	 *            the new <code>TaskReservation</code>
	 *            
	 * @return the created <code>TaskExecutor</code> or null.
	 */
	public TaskExecutor loadNewTask(final TaskReservation rsvp) {
		// connect to the robot - this blocks until the robot is *first*
		// connected

		while (robot == null) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException ie) {
			}
		}

		if (rsvp == null) {
			return null;
		}

		/*
		 * Get taskType and taskPlan from the Scheduling database associated
		 * with the taskId
		 */

		final TaskExecutor.TaskType taskType = rsvp.getTaskExeType();
		final String task = rsvp.getTaskPlan();

		try {

			final ConcurrentHashMap<TaskParameter, Object> config = new ConcurrentHashMap<TaskParameter, Object>();

			// increment the task ID pool to keep tasks unique
			final Integer thisTaskId = new Integer(taskIdPool.intValue());

			config.put(TaskParameter.ROBOT, robot);
			config.put(TaskParameter.TYPE, taskType);
			config.put(TaskParameter.TASK, task);
			config.put(TaskParameter.ID, thisTaskId);
			config.put(TaskParameter.RESERVATION, rsvp);

			// handle task ID wrapping
			taskIdPool = ((taskIdPool + 1) == Integer.MAX_VALUE) ? 1
					: taskIdPool + 1;

			return new TaskExecutor(config);

		} catch (final TaskConfigurationIncompleteException e) {
			// we need to handle this better
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Start a new task. This method configures and starts a task.
	 * 
	 * @param rsvp
	 *            the rsvp
	 */
	public synchronized void startNewTask(final TaskExecutor newTaskExe) {

		/*
		 * Start a new task executor and set it running
		 */

		System.out.println("[TaskManager] Starting new "
				+ newTaskExe.getReservation().getTaskName() + " task ("
				+ newTaskExe.getTaskId() + ") from "
				+ newTaskExe.getReservation().getTaskRequester());

		runningTasks.put(newTaskExe.getTaskId(), newTaskExe);

		newTaskExe.addObbserver(this);

		// start the new task as a separate thread
		new Thread(newTaskExe).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public synchronized void update(final Observable o, final Object update) {
		
		try {	
			final TaskStatus status = (TaskStatus) update;
			
			if (bus.isConnected() == false) {
				return;
			}
			
			final Integer tid = status.getTaskId();
			final MessageProducer taskControlPub = 	bus.getTaskProducer();			

			// inform the client that the task is complete

			if (status.getStatus() == TaskStatusValue.COMPLETE) {

				System.out.println("[TaskManager] Task Complete");
				final TaskCompleteMessage tcm = new TaskCompleteMessage(tid);

				taskControlPub.send(bus.generateMessage(tcm,
						TaskMessage.TASK_COMPLETE));

				runningTasks.remove(tid);
			}

			/*
			 * If the submitted executor is READY then it has been accepted The
			 * READY message should include the task-specific settings for the
			 * task, such as the I/O connections.
			 */
			else if (status.getStatus() == TaskStatusValue.READY) {

				System.out
				.println("[TaskManager] new Task ready (" + tid + ")");

				final TaskExecutor exe = runningTasks.get(tid);
				final TaskReservation rsvp = exe.getReservation();

				Vector<TaskInputMap> routes = null;

				// if this is a scripted task then send the I/O plan
				if (exe.getTaskType() == TaskType.SCRIPT_TASK) {

					if (status.getStatusMessage() instanceof Vector<?>) {
						routes = (Vector<TaskInputMap>) status.getStatusMessage();
						for (TaskInputMap r : routes) { 
							System.out.println("[TaskManager] I/O Map: " + r.toString());
						}
					}
				} else {
					// no settings
					routes = null;
				}

				final TaskReadyMessage ready = new TaskReadyMessage(tid,
						rsvp, routes);

				taskControlPub.send(
						bus.generateMessage(ready,
								TaskMessage.TASK_READY));

			}
		
			/*
			 * The task is in error. Currently this just ends the task
			 */
			else if (status.getStatus() == TaskStatusValue.ERROR) {

				System.out.println("[TaskManager] ERROR: "
						+ status.getStatusMessage());
				
				final CancelTaskMessage cancel = 
					new CancelTaskMessage(
						status.getTaskId(), 
						TaskRequester.TASK_MANAGER,
						(String) status.getStatusMessage()); // this is the reason that the task was canceled

				taskControlPub.send(bus.generateMessage(cancel,
						TaskMessage.CANCEL_TASK));
				
			}

			/*
			 * The task has been terminated
			 */
			else if (status.getStatus() == TaskStatusValue.CANCELED) {
				
				System.out.println("[TaskManager] Task terminated (" 
						+ status.getTaskId()+")");
				
				final CancelTaskMessage cancel = 
					new CancelTaskMessage(
						status.getTaskId(), 
						TaskRequester.TASK_MANAGER,
						(String) status.getStatusMessage()); // this is the reason that the task was canceled

				taskControlPub.send(bus.generateMessage(cancel,
						TaskMessage.CANCEL_TASK));
			}
			
			bus.releaseProducer(taskControlPub);
			
		} catch (final JMSException e) {
			e.printStackTrace();
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public synchronized void onMessage(final Message busMessage) {
		
		try {
			final ObjectMessage om = (ObjectMessage) busMessage;
			final TaskMessage message = (TaskMessage) om.getObject();

			// handle the message
			handleTaskMessage(message);

		} catch (final JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle message from the task bus.
	 * 
	 * @param message
	 *            the input message to the Task Manager
	 */
	public final synchronized void handleTaskMessage(final TaskMessage message) {

		/*
		 * Load a new task. The steps for loading a new task are as follows:
		 * 
		 * 1. evaluate any conflicts and produce a plan 2. execute the plan,
		 * which may involve killing other tasks 3. profit!
		 */
		if (message.getMessageType() == TaskMessage.LOAD_TASK) {

			final LoadTaskMessage newTaskReq = (LoadTaskMessage) message;

			// create an executor for this new task
			final TaskExecutor newTaskExe = 
				loadNewTask(newTaskReq.getReservation());

			if (newTaskExe != null) {
								
				// evaluate the new task request to determine if it can be run
				final TaskPlan executionPlan = 
					taskPlanner.evaluate(newTaskExe, runningTasks.values());
		
				/* 
				 * check to see if the new task was 
				 * accepted - it is in the list of tasks to start 
				 */
				if (executionPlan.getBirthList().contains(newTaskExe)) {
					
					System.out.println("[TaskManager] New task accepted: " 
							+ newTaskExe.getReservation().getTaskName() 
							+ "(" + newTaskExe.getTaskId() +")");
					
					// execute the task
					executeTaskPlan(executionPlan);
				}
				else {
					// the new task was rejected!
					final MessageProducer taskPub = bus.getTaskProducer();					
					
					try {
						taskPub.send(
								bus.generateMessage(
										new RejectTaskMessage(
												newTaskExe.getReservation(),
												"New task could not be accepted"), 
										TaskMessage.REJECT_TASK)
										);
					} catch (JMSException e) {
						e.printStackTrace();
					}
					bus.releaseProducer(taskPub);
				}	

			} 
			else {
				System.out.println(
						"[TaskManager] Could not start new task because it is null");
			}
		}

		/*
		 * Cancel a running task
		 */
		else if (message.getMessageType() == TaskMessage.CANCEL_TASK) {
			final CancelTaskMessage cancel = (CancelTaskMessage) message;

			// Cancel the task if the request is not from the task manager
			if (cancel.getRequester() != TaskRequester.TASK_MANAGER) {

				if (runningTasks.containsKey(cancel.getTaskId())) {

					final TaskExecutor task = 
						runningTasks.get(cancel.getTaskId());

					task.terminate(cancel.getReason()); // kill the task
				}
			}
		}
	}

	/**
	 * Executing a task plan means killing the things to kill and starting the
	 * things to start
	 * 
	 * @param plan the <code>TaskPlan</code> to execute.
	 */
	public void executeTaskPlan(final TaskPlan plan) {

		// kill the old tasks
		for (final TaskExecutor toKill : plan.getKillList()) {
			toKill.terminate(plan.getKillReason());
		}

		// start the new tasks
		for (final TaskExecutor toLive : plan.getBirthList()) {
			startNewTask(toLive);
		}
	}
	
	/**
	 * Unload the task manager by ending all tasks, closing bus connections, and
	 * shutting down the robot task proxy (if it is running).
	 */
	public synchronized void unload() { 
		
		// kill everything
		for (TaskExecutor exe : runningTasks.values()) {
			exe.terminate("Sorry, the Gizmo Task Manager is unloading");
		}
		
		if (robotTaskProxy != null) { 
			robotTaskProxy.uninstallTaskProxy();
		}
		
		bus.releaseConsumer(input);
		bus.disconnect();		
	}

	/**
	 * The main method for the <code>TaskManager</code>
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		new TaskManager();
	}
}