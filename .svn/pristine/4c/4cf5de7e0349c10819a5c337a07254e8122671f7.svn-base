/*
 * TaskHandler.java 1.0 2012-06-27
 */
package edu.cmu.gizmo.management.taskclient;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.taskmanager.TaskInputMap;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;


/**
 * Manages the capabilities assigned to a task for the client.
 * 
 * @version 1.0 2012-06-27
 * @author Jeff Gennari
 */
public class TaskHandler {

	/**
	 * The status values that a task may take.
	 */
	public static enum TaskHandlerStatus {

		/** The initial state of the task. */
		READY, 
		/** The running state. */
		RUNNING, 
		/** The completed state . */
		COMPLETE, 
		/** The error state. */
		ERROR, 
		/** The paused state. */
		PAUSED
	};

	/** the status of this task handler. */
	private TaskHandlerStatus status;
	
	/** The I/O routes */
	private Vector<TaskInputMap> routes;

	/** The reservation for this task */
	private TaskReservation rsvp;
	
	/** Task identifier. */
	private final Integer taskId;
	

	/** The capabilities associated with this task. */
	private final ConcurrentHashMap<Integer, CapabilityHandler> capabilities;

	/**
	 * Creates a new task handler with a task ID. When created the handler will
	 * be in the READY state.
	 * 
	 * @param tid
	 *            the ID of the new task
	 */
	public TaskHandler(final Integer tid, final TaskReservation res) {
		taskId = tid;
		rsvp = res;
		status = TaskHandlerStatus.READY;
		capabilities = new ConcurrentHashMap<Integer, CapabilityHandler>();
		routes = null;
	}

	/** 
	 * Get the settings for a capability. 
	 * 
	 * @param cid the ID of the target capability. 
	 * @return the settings or null if the capability is not found.
	 */
	public ConcurrentHashMap<Object, Object> getCapabilitySettings(Integer cid) {
		if (capabilities.containsKey(cid)) { 
			return capabilities.get(cid).getSettings();
		}
		return null;
	}

	
	/**
	 * Add the I/O routes for the task. This keeps track of how input should be 
	 * routed intra-task
	 *  
	 * @param ioMap
	 * 		the task I/O routes
	 */
	public void addIoRoute(Vector<TaskInputMap> ioMap) {
		routes = ioMap;
	}
	
	/**
	 * Set the status of this task hander.
	 * 
	 * @param newStatus
	 *            the new status
	 */
	public void setStatus(final TaskHandlerStatus newStatus) {
		status = newStatus;
	}

	/**
	 * Return the status of the hander.
	 * 
	 * @return the status
	 */
	public TaskHandlerStatus getStatus() {
		return status;
	}

	/**
	 * Determine if this task handler has a capability by ID.
	 *
	 * @param cid the ID of the sought after capability
	 * @return true if this handler has the capability; false otherwise.
	 */
	public Boolean hasCapability(final Integer cid) {
		return capabilities.containsKey(cid);
	}

	/**
	 * Return the name of a capability.
	 * 
	 * @param cid
	 *            the ID of the sought after capability
	 * 
	 * @return the name of the capability or <code>null</code> if it cannot be
	 *         found.
	 */
	public String getCapabilityName(final Integer cid) {
		final CapabilityHandler c = capabilities.get(cid);
		if (c != null) {
			return c.getCapabilityName();
		}
		return null;
	}

	/**
	 * Get the status of this capability (as it pertains to the TaskClient).
	 *
	 * @param cid the ID of the capability
	 * @return the target capability's status or null if the capability is not
	 * found.
	 */
	public CapabilityHandler.ClientCapabilityStatus getCapabilityStatus(
			final Integer cid) {
		final CapabilityHandler c = capabilities.get(cid);
		if (c != null) {
			return c.getStatus();
		}
		return null;
	}

	/**
	 * Get all the capabilities associated with a task.
	 *
	 * @return a vector containing all the capabilities associated with a task
	 */
	public Vector<CapabilityHandler> getAllCapabilities() {
		return new Vector<CapabilityHandler>(capabilities.values());
	}

	/**
	 * Set the status of a capability.
	 * 
	 * @param cid
	 *            the ID of the capability to update
	 * @param newStatus
	 *            the new status of the target capability
	 */
	public void setCapabilityStatus(final Integer cid,
			final CapabilityHandler.ClientCapabilityStatus newStatus) {
		final CapabilityHandler c = capabilities.get(cid);
		if (c != null) {
			c.setStatus(newStatus);
		}
	}

	/**
	 * Add a capability to a task.
	 * 
	 * @param capabilityId
	 *            the ID of the new capability.
	 * @param capabilityName
	 *            the name of the new capability.
	 * @param capabilitySettings
	 *            the capability settings.
	 * 
	 * @return true if the capability is added; false otherwise.
	 */
	public Boolean addCapabilityToTask(final Integer capabilityId,
			final String capabilityName, 
			final ConcurrentHashMap<Object,Object> capabilitySettings) {

		if (capabilities.containsKey(capabilityId)) {
			return false;
		}

		capabilities.put(capabilityId, 
				new CapabilityHandler(capabilityId, capabilityName, capabilitySettings));
		return true;
	}

	/**
	 * Return the task ID associated with this hander.
	 *
	 * @return the task ID
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * Find the input route based on capability ID 
	 * 
	 * @param toCid the target capability that 
	 * @return
	 */
	public TaskInputMap getInputToThisCapability(Integer toCid) {
		if (routes != null) {
			for (TaskInputMap route : routes) {
				if (route.getToCapabilityId().compareTo(toCid) == 0) {
					return route;
				}
			}
		}
		return null;
	}
	/**
	 * Find the output route based on capability ID 
	 * 
	 * @param fromCid the target capability that 
	 * @return
	 */
	public TaskInputMap getOutputFromCapability(Integer fromCid) {
		if (routes != null) {
			for (TaskInputMap route : routes) {
				if (route.getFromCapabilityId().compareTo(fromCid) == 0) {
					return route;
				}
			}
		}
		return null;
	}

	/**
	 * Return the name associated with this task.
	 * 
	 * @return the task name
	 */
	public String getTaskName() {
		return rsvp.getTaskName();
	}	
}