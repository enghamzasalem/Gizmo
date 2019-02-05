/*
 * CapabilityLoader.java 1.0 2012-07-09 
 */
package edu.cmu.gizmo.management.taskmanager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.capability.Capability;
import edu.cmu.gizmo.management.robot.Robot;
import edu.cmu.gizmo.management.taskmanager.scripttask.Task;


/**
 * This class is used to load a capability object from a specified directory.
 * 
 * @version 1.0 2012-07-09
 * @author Jeff Gennari
 */
public class TaskCapabilityLoader  {
	
	/** The directory from which to load capabilities. */
	private String capabilityDirectory;
	
	/** The capability loader. */
	private ClassLoader capabilityLoader;

	/** The properties of capability **/
	private Properties capabilityManifest;
	
	/** The configuration hashmap **/
	private ConcurrentHashMap<Object, Object> config;
	
	/** The robot **/
	private Robot robot;
	
	/**
	 * Create a new capability loader for a given directory.
	 * 
	 * @param dir the directory to load capabilities from
	 */

	/**
	 * Given a capability name create an object of that capability. 
	 * 
	 * @param directory where all capabilities are installed 
	 * @return the loaded capability
	 */

	/**
	 * Empty Constructor
	 */
	public TaskCapabilityLoader() {
	}

	/**
	 * Constructor w/ Robot
	 * 
	 * @param robot reference to a robot
	 */
	public TaskCapabilityLoader(Robot robot) {
		this.robot = robot;	
	}

	/**
	 * Loads all of the parameters needed for the capability object
	 * e.g. robot
	 * @param config
	 */
	public void loadConfiguration(ConcurrentHashMap<Object, Object> config) {
		System.out.println("[TaskCapabilityLoader] loadConfiguration: In loadConfiguration Before config");
		this.config = config;
		System.out.println("[TaskCapabilityLoader] In loadConfiguration");
		if (this.config.get("robot.exist").equals("true")) {
			System.out.println("[TaskCapabilityLoader]robot.exit is true");
			
			// load the Robot object to the hashmap
			this.config.put("robot.object", robot);
		}
	}

	/** 
	 * Creates constructor of a task 
	 * 
	 * @param task references to a task bean
	 * @return constructor of a task
	 */
	public Constructor createConstructor(Task task) {
		ConcurrentHashMap<Object, Object> config = task.getConfig();
		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
		Constructor constructor = null;


		String classNameToBeLoaded = config.get("capability.directory") + "." + task.getCapabilityName();
		try {
			System.out.println("[TaskCapabilityLoader] Before loadConfiguration... " + task.getCapabilityName());
			loadConfiguration(config);
			System.out.println("[TaskCapabilityLoader] After loadConfiguration... " + task.getCapabilityName());
			Class myClass = myClassLoader.loadClass(classNameToBeLoaded);

			constructor = myClass.getConstructor();

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return constructor;		
	}

	/**
	 * Instantiates the capability using the task reference
	 * @param task reference to a task
	 * @return capability object instantiated
	 */
	public Capability instantiateCapability(Task task) {
		Constructor constructor = task.getCapabilityConstructor();
		Capability capability = null;
		try {

			capability = (Capability) constructor.newInstance();
			task.setCapbility(capability);
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(InstantiationException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		}
		return capability;
	}


	/**
	 * Returns Config Hashmap
	 * 
	 * @return configuration hashmap
	 */
	public ConcurrentHashMap<Object, Object> getConfig() {
		return this.config;
	}
}
