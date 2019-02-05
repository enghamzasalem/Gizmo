/*
 * TaskMessageHandler.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskbus.messages;


/**
 * The Interface TaskMessageHandler.
 */
public interface TaskMessageHandler {
	
	/**
	 * Handle message.
	 *
	 * @param message the message
	 */
	public void handleMessage(TaskMessage message);
}
