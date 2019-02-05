package edu.cmu.gizmo.management.taskclient.observers;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author majedalzayer
 *
 * This class is responsible of representing the UI data model of
 * the CoBot dashboard capability
 */
public class CoBot3DashboardObserver extends AbstractCapabilityObserver {

	
	/**
	 * The video output object that represents the streamed images
	 * from CoBot
	 */
	byte[] videoOutput;
	
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .AbstractCapabilityObserver#setOutput(java.lang.Object)
	 * 
	 * This method updates the videoOutput instance member.
	 * 
	 */
	@Override
	public void setOutput(Object output)
	{
		ConcurrentHashMap<Object, Object> outputMap =
			(ConcurrentHashMap<Object, Object>) output;
		videoOutput = (byte[]) outputMap.get("image");
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient
	 * .observers.AbstractCapabilityObserver#getOutput()
	 */
	@Override
	public Object getOutput() {
		return videoOutput;
	}
	
	
}
