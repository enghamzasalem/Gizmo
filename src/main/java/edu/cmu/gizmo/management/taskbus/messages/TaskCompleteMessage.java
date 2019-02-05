/*
 * TaskCompleteMessage.java 1.0 2012-06-29
 */

package edu.cmu.gizmo.management.taskbus.messages;


/**
 * Message for task completion.
 *
 * @version 1.0 2012-06-29
 * @author Jeff Gennari
 */
public class TaskCompleteMessage implements TaskMessage {

	/** The ID for the completed task. */
	private Integer taskId;
	
	/**
	 * Instantiates a new task complete message.
	 *
	 * @param tid the tid
	 */
	public TaskCompleteMessage(Integer tid)  {
		taskId = tid;
	}
	
	/**
	 * Gets the task id.
	 *
	 * @return the task id
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.TASK_COMPLETE;
	}
}
