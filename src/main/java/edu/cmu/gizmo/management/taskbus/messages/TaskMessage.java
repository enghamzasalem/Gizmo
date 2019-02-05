/*
 * TaskMessage.java 1.0 2012-06-22
 */

package edu.cmu.gizmo.management.taskbus.messages;

import java.io.Serializable;


/** 
 * This is the super interface for all messages that traverse the task bus. 
 * It is an abstract class as it cannot be instantiated directly
 *  
 * @version 1.0 2012-06-22 
 * @author Jeff Gennari
 *
 */
public interface TaskMessage extends Serializable {
	
	/** The Constant CAPABILITY_OUTPUT. */
	public static final String CAPABILITY_OUTPUT  = "CAPABILITY_OUTPUT";
	
	/** The Constant CAPABILITY_INPUT. */
	public static final String CAPABILITY_INPUT  = "CAPABILITY_INPUT";
	
	/** The Constant CAPABILITY_STATUS. */
	public static final String CAPABILITY_STATUS  = "CAPABILITY_STATUS";
	
	/** The Constant CAPABILITY_COMPLETE. */
	public static final String CAPABILITY_COMPLETE = "CAPABILITY_COMPLETE";
	
	/** The Constant TERMINATE_CAPABILITY. */
	public static final String TERMINATE_CAPABILITY = "TERMINATE_CAPABILITY";
	
	/** The Constant START_CAPABILITY. */
	public static final String START_CAPABILITY = "START_CAPABILITY";
	
	/** The Constant HELO_CLIENT. */
	public static final String HELO_CLIENT  = "HELO_CLIENT";
	
	/** The Constant LOAD_TASK. */
	public static final String LOAD_TASK  = "LOAD_TASK";
	
	/** The Constant TASK_READY. */
	public static final String TASK_READY  = "TASK_READY";
	
	/** The Constant PAUSE_TASK. */
	public static final String PAUSE_TASK = "PAUSE_TASK";
	
	/** The Constant PAUSE_TASK_COMPLETE. */
	public static final String PAUSE_TASK_COMPLETE = "TASK_PAUSED";
	
	/** The Constant PAUSE_LIST. */
	public static final String PAUSE_LIST = "PAUSE_LIST";
	
	/** The Constant RESUME_TASK. */
	public static final String RESUME_TASK = "RESUME_TASK";
	
	/** The Constant RESUME_TASK_COMPLETE. */
	public static final String RESUME_TASK_COMPLETE = "TASK_RESUMED";
	
	/** The Constant CANCEL_TASK. */
	public static final String CANCEL_TASK = "CANCEL_TASK";
	
	/** The Constant CLIENT_REPLAN. */
	public static final String CLIENT_REPLAN = "CLIENT_REPLAN";
	
	/** The Constant SYSTEM_REPLAN. */
	public static final String SYSTEM_REPLAN = "SYSTEM_REPLAN";
	
	/** The Constant TASK_COMPLETE. */
	public static final String TASK_COMPLETE = "TASK_COMPLETE";
	
	/** The Constant START_CAPABILITY. */
	public static final String REJECT_TASK = "REJECT_TASK";
	
	/**
	 * Gets the message type.
	 *
	 * @return the message type
	 */
	public String getMessageType();
	
}
