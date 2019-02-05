/*
 * LogicTaskStrategy.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager;

import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.capability.Capability;
import edu.cmu.gizmo.management.capability.Cobot3DashboardCapability;
import edu.cmu.gizmo.management.capability.PausableCapability;
import edu.cmu.gizmo.management.capability.Capability.CapabilityStatus;
import edu.cmu.gizmo.management.robot.Robot;
import edu.cmu.gizmo.management.taskmanager.exceptions.CapabilityNotFoundException;
import edu.cmu.gizmo.management.taskmanager.exceptions.CapabilityNotFoundForPrimitive;
import edu.cmu.gizmo.management.taskmanager.scripttask.Task;
import edu.cmu.gizmo.management.util.ManifestReader;


/**
 * The Class LogicTaskStrategy.
 */
public class LogicTaskStrategy extends TaskExecutionStrategy {
	
	/** The status. */
	private TaskStatus status;
	
	/** The cobot. */
	private Robot cobot = null;
	
	/** The capability. */
	private Capability capability= null;
	
	/** The task resolver used to look up the new task. */
	private TaskResolver resolver = null;

	private Task task; 
	
	/**
	 * Constructor with all of the parameters.
	 *
	 * @param robot the robot
	 * @param tid the tid
	 * @param cap the cap
	 * @throws CapabilityNotFoundException the capability not found exception
	 */
	public LogicTaskStrategy(Robot robot, 
			Integer tid, String cap) throws CapabilityNotFoundException {

		resolver = new TaskResolver();
		task = new Task();
		try {
			
			task.setCapabilityName(resolver.retrieveCapablityName(cap)); 
			
			
		} catch (CapabilityNotFoundForPrimitive e) {
			
			throw new CapabilityNotFoundException();
		}
		
		task.setTaskId(tid);
		cobot = robot;
	}
	
	/**
	 * Read the capability manifest and use it to sett neededd values
	 */
	private void loadCapability() { 
		TaskCapabilityLoader loader = new TaskCapabilityLoader(cobot);
		
		// configure the new task from the manifest reader
		ManifestReader manifest = new ManifestReader();
		
		ConcurrentHashMap<Object,Object> config = 
			manifest.readCapabilityManifest(task.getCapabilityName());
		
		task.setConfig(config); 

		task.setCapabilityConstructor(loader.createConstructor(task));
		capability = loader.instantiateCapability(task);

	}

	/**
	 * Run the capability by setting it to load and executing it. When this 
	 * method returns, the capability has been run to completion, cancellation,
	 * or error.
	 */
	private void runCapability() { 
		capability.load(task.getTaskId(), 0, task.getConfig());  
		capability.execute();
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskmanager.TaskExecutionStrategy#execute()
	 */
	@Override
	public void execute() {	
		
		// tell the manager that the task is ready
		TaskStatus status = new TaskStatus(task.getTaskId(),
				TaskStatus.TaskStatusValue.READY, "READY");
		
		setChanged();
		notifyObservers(status);
				
		/*
		 * load the capability so that is is ready to run 
		 */
		
		loadCapability();
		
		/*
		 * run the capability
		 */
		
		runCapability();
		
		/*
		 * Mark the capability as completed 
		 */
		if (capability.getStatus() == CapabilityStatus.COMPLETE) {
			status = new TaskStatus(task.getTaskId(),
					TaskStatus.TaskStatusValue.COMPLETE, "COMPLETE");
			setChanged();
			notifyObservers(status);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskmanager.TaskExecutionStrategy#pause()
	 */
	@Override
	public Object pause() {
		PausableCapability pause = (PausableCapability) capability;
		return pause.pause();
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskmanager.TaskExecutionStrategy#resume(java.lang.Object)
	 */
	@Override
	public void resume(Object state) {
		PausableCapability resume = (PausableCapability) capability;
		resume.resume(state);
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.taskmanager.TaskExecutionStrategy#terminte()
	 */
	@Override
	public void terminte(final String reason) {
		capability.terminate();
		
		TaskStatus status = new TaskStatus(task.getTaskId(),
				TaskStatus.TaskStatusValue.CANCELED, reason);
		
		setChanged();
		notifyObservers(status);
		
	}

}
