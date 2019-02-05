/*
 * CancelTaskMessage.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskbus.messages;

import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;


/**
 * The Class CancelTaskMessage.
 */
public class CancelTaskMessage implements TaskMessage {

	/** The task id. */
	private Integer taskId;

	/** The originator. */
	private TaskRequester requester;
		
	private String reason;
	
	/**
	 * Instantiates a new cancel task message.
	 *
	 * @param tid the tid
	 * @param orig the orig
	 */
	public CancelTaskMessage(Integer tid, TaskRequester orig, String cause) {
		
		requester = orig;
		taskId = tid;
		reason = cause;
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
	 * Gets the requester.
	 *
	 * @return the requester
	 */
	public TaskRequester getRequester() { 
		return requester;
	}
	
	/**
	 * Gets the reason for canceling.
	 *
	 * @return the reason for canceling the task
	 */
	public String getReason() { 
		return reason;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.CANCEL_TASK;
	}
}
