/*
 * TaskStatus.java 1.0 2012-06-21
 */
package edu.cmu.gizmo.management.taskmanager;


/**
 * This class represents a task status.
 *
 * @version 1.0
 * @author Jeff Gennari
 */
public class TaskStatus {
	
	/**
	 *  The status values that a task may take. The values have the 
	 *  following meanings:
	 *  <ul>
	 *  <li> INIT: the initial status when the strategy is not configured
	 *  <li> READY: The task is ready to begin execution (i.e. 
	 *              capabilities are loaded)
	 *  <li> RUNNING: the task is executing
	 *  <li> COMPLETE: the task completed successfully
	 *  <li> ERROR: the task experienced an error
	 *  <li> PAUSED: the task is paused
	 *  <li> CANCELED: the task was terminated before it completed
	 *  </ul>   
	 */
	public static enum TaskStatusValue { 
		
		/** The init. */
		INIT, 
 /** The ready. */
 READY, 
 /** The canceled. */
 CANCELED, 
 /** The running. */
 RUNNING, 
 /** The complete. */
 COMPLETE, 
 /** The error. */
 ERROR, 
 /** The paused. */
 PAUSED 
	};
	
	/** The status value. */
	private TaskStatusValue status;
	
	/** The status message associated with the status value. */
	private Object statusUpdate;
	
	/** The ID of the task associated with this status. */
	private Integer taskId;
	
	/**
	 * Create a new task status.
	 *
	 * @param tid the task ID
	 * @param v the task status value
	 * @param m the task status message
	 */
	public TaskStatus(Integer tid, TaskStatusValue v, Object m) {
		taskId = tid;
		status = v;
		statusUpdate = m;
	}
	
	/**
	 * Get the task ID.
	 *
	 * @return the task ID
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * Get the status value.
	 *
	 * @return the status value
	 */
	public TaskStatusValue getStatus() {
		return status;
	}
		
	/**
	 * Get the status message.
	 *
	 * @return the status message
	 */
	public Object getStatusMessage() {
		return statusUpdate;
	}
}