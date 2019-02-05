/*
 * LoadTaskMessage.java 1.0 2012-06-27
 */
package edu.cmu.gizmo.management.taskbus.messages;

import edu.cmu.gizmo.management.taskmanager.TaskReservation;


/**
 * This message initiates a new task.
 *
 * @version 1.0 2012-06-2012
 * @author Jeff Gennari
 */
public class LoadTaskMessage implements TaskMessage {

	/** the reservation for the task. */
	private TaskReservation rsvp;
	
	/**
	 * Create a new LoadTaskMessage.
	 *
	 * @param res the reservation
	 */
	public LoadTaskMessage(TaskReservation res) {
		rsvp = res;
	}	
	
	/**
	 * Gets the reservation.
	 *
	 * @return the reservation
	 */
	public TaskReservation getReservation() { 
		return rsvp;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.LOAD_TASK;
	}
}