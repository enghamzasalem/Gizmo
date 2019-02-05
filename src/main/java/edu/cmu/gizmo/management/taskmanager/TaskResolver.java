/*
 * TaskResolver.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager;

import java.util.concurrent.Executors;

import edu.cmu.gizmo.management.dataaccess.TaskCapabilityDBAccess;
import edu.cmu.gizmo.management.dataaccess.jdbc.TaskCapabilityDBAccessImpl;
import edu.cmu.gizmo.management.robot.Robot;
import edu.cmu.gizmo.management.taskmanager.exceptions.CapabilityNotFoundForPrimitive;

/**
 *  The TaskResolver uses this class to retrieve the mappings from task 
 *  primitives to capabilities.
 *  This task maps task steps to specific capabilities by querying the Tasks 
 *  and Capabilities database for a task primitive to capability mapping 
 *  using the TaskAndCapabilityDBAccess class.
 *  This class provides a list of capabilities for a ScriptTaskStrategy to 
 *  load.
 *  This class manages time on task
 *  Currently, this class will simply fetch the task-to-capability mapping from 
 *  the database.
 * */
public class TaskResolver {

	/** The dbo. */
	private TaskCapabilityDBAccess dbo;
	
	/**
	 * Constructor
	 * 1. Set up necessary variables needed for Capability load such as robot
	 * Other variables needed for Capability will be passed when load is
	 * called
	 * 2. Set up dbo - Establish DB connection
	 *
	 * @param robot the robot
	 */
	public TaskResolver() {
		// Setup dbo
		this.dbo = new TaskCapabilityDBAccessImpl();
		Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Refer to "Task Orchestration Package" in architecture document
	 * Queries DB for which capability to load for
	 * CheckAvailability		The capability used to query a calendar
	 * FindEvent		The capability used to query for an events location
	 * FindPerson		The capability used to query for an persons location
	 * GoToRoom		The capability used to move the robot to a room
	 * SeekHelp		The capability used to search for help
	 * SendEmail		The capability used to send an email
	 * CreateItinerary		The capability used to create an itinerary
	 * Communicate		The capability used to enable interaction with the robot
	 * MoveRobot		The capability used to incrementally move the robot
	 * *************************************************************************
	 * 1. Check Conn - dbo will take care of checking connection
	 * 2. Run a query using primitive - Call dbo.retrieveCapability
	 * 3. If not found
	 * 3a. throw an exception this will be caught in ScriptTaskStrategy.
	 * ScriptTaskStrategy will catch and handle the exception by
	 * notifying the TaskManager with Observer-Observable pattern
	 * 3b. If found
	 * Dynamically load the Capability and return the capability
	 * 4. If not loaded e.g. XXXCapability class not implemented yet
	 * 4a. throw an exception to ScriptTaskStrategy. Again ScriptTaskStrategy
	 * will notify the TaskManager with Observer-Observable pattern
	 * 5. Return Capability
	 *
	 * @param primitive the primitive
	 * @return capabilityName
	 */
	
	public String retrieveCapablityName(String primitive) 
			throws CapabilityNotFoundForPrimitive {
		// 1. Retrieve capability name from database using primitive
		String capabilityName = dbo.retrieveCapability(primitive);
		if(capabilityName == null)
			throw new CapabilityNotFoundForPrimitive();
			
		return capabilityName;
	}	
}
