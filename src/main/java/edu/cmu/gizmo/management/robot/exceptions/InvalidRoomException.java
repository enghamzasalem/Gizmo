/*
 * InvalidRoomException.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.robot.exceptions;


/**
 * This is the exception class to indicate that the specified room doesn't exist.
 *
 * @version 1.0 18 Jun 2012
 * @author Jeff Gennari
 */
public class InvalidRoomException extends Exception { 
	
	/**
	 * Instantiates a new invalid room exception.
	 */
	public InvalidRoomException()
	{
		super();
	}
	
	/**
	 * Instantiates a new invalid room exception.
	 *
	 * @param message the message
	 */
	public InvalidRoomException(String message)
	{
		super(message);
	}
} 
