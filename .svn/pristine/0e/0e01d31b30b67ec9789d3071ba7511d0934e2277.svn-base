/*
 * TaskReadyMessage.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskbus.messages;

import java.util.Vector;

import edu.cmu.gizmo.management.taskmanager.TaskInputMap;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;


/**
 * The Class TaskReadyMessage.
 */
public class TaskReadyMessage implements TaskMessage {

	/** The task id. */
	private Integer taskId;
	
	/** The task settings. */
	private Vector<TaskInputMap> taskRoutes;
	
	/** The reservation of this task. */
	private TaskReservation rsvp;
	
	/**
	 * Instantiates a new task ready message.
	 *
	 * @param tid the tid
	 * @param rid the rid
	 * @param settings the settings
	 */
	public TaskReadyMessage(Integer tid, TaskReservation res, 
			Vector<TaskInputMap>  settings) {
		
		taskId = tid;
		rsvp = res;
		taskRoutes = settings;
	}
	
	/**
	 * Gets the task id.
	 *
	 * @return the task id
	 */
	public Integer getTaskId() {
		return taskId;
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
		return TaskMessage.TASK_READY;
	}

	/**
	 * Gets the task settings.
	 *
	 * @return the task settings
	 */
	public Vector<TaskInputMap> getTaskRoutes() {
		return taskRoutes;
	}
}