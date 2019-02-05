/*
 * GizmoTaskClient.java Jul 23, 2012 1.0
 */
package edu.cmu.gizmo.management.taskclient;

import edu.cmu.gizmo.management.taskmanager.TaskReservation;

/**
 * The interface that all task clients must implement. 
 * 
 * @author Jeff Gennari
 *
 */
public interface GizmoTaskClient {

	/**
	 * End all outstanding tasks.
	 */
	public abstract void endTasks();

	/**
	 * start a new task via a reservation.
	 * 
	 * @param rsvp the <code>TaskReservation</code>
	 */
	public abstract void loadNewTask(TaskReservation rsvp);

}