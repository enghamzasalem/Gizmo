package edu.cmu.gizmo.management.taskclient.observers;

import java.util.Observable;

import edu.cmu.gizmo.management.taskclient.TaskClient;
import edu.cmu.gizmo.management.taskclient.observers.ICapabilityObserver.CapabilityUIStatus;

/**
 * @author majed alzayer
 * This class encapsulates the instance members and instance methods
 * that are used to represent the capabilities UI data model.
 */
public abstract class AbstractCapabilityObserver
	implements ICapabilityObserver {
	
	
	/**
	 * The UI status of the capability
	 */
	private CapabilityUIStatus status;
	
	
	/**
	 * The task id of the capability
	 */
	private int taskId;
	
	
	/**
	 * The capability id of the capability
	 */
	private int capabilityId;
	
	
	/**
	 * The UI file directory of the capability. Currently,
	 * it is the name of the JSP file.
	 */
	private String capabilityUiDirectory;
	
	
	/**
	 * The task client that each capability observes.
	 */
	private TaskClient taskClient;
	
	
	/**
	 * The output that each capability produces.
	 */
	private Object output;
	
	
	/**
	 * The output of the previous capability so that it can
	 * be utilized by the current capability
	 */
	private Object defaultInput;
	
	
	/**
	 * The name of the capability
	 */
	private String capabilityName;
	
	
	/**
	 * The constructor. When any capability is instantiated,
	 * its UI status is STARTED by default.
	 */
	public AbstractCapabilityObserver() {
		status = CapabilityUIStatus.STARTED;
	}
	
	
	/**
	 * @return the task client instance member
	 */
	public TaskClient getTaskClient() {
		return taskClient;
	}

	/**
	 * @param taskClient
	 */
	public void setTaskClient(TaskClient taskClient) {
		this.taskClient = taskClient;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#getStatus()
	 */
	@Override
	public CapabilityUIStatus getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#setStatus(edu.cmu.gizmo.management
	 * .taskclient.observers.ICapabilityObserver.CapabilityUIStatus)
	 */
	@Override
	public void setStatus(CapabilityUIStatus status) {
		this.status = status;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#getCapabilityUiDirectory()
	 */
	@Override
	public String getCapabilityUiDirectory() {
		return capabilityUiDirectory;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#setCapabilityUiDirectory(java.lang.String)
	 */
	@Override
	public void setCapabilityUiDirectory(
			String capabilityUiDirectory) {
		this.capabilityUiDirectory = capabilityUiDirectory;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#getCapabilityId()
	 */
	@Override
	public int getCapabilityId() {
		return capabilityId;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#setCapabilityId(int)
	 */
	@Override
	public void setCapabilityId(int capabilityId) {
		this.capabilityId = capabilityId;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient.observers
	 * .ICapabilityObserver#observe(java.util.Observable)
	 */
	public void observe(Observable anObservalble)
	{
		this.taskClient = (TaskClient) anObservalble;
		//anObservalble.addObserver(this);
	}	
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskclient
	 * .observers.ICapabilityObserver#getOutput()
	 */
	@Override
	public Object getOutput()
	{
		return output;
	}
	
	@Override
	public void setOutput(Object output) {
		this.output = output;
	}
	
	@Override
	public int getTaskId() {
		return taskId;
	}
	
	@Override
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	@Override
	public void setDeafultInput(Object prevOutput) {
		this.defaultInput = prevOutput;
	}
	
	
	public String getCapabilityName() {
		return capabilityName;
	}

	@Override
	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	public boolean equals(Object object)
	{
		
		if (object instanceof ICapabilityObserver)
		{
			ICapabilityObserver anObserver = (ICapabilityObserver) object;
		if (anObserver.getCapabilityId() == this.getCapabilityId() 
				&& anObserver.getTaskId() == this.getTaskId()) {
			return true;
		}
			return false;
		}
		return false;
		
	}

	@Override
	public Object getDefaultInput() {
		return defaultInput;
	}
	
	

	

}
