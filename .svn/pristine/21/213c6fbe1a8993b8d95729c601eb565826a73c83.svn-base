/*
 * CapabilityData.java 1.0 2012-06-29
 */
package edu.cmu.gizmo.management.taskclient;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Contains client-relevant data for a capability.
 *
 * @version 1.0 2012-06-29
 * @author Jeff Gennari
 */
public class CapabilityHandler {

	/**
	 * Represents capability status from the POV of the Task Client
	 * 
	 * <ul>
	 * <li>INIT: The initial status
	 * <li>DISPLAYED: The capability's UI has been displayed
	 * <li>COMPLETE: The capability is completed
	 * <li>ERROR: The capability experienced an error.
	 */
	public static enum ClientCapabilityStatus {

		/** initial state */
		INIT, 
		/** ready to execute*/
		READY,
		/** running state. */
		RUNNING, 
		/** done executing */
		COMPLETE, 
		/** error  */
		ERROR;
	};

	/** The ID of this capability. */
	private final Integer capabilityId;

	/** The name of this capability. */
	private final String capabilityName;

	/** The status of this capability. */ 
	private ClientCapabilityStatus status;

	/** capability specific settings */
	private ConcurrentHashMap<Object, Object> settings;
	
	
	/**
	 * Create a new CapabilityData object.
	 *
	 * @param cid the ID of this capability
	 * @param cName the name of this capability
	 */
	public CapabilityHandler(final Integer cid, final String cName, 
			ConcurrentHashMap<Object, Object> stng) {
		
		capabilityId = cid;
		capabilityName = cName;
		settings = stng;
		status = ClientCapabilityStatus.INIT;
	}
	
	public ConcurrentHashMap<Object, Object> getSettings() {
		return settings;
	}


	/**
	 * Get the name of this capability.
	 *  
	 * @return the capability name
	 */
	public String getCapabilityName() {
		return capabilityName;
	}

	/**
	 * Return the capability ID.
	 * 
	 * @return the capability ID
	 */
	public Integer getCapabilityId() {
		return capabilityId;
	}

	/**
	 * Get the status of this capability.
	 *
	 * @return The status of this capability
	 */
	public ClientCapabilityStatus getStatus() {
		return status;
	}

	/**
	 * Set the status for this capability. All capabilities must set status to
	 * reflect operating state.
	 * 
	 * @param newStatus
	 *            the status of the capability
	 */
	public void setStatus(final ClientCapabilityStatus newStatus) {
		status = newStatus;
	}
}
