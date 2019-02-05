/*
 * GroupedTask.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.scripttask;

import java.util.ArrayList;


/**
 * The Class GroupedTask.
 */
public class GroupedTask {
	
	/** The grouped task id. */
	private int groupedTaskId = -1;
	
	/** The tasks. */
	private ArrayList<Task> tasks = new ArrayList<Task>(0);
	
	/** The grouped task status. */
	private GroupedTaskStatus groupedTaskStatus = null;
	
	/** The grouped task depends on. */
	private GroupedTaskDependsOn groupedTaskDependsOn = null;
	
	/** The action. */
	private GroupedTaskAction action = null;
		

	/**
	 * Gets the grouped task id.
	 *
	 * @return the grouped task id
	 */
	public int getGroupedTaskId() {
		return groupedTaskId;
	}
	
	/**
	 * Sets the grouped task id.
	 *
	 * @param groupedTaskId the new grouped task id
	 */
	public void setGroupedTaskId(int groupedTaskId) {
		this.groupedTaskId = groupedTaskId;
	}
	
	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 */
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	
	/**
	 * Sets the tasks.
	 *
	 * @param tasks the new tasks
	 */
	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
	
	/**
	 * Gets the grouped task status.
	 *
	 * @return the grouped task status
	 */
	public GroupedTaskStatus getGroupedTaskStatus() {
		return groupedTaskStatus;
	}
	
	/**
	 * Sets the grouped task status.
	 *
	 * @param groupedTaskStatus the new grouped task status
	 */
	public void setGroupedTaskStatus(GroupedTaskStatus groupedTaskStatus) {
		this.groupedTaskStatus = groupedTaskStatus;
	}
	
	/**
	 * Gets the grouped task depends on.
	 *
	 * @return the grouped task depends on
	 */
	public GroupedTaskDependsOn getGroupedTaskDependsOn() {
		return groupedTaskDependsOn;
	}
	
	/**
	 * Sets the grouped task depends on.
	 *
	 * @param groupedTaskDependsOn the new grouped task depends on
	 */
	public void setGroupedTaskDependsOn(GroupedTaskDependsOn groupedTaskDependsOn) {
		this.groupedTaskDependsOn = groupedTaskDependsOn;
	}
	
	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public GroupedTaskAction getAction() {
		return action;
	}
	
	/**
	 * Sets the action.
	 *
	 * @param action the new action
	 */
	public void setAction(GroupedTaskAction action) {
		this.action = action;
	}	
}
