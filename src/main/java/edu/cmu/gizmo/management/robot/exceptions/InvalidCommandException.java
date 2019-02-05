/*
 * InvalidCommandException.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.robot.exceptions;


/**
 * The Class InvalidCommandException.
 */
public class InvalidCommandException extends Exception{
	
	/**
	 * Instantiates a new invalid command exception.
	 */
	public InvalidCommandException()
	{
		super();
	}
	
	/**
	 * Instantiates a new invalid command exception.
	 *
	 * @param message the message
	 */
	public InvalidCommandException(String message)
	{
		super(message);
	}

}
