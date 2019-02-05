/*
 * HeloCapabilityMessage.java 1.0 2012-07-06
 */
package edu.cmu.gizmo.management.taskbus.messages;

import java.util.concurrent.ConcurrentHashMap;


/**
 * The message sent when a capability is loaded.
 * 
 * @version 1.0 2012-07-06
 * @author Jeff Gennari
 */
public class HeloClientMessage implements TaskMessage {
	
	/** The task id. */
	private Integer taskId;
	
	/** The capability id. */
	private Integer capabilityId;
	
	/** The capability name. */
	private String capabilityName; 
	
	/** capability specific settings */
	private ConcurrentHashMap<Object, Object> settings;
	
	/**
	 * Instantiates a new helo client message.
	 *
	 * @param tid the tid
	 * @param cid the cid
	 * @param cName the c name
	 * @param settings capability settings
	 * @param ConcurrentHashMap the concurrent hash map
	 */
	public HeloClientMessage(Integer tid, Integer cid, 
			String cName, ConcurrentHashMap<Object, Object> stng)  {
		
		taskId = tid;
		capabilityId = cid;
		capabilityName = cName;
		settings = stng;
	}
	
	/**
	 * Get the capability settings
	 * 
	 * @return the settings 
	 */
	public ConcurrentHashMap<Object, Object> getCapabilitySettings() {
		return settings;
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
	 * Gets the capability name.
	 *
	 * @return the capability name
	 */
	public String getCapabilityName() {
		return capabilityName;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskbus.messages.TaskMessage#getMessageType()
	 */
	@Override
	public String getMessageType() {
		return TaskMessage.HELO_CLIENT;
	}
}

