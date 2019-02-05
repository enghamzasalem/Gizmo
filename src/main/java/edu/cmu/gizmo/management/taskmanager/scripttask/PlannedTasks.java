/*
 * PlannedTasks.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.scripttask;

import java.util.ArrayList;


/**
 * The Class PlannedTasks.
 */
public class PlannedTasks {
	
	/** The grouped task. */
	private ArrayList<GroupedTask> groupedTask = new ArrayList<GroupedTask>(0);

	/**
	 * Gets the grouped task.
	 *
	 * @return the grouped task
	 */
	public ArrayList<GroupedTask> getGroupedTask() {
		return groupedTask;
	}

	/**
	 * Sets the grouped task.
	 *
	 * @param groupedTask the new grouped task
	 */
	public void setGroupedTask(ArrayList<GroupedTask> groupedTask) {
		this.groupedTask = groupedTask;
	}
}
