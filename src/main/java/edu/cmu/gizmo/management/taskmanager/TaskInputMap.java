/*
 * TaskInputMap.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The Class TaskInputMap.
 */
public class TaskInputMap implements Serializable{
	
	/** The from id. */
	private Integer fromId;
	
	/** The to id. */
	private Integer toId;
	
	/** The route. */
	private ConcurrentHashMap<String,String> route;

	/**
	 * Instantiates a new task input map.
	 */
	public TaskInputMap() {
		fromId = null;
		toId = null;
		route = new ConcurrentHashMap<String,String>();
	}
	
	/**
	 * Gets the input capability id.
	 *
	 * @return the input capability id
	 */
	public Integer getFromCapabilityId() {
		return fromId;
	}

	/**
	 * Sets the input capability id.
	 *
	 * @param inputCapabilityId the new input capability id
	 */
	public void setFromCapabilityId(Integer inputCapabilityId) {
		this.fromId = inputCapabilityId;
	}

	/**
	 * Gets the output capability id.
	 *
	 * @return the output capability id
	 */
	public Integer getToCapabilityId() {
		return toId;
	}

	/**
	 * Sets the output capability id.
	 *
	 * @param outputCapabilityId the new output capability id
	 */
	public void setToCapabilityId(Integer outputCapabilityId) {
		this.toId = outputCapabilityId;
	}

	/**
	 * Gets the route.
	 *
	 * @return the route
	 */
	public ConcurrentHashMap<String, String> getRoute() {
		return route;
	}

	/**
	 * Adds the route.
	 *
	 * @param outputFrom the output from
	 * @param inputTo the input to
	 */
	public void addRoute(String outputFrom, String inputTo) {
		route.put(outputFrom,inputTo);
	}

	public String toString() { 
		
		String r = "";
		if (route != null) {
			final Iterator<Entry<String, String>> entries = 
				route.entrySet().iterator();

			// send each input parameter to the extending capability
			while (entries.hasNext()) {
				final Entry<String, String> entry = 
					entries.next();

				r += entry.getKey() +":"+ entry.getValue() +" ";
			}
		}
		
		return fromId.toString() + " -> " + toId.toString() + " = " + r;
	}
}
