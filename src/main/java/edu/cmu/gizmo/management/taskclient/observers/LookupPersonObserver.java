package edu.cmu.gizmo.management.taskclient.observers;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author majedalzayer
 * This class is responsible of representing the UI model of the
 * QueryGoogleCalendarCapability
 */
public class LookupPersonObserver
	extends AbstractCapabilityObserver {
	
	
	/**
	 * The name of the person that the teleoperator is looking for
	 */
	private String personName;
	
	/**
	 * The room number to which the teleopeator is going to navigate
	 */
	private String roomNumber;
	
	
	/**
	 * The avaialbility status of the person that the teleopeator is
	 * looking for
	 */
	private boolean isAvailable;
	
	
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.
	 * observers.AbstractCapabilityObserver#setOutput(java.lang.Object)
	 */
	public void setOutput(Object output) {
		
		ConcurrentHashMap<Object, Object> outputMap = 
			(ConcurrentHashMap<Object, Object>) output;
		
		System.out.println("[LookupPersonObserver] Updating the output");
		
		/*
		 * The current design suggests that the output is returned field
		 * by field. So, only update the field if it is not null
		 */
		String personNameOutput = (String) outputMap.get("person");
		if (personNameOutput != null) {
			personName = personNameOutput;
		}
		
		String roomNumberOutput = (String) outputMap.get("roomnumber");
		if (roomNumberOutput != null) {
			roomNumber = roomNumberOutput;
		}
		
		String isAvailableOutput = (String) outputMap.get("status");
		if (isAvailableOutput != null) {
			isAvailable = Boolean.parseBoolean(isAvailableOutput);
		}
		
		System.out.println("[LookupPersonObserver] Updated the output");
		System.out.println("[LookupPersonObserver] Updated the output. " 
				+  "personName: " + personName
				+ " roomNumber: " + roomNumber + " status: " + isAvailable);
	}


	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient
	 * .observers.AbstractCapabilityObserver#getOutput()
	 */
	@Override
	public Object getOutput() {
		System.out.println("[LookupPersonObserver] Trying to get the output");
		ConcurrentHashMap<String, Object> outputMap=
			new ConcurrentHashMap<String, Object>();
		/*
		 * The current design suggests that the output is returned field
		 * by field. So, only return the output if all fields are populated
		 */
		if (personName != null && roomNumber != null) {
			System.out.println("[LookupPersonObserver] " 
					+ "the output is not null. trying to get it");
			outputMap.put("personName", personName);
			outputMap.put("roomNumber", roomNumber);
			
			// Formatting the output
			if (isAvailable) {
				outputMap.put("availStatus", "available");
			} else {
				outputMap.put("availStatus", "busy");
			}
			
			System.out.println("[LookupPersonObserver] " 
					+ 	"added personName: " + personName
					+ " roomNumber: " + roomNumber + " status: "
					+ isAvailable);
			
		}
		System.out.println("[LookupPersonObserver] " 
				+ "Returning the hashmap: " + outputMap.size());
		return outputMap;
	}
	
	

}
