/*
 * CapabilityOutputMessage.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskbus.messages;

import java.util.concurrent.ConcurrentHashMap;



/**
 * A capability output message.
 *
 * @version 1.0 2012-06-25
 * @author Jeff Gennari
 */
public class CapabilityOutputMessage implements TaskMessage {
	
	/** The unique identifier for the capability using this message. */
	private Integer capabilityId;

	/** The unique identifier for the task using this message. */
	private Integer taskId;
	
	/** The output for this capability. */
	private ConcurrentHashMap<Object, Object>output;
	
	/**
	 * Create a new CapabilityOutputMessage.
	 *
	 * @param tid the task identifier
	 * @param cid the capability identifier
	 * @param out the out
	 */
	public CapabilityOutputMessage(Integer tid, Integer cid, 
			ConcurrentHashMap<Object, Object>out) {
		taskId = tid;
		capabilityId = cid;
		output = out;
	}
	
	/**
	 * Return the capability identifier.
	 *
	 * @return the capabilityId
	 */
	public Integer getCapabilityId() {
		return capabilityId;
	}
	
	/**
	 * Get the output of this capability.
	 *
	 * @return the output as a <code>ConcurrentHashMap</code>
	 */
	public ConcurrentHashMap<Object,Object> getOutput() {
		return output;
	}
	
	/**
	 * Return the task identifier.
	 *
	 * @return the taskId
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.CAPABILITY_OUTPUT;
	}
}