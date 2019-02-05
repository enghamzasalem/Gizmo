/*
 * InvalidCommandStatusException.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.robot.exceptions;


/**
 * The Class InvalidCommandStatusException.
 */
public class InvalidCommandStatusException extends Exception {
	
	/**
	 * Instantiates a new invalid command status exception.
	 *
	 * @param message the message
	 */
	public InvalidCommandStatusException(String message)
	{
		super(message);
	}
	
	/**
	 * Instantiates a new invalid command status exception.
	 */
	public InvalidCommandStatusException()
	{
		super();
	}

}
