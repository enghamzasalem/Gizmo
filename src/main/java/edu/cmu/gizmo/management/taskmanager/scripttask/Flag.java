/*
 * Flag.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.scripttask;


/**
 * The Class Flag.
 */
public class Flag {
	
	/** The status. */
	private int status = -1;
	
	/** The msg. */
	private String msg = null;
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Gets the msg.
	 *
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * Sets the msg.
	 *
	 * @param msg the new msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
