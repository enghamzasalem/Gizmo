
/*
 * CapabilityInputMessage.java 1.0 2012-06-25
 */
package edu.cmu.gizmo.management.taskbus.messages;

import java.util.concurrent.ConcurrentHashMap;


/**
 * A capability input message.
 *
 * @version 1.0 2012-06-25
 * @author Jeff Gennari
 */
public class CapabilityInputMessage implements  TaskMessage {
	
	/** The unique identifier for the capability using this message. */
	private Integer capabilityId;

	/** The unique identifier for the task using this message. */
	private Integer taskId;
	
	/** the input to this capability. */
	private ConcurrentHashMap<Object, Object>input;
	
	/**
	 * Create a new CapabilityInputMessage.
	 *
	 * @param tid the task ID for this message
	 * @param cid the capability ID for this message
	 * @param in the in
	 */
	public CapabilityInputMessage(Integer tid, Integer cid, 
			ConcurrentHashMap<Object, Object> in) {
		taskId = tid;
		capabilityId = cid;
		input = in;
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
	
	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	public ConcurrentHashMap<Object, Object> getInput() {
		return input;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.CAPABILITY_INPUT;
	}	
}
