package edu.cmu.gizmo.management.taskclient.observers;

import java.util.concurrent.ConcurrentHashMap;

import com.opensymphony.xwork2.util.location.LocationUtils;


/**
 * @author majedalzayer
 * 
 * This class is responsible of representing the UI model of the
 * GoToRoomDriveCapability.
 *
 */
public class GoToRoomObserver extends AbstractCapabilityObserver {
	
	
	/**
	 * The location to which CoBot will navigate
	 */
	private String location;
	
	
	/**
	 * The status of the location navigation
	 */
	private String navStatus;	
	

	/**
	 * @return the location instance member
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}	
	
	@Override
	public void setOutput(Object output)
	{
		ConcurrentHashMap<Object, Object> outputMap = 
			(ConcurrentHashMap<Object, Object>) output;
		
		navStatus = (String) outputMap.get("status");
	}
	
	

	@Override
	public void setDeafultInput(Object defaultInput) {
		if (defaultInput != null) {
			
			ConcurrentHashMap<Object, Object> locationHashMap = 
					(ConcurrentHashMap<Object, Object>) defaultInput;
			
			if (locationHashMap.containsKey("room")) {
				this.location = (String) locationHashMap.get("room");
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient
	 * .observers.AbstractCapabilityObserver#getOutput()
	 */
	@Override
	public Object getOutput() {
		if (location != null && navStatus != null) {
			ConcurrentHashMap<String, String> outputHashMap
			= new ConcurrentHashMap<String, String>();
			
			outputHashMap.put("roomNumber", location);
			outputHashMap.put("navStatus", navStatus);
			return outputHashMap;
		}
		
		return super.getOutput();
	}

	@Override
	public Object getDefaultInput() {
		if (location != null && !location.isEmpty()) {
			ConcurrentHashMap<String, String> defaultInputMap
			= new ConcurrentHashMap<String, String>();
		
			defaultInputMap.put("room", location);
			return defaultInputMap;
		}
		return super.getDefaultInput();
		
	}
	
	
	
	
	
	

}
