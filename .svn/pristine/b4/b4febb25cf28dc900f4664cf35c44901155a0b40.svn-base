package edu.cmu.gizmo.management.taskorchestrator;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.beans.CapabilityBean;
import edu.cmu.gizmo.management.beans.TaskDef;
import edu.cmu.gizmo.management.dataaccess.TaskCapabilityDBAccess;
import edu.cmu.gizmo.management.dataaccess.jdbc.TaskCapabilityDBAccessImpl;
import edu.cmu.gizmo.management.util.ManifestReader;

/**
 * PreReqChecker class checks that all of the following pre-requisites exist:
 * 1. Check script name does not exist in DB
 * 2. Check script does not exist in file system
 * 3. Check capability exist in the file system
 * 
 * @see TaskScriptOrchestrator
 */
public class TaskOrchestrationPreReqChecker {
	/**
	 * Checks that the capabiltiy-primitive information exists in DB for the taskDef specified
	 * @param taskDef
	 * @return true if the capability-primitive mapping information exists in DB
	 */
	public boolean verifyExistenceOfScriptTaskNameInDB(TaskDef taskDef) {
		TaskCapabilityDBAccess tcd = new TaskCapabilityDBAccessImpl();
		return tcd.findTaskByTaskName(taskDef.getTaskName());
	}

	/**
	 * Checks that the task script exists
	 * @param taskDef task javabean of a task
	 * @return true if the task script exists
	 */
	public boolean verifyExistenceOfScriptTaskNameInFileSystem(TaskDef taskDef) {
		String scriptName = taskDef.getTaskScriptName();
		boolean exists = new File(scriptName).exists();		
		return exists;
		
	}
	
	/**
	 * Checks that the capability-primitive information exists in DB for the capabilityBean specified
	 * @param capabilityBean
	 * @return true if the capability-primitive mapping information exists in DB
	 */
	public boolean verifyExistenceOfCapabilityInDB(CapabilityBean capabilityBean) {
		TaskCapabilityDBAccess tcd = new TaskCapabilityDBAccessImpl();
		return tcd.findCapabilityByCapaiblityName(capabilityBean.getCp_name());
		
	}
	
	/**
	 * Checks that the manifest file for the capability exists
	 * 
	 * @param the capabilityBean the capability bean object
	 * @return true if the manifest file exists
	 */
	public boolean verifyExistenceOfCapabilityInFileSystem(CapabilityBean capabilityBean) {
		ManifestReader mfReader = new ManifestReader();
		mfReader.readCapabilityManifest(capabilityBean.getCp_name());
		ConcurrentHashMap config = mfReader.readConfig();
		
		String capabilityName = capabilityBean.getCp_name();
		String fullCapabilityName = config.get("capability.directory") +"/" + capabilityName + ".class";
		
		System.out.println("fullCapabilityName: " + fullCapabilityName);
		boolean exists = new File(fullCapabilityName).exists();
		return exists;
	}
		
}
