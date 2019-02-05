package edu.cmu.gizmo.management.taskclient.observers;

import java.util.Observable;

/**
 * @author majedalzayer
 *
 * This interface containts properties that are needed to generally act
 * on the capabilities observers at runtime by utilizing polymorphism. Also,
 * it is used to as a contract that the capability developer must conform to
 * in order to be able to implement a capability observer.
 */
public interface ICapabilityObserver{

	/**
	 * @author majedalzayer
	 *
	 * The UI status of a capability:
	 *  - STARTED: Once a capability observer is created.
	 *  - COMPLETED: Once a capability finishes its functionality
	 *    and no more output is sent.
	 *  - ENDED: To mark the end of the capability
	 */
	public enum CapabilityUIStatus {
		STARTED, COMPLETED, ENDED
	}
	
	/**
	 * @return the output of the capability
	 */
	public Object getOutput();
	
	/**
	 * @param output
	 */
	public void setOutput(Object output);
	
	/**
	 * @param anObservable
	 * 
	 * CURRENTLY NOT USED
	 */
	public void observe(Observable anObservable);
	
	/**
	 * @return the UI status of the capability
	 */
	public CapabilityUIStatus getStatus();
	
	/**
	 * @param status
	 */
	public void setStatus(CapabilityUIStatus status);
	
	/**
	 * @param capabilityId
	 */
	public void setCapabilityId(int capabilityId);
	
	/**
	 * @param directory
	 */
	public void setCapabilityUiDirectory(String directory);
	
	/**
	 * @return the capability id
	 */
	public int getCapabilityId();
	
	/**
	 * @return the task id
	 */
	public int getTaskId();
	
	/**
	 * @param taskId
	 */
	public void setTaskId(int taskId);
	
	/**
	 * @return the capability UI file directory. Currently,
	 * it's the JSP file name
	 */
	public String getCapabilityUiDirectory();
	
	/**
	 * @param prevOutput
	 * 
	 * Sets the output of the previous capability
	 */
	public void setDeafultInput(Object prevOutput);
	
	/**
	 * @param capabilityName
	 */
	public void setCapabilityName(String capabilityName);
	
	public Object getDefaultInput();
}
