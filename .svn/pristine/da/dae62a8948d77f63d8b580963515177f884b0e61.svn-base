/*
 * TaskPlan.java Jul 20, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.taskplanner;

import java.util.Vector;

import edu.cmu.gizmo.management.taskmanager.TaskExecutor;

/**
 * The Class TaskPlan. A task plan is basically two lists of tasks:
 * <ol>
 *   <li> The set of tasks to start
 *   <li> The set of tasks to end
 * </ol>
 * 
 * That is, a task plan contains guidance on what tasks should be ended when a 
 * new task is proposed.  
 */
public class TaskPlan {

	/**
	 * The strategy to use for 
	 * @author jsg
	 *
	 */
	public static enum TerminationStrategy {
		TERMINATE, PAUSE
	} 
	
	/** The tasks to kill. */
	private final Vector<TaskExecutor> tasksToKill;

	/** The tasks to start. */
	private final Vector<TaskExecutor> tasksToStart;

	private String killReason;
	
	private TerminationStrategy killStrategy;
	
	/**
	 * Instantiates a new task plan.
	 */
	public TaskPlan() {

		tasksToKill = new Vector<TaskExecutor>();
		killReason = null;
		killStrategy = TerminationStrategy.TERMINATE; // default to kill
		tasksToStart = new Vector<TaskExecutor>();
		
	}

	/**
	 * Adds a task to the kill list.
	 * 
	 * @param walkingDead
	 *            the task to kill
	 */
	public void addTaskToKill(final TaskExecutor walkingDead) {

		tasksToKill.add(walkingDead);
	}

	/**
	 * Adds the task to start.
	 * 
	 * @param newBorn
	 *            the task to add
	 */
	public void addTaskToStart(final TaskExecutor newBorn) {

		tasksToStart.add(newBorn);
	}

	/**
	 * Gets the kill list.
	 * 
	 * @return the kill list
	 */
	public Vector<TaskExecutor> getKillList() {

		return tasksToKill;
	}

	/**
	 * Gets the birth list.
	 * 
	 * @return the birth list
	 */
	public Vector<TaskExecutor> getBirthList() {

		return tasksToStart;
	}

	/**
	 *  Included in the task plan is some indication as to why the tasks that 
	 *  will be killed need to be killed.
	 *  
	 * @param reason the reason to kill the task.
	 */
	public void addKillReason(final String reason) {
		killReason = reason;
		
	}

	/**
	 * Return the reason for killing this new task.
	 * 
	 * @return the reason as a String.
	 */
	public String getKillReason() {
		return killReason;
	}
	
	/**
	 *  Included in the task plan is some indication as to why the tasks that 
	 *  will be killed need to be killed.
	 *  
	 * @param reason the reason to kill the task.
	 */
	public void addKillStrategy(final TerminationStrategy strat) {
		killStrategy = strat;
		
	}

	/**
	 * Return the strategy for killing this new task.
	 * 
	 * @return the reason as a String.
	 */
	public TerminationStrategy getKillStrategy() {
		return killStrategy;
	}
}
