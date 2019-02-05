/*
 * Cobot3CommandStatus.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.robot;


/**
 * This class is used to fetch command status and possible error messages.
 * Command status is a separate class to allow for better and more sophisticated
 * error messages.
 * 
 * @version 1.0
 * @author Jeff Gennari
 * 
 */
public class Cobot3CommandStatus {

	/**
	 * The Enum CommandStatus.
	 */
	public static enum CommandStatus {
		
		/** The command is running. */
		RUNNING,
		
		/** The command terminated in error (extended error message in <code>statusMessage</code>. */
		ERROR,
		
		/** The command completed successfully. */
		COMPLETE,
		
		/** The command is waiting for input (extended information in <code>statusMessage</code>. */
		WAITING,
		
		/** The command is canceled (extended information in <code>statusMessage</code>. */
		CANCELED
		
	}

	/** The command number. */
	private Integer commandNumber;
	
	/** The status. */
	private CommandStatus status;
	
	/** The status message. */
	private String statusMessage;

	/**
	 * get the unique number for this command.
	 *
	 * @return the unique command number
	 */
	public Integer getCommandNumber() {
		return commandNumber;
	}

	/**
	 * set the command number for this command status (i.e. this links command
	 * status to command object)
	 *
	 * @param commandNumber the new command number
	 */
	public void setCommandNumber(final Integer commandNumber) {
		this.commandNumber = commandNumber;
	}

	/**
	 * Get the status flag.
	 *
	 * @return return the status flag
	 */
	public CommandStatus getStatus() {
		return status;
	}

	/**
	 * set the command status.
	 *
	 * @param status the status of the command
	 */
	public void setStatus(final CommandStatus status) {
		this.status = status;
	}

	/**
	 * Get the status message associated with this command status.
	 *
	 * @return the status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Sets the status message.
	 *
	 * @param statusMessage the new status message
	 */
	public void setStatusMessage(final String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
