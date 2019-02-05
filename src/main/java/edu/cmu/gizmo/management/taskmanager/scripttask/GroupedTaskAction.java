/*
 * GroupedTaskAction.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.scripttask;


/**
 * The Class GroupedTaskAction.
 */
public class GroupedTaskAction {
	
	/** The grouped task status. */
	private GroupedTaskStatus groupedTaskStatus = null;
	
	/** The action. */
	private int action = -1;
	
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
	//-----------
	// 0 | email
	// 1 | rerun
	// 2 | xxxxx
	//-----------
	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	public int getAction() {
		return action;
	}
	
	/**
	 * Sets the action.
	 *
	 * @param action the new action
	 */
	public void setAction(int action) {
		this.action = action;
	}
}
