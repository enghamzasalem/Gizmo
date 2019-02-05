/*
 *  CapabilityCompleteMessage.java 1.0 2012-07-01.s
 */

package edu.cmu.gizmo.management.taskbus.messages;


/**
 * Message sent when capability completes successfully.
 *
 * @version 1.0 2012-07-01
 * @author Jeff Gennari
 */
public class CapabilityCompleteMessage implements TaskMessage {

	/** the task ID. */
	private Integer taskId;
	
	/** the capability ID. */
	private Integer capabilityId;
	
	/**
	 * Instantiates a new capability complete message.
	 *
	 * @param tid the tid
	 * @param cid the cid
	 */
	public CapabilityCompleteMessage(Integer tid, Integer cid)  {
		taskId = tid;
		capabilityId = cid;
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
	 * Gets the capability id.
	 *
	 * @return the capability id
	 */
	public Integer getCapabilityId() {
		return capabilityId;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.CAPABILITY_COMPLETE;
	}
}
