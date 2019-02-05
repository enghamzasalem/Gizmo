package edu.cmu.gizmo.management.taskbus.messages;

import edu.cmu.gizmo.management.taskmanager.TaskReservation;

public class RejectTaskMessage implements TaskMessage {

	/** the reservation for the task. */
	private TaskReservation rsvp;
	
	/** the reason the task was rejected */
	private String reason; 
	
	/**
	 * Create a new LoadTaskMessage.
	 *
	 * @param res the reservation
	 */
	public RejectTaskMessage(final TaskReservation reservation, final String cause) {
		rsvp = reservation;
		reason = cause;
	}	
	
	/**
	 * Gets the reason for the rejection.
	 *
	 * @return the reason
	 */
	public String getReason() { 
		return reason;
	}
	
	/**
	 * Gets the reservation.
	 *
	 * @return the reservation
	 */
	public TaskReservation getReservation() { 
		return rsvp;
	}

	/**
	 * Gets the task description.
	 *
	 * @return the task description
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.REJECT_TASK;
	}

}
