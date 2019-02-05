/*
 * TaskExecutor.java 1.1 2012-06-23
 */
package edu.cmu.gizmo.management.taskmanager;

import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.robot.Robot;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.exceptions.CapabilityNotFoundException;
import edu.cmu.gizmo.management.taskmanager.exceptions.TaskConfigurationIncompleteException;
import edu.cmu.gizmo.management.taskmanager.exceptions.TaskPlanNotFoundException;


/**
 * This class is determines how best to execute a task. It is the abstract
 * strategy part of a strategy pattern.
 * 
 * @version 1.0 2012-06-19
 * @author Jeff Gennari
 * 
 */
public class TaskExecutor implements Runnable, Comparable<TaskExecutor>  {

	/** A connection to the robot. */
	private Robot robot = null;
	
	/** The execution strategy to use for this task (LOGIC or SCRIPT). */
	private TaskExecutionStrategy executionStrategy = null;

	/** the ID for this task. */
	private Integer taskId = null;
	
	/** the reservation for this task. */
	private TaskReservation rsvp = null; 
	
	/** The task for this executor */
	private String task = null;

	/** The type for this task. */
	private TaskType type = null;
	
	/*
	 * The following  are the names of configuration parameters that 
	 * each task may use
	 */
	
	public static enum TaskParameter {
		ROBOT,
		ID,
		TASK,
		TYPE,
		RESERVATION
	};
	
	/**
	 * The type of task to execute.
	 */
	public static enum TaskType {

		/** The script task. */
		SCRIPT_TASK, 
		/** The logic task. */
		LOGIC_TASK
	};

	/**
	 * Create a new TaskExecutor with a specific strategy.
	 *
	 * @param config the configuration for this task
	 * @throws TaskConfigurationIncompleteException the task configuration incomplete exception
	 */
	public TaskExecutor(final ConcurrentHashMap<TaskParameter, Object> config)
	throws TaskConfigurationIncompleteException {		

		// get the rsvp
		if (config.containsKey(TaskParameter.RESERVATION))  {
			rsvp = (TaskReservation) config.get(TaskParameter.RESERVATION);
		}
		else {
			throw new TaskConfigurationIncompleteException(
					"Strategy configuration incomplete: no reservation"); 
		}
		
		// get the robot
		if (config.containsKey(TaskParameter.ROBOT)) {
			robot = (Robot) config.get(TaskParameter.ROBOT);
		}
		else {
			throw new TaskConfigurationIncompleteException(
			"Strategy configuration incomplete: no reservation"); 
		}
		
		// get the task ID 
		if (config.containsKey(TaskParameter.ID)) {
			taskId = (Integer) config.get(TaskParameter.ID);
		}
		else {
			throw new TaskConfigurationIncompleteException(
			"Strategy configuration incomplete: no reservation"); 
		}
		
		// get the task ID 
		if (config.containsKey(TaskParameter.TASK)) {
			task = (String) config.get(TaskParameter.TASK);
		}
		else {
			throw new TaskConfigurationIncompleteException(
			"Strategy configuration incomplete: no reservation"); 
		}
		
		if (config.containsKey(TaskParameter.TYPE)) {
			
			type =
				(TaskExecutor.TaskType) config.get(TaskParameter.TYPE);

			// configure a script task
			if (type == TaskExecutor.TaskType.SCRIPT_TASK) {

				try {
					executionStrategy = 
						new ScriptTaskStrategy(robot, taskId, task);
				} catch (final TaskPlanNotFoundException e) {
					e.printStackTrace();
				}
			
			}
			
			else if (type == (TaskExecutor.TaskType.LOGIC_TASK)) {
				try {

					executionStrategy = 
						new LogicTaskStrategy(robot, taskId, task);

				} catch (final CapabilityNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			throw new TaskConfigurationIncompleteException(
			"Strategy configuration incomplete: no type");
		}
			
		if (executionStrategy == null) {
			StringBuffer errMsg = new StringBuffer("[");
			if (robot == null)
				errMsg.append(" invalid robot ");
			if (taskId == null)
				errMsg.append(" invalid taskId ");
			if (type == null)
				errMsg.append(" invalid type ");
			if (!config.containsKey(TaskParameter.TASK))
				errMsg.append(" invalid taskPlan ");
			if (task == null)
				errMsg.append(" invalid task ");
			if (rsvp == null)
				errMsg.append(" invalid reservation ");
			throw new TaskConfigurationIncompleteException(
					"Strategy configuration incomplete: " + errMsg.toString() 
					+ "]");
		}
	}

	/**
	 * Get the task ID associated with this executor.
	 *
	 * @return the task ID
	 */
	public Integer getTaskId() {
		return taskId;
	}
	
	/**
	 * Get the owner of this task
	 * 
	 * @return the owner
	 */
	public TaskReservation getReservation() { 
		return rsvp;
	}

	/**
	 * Gets the task type.
	 *
	 * @return the task type
	 */
	public TaskType getTaskType() {
		return type;
	}

	/**
	 * Add an observer to this TaskExecutionStrategy.
	 *
	 * @param observer the observer for the strategy
	 */
	public void addObbserver(final Observer observer) {
		executionStrategy.addObserver(observer);
	}

	/**
	 * Execute the task strategy in it's own thread.
	 */
	@Override
	public void run() {
		executionStrategy.execute();
	}

	/**
	 * Pause the current task.
	 *
	 * @return a <code>Tasks</code> object with the saved state for this task
	 */
	public Object pause() {
		return executionStrategy.pause();
	}

	/**
	 * Restore a task to its previous state.
	 *
	 * @param state the <code>Tasks</code> state to restore
	 */
	public void resume(Object state) {
		executionStrategy.resume(state);
		new Thread(this).start();
	}

	/**
	 * Terminate the current task.
	 */
	public void terminate(final String reason) {
		executionStrategy.terminte(reason);
	}

	/**
	 * Compare two task executors by task ID.
	 */
	@Override
	public int compareTo(TaskExecutor comp) {
		return comp.taskId.compareTo(this.taskId); 
	}
}