/*
 * Task.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.scripttask;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.capability.Capability;
import edu.cmu.gizmo.management.taskmanager.TaskInputMap;


/**
 * The Class Task.
 */
public class Task {
	
	/** The task id. */
	private int taskId = -1;
	
	/** The task name. --> Primitive */
	private String taskName = null;
	
	/** The task status. */
	private PlannedTaskStatus taskStatus = null;
	
	/** The flag array list. */
	private ArrayList<Flag> flagArrayList = new ArrayList<Flag>(0);
	
	/** The action array list. */
	private ArrayList<Action> actionArrayList = new ArrayList<Action>(0);
	
	/** The parameter array list. */
	private ArrayList<Parameter> parameterArrayList = new ArrayList<Parameter>(0);
	
	/** The depends on. */
	private DependsOn dependsOn = null;
	
	/** The capbility. */
	private Capability capbility = null;
	
	/** The output array list. */
	private ArrayList<Output> outputArrayList = new ArrayList<Output>(0);
	
	/** The task input map array list. */
	private ArrayList<TaskInputMap> taskInputMapArrayList = new ArrayList<TaskInputMap>(0);
	
	/** The capability constructor. */
	private Constructor capabilityConstructor = null;

	/** The configuration information */
	private ConcurrentHashMap<Object, Object> config = null;
	
	/** The capabilityName from primitive name */
	private String capabilityName = null;
	
	/**
	 * Gets the CapabilityName
	 *
	 */
	public String getCapabilityName() {
		return capabilityName;
	}

	/**
	 * Sets the CapabilityName
	 *
	 */
	public void setCapabilityName(String capabilityName) {
		this.capabilityName = capabilityName;
	}

	/**
	 * Gets the HashMap configuration object
	 *
	 */
	public ConcurrentHashMap<Object, Object> getConfig() {
		return config;
	}

	/**
	 * Sets the HashMap configuration object.
	 *
	 * @param HashMap config object is set.
	 * 
	 */
	public void setConfig(ConcurrentHashMap<Object, Object> config) {
		this.config = config;
	}

	/**
	 * Gets the capability constructor.
	 *
	 * @return the capability constructor
	 */
	public Constructor getCapabilityConstructor() {
		return capabilityConstructor;
	}
	
	/**
	 * Sets the capability constructor.
	 *
	 * @param capabilityConstructor the new capability constructor
	 */
	public void setCapabilityConstructor(Constructor capabilityConstructor) {
		this.capabilityConstructor = capabilityConstructor;
	}
	
	/**
	 * Gets the task input map array list.
	 *
	 * @return the task input map array list
	 */
	public ArrayList<TaskInputMap> getTaskInputMapArrayList() {
		return taskInputMapArrayList;
	}
	
	/**
	 * Sets the task input map array list.
	 *
	 * @param taskInputMapArrayList the new task input map array list
	 */
	public void setTaskInputMapArrayList(
			ArrayList<TaskInputMap> taskInputMapArrayList) {
		this.taskInputMapArrayList = taskInputMapArrayList;
	}

	/**
	 * Gets the output array list.
	 *
	 * @return the output array list
	 */
	public ArrayList<Output> getOutputArrayList() {
		return outputArrayList;
	}
	
	/**
	 * Sets the output array list.
	 *
	 * @param outputArrayList the new output array list
	 */
	public void setOutputArrayList(ArrayList<Output> outputArrayList) {
		this.outputArrayList = outputArrayList;
	}

	/**
	 * Gets the depends on.
	 *
	 * @return the depends on
	 */
	public DependsOn getDependsOn() {
		return dependsOn;
	}
	
	/**
	 * Sets the depends on.
	 *
	 * @param dependsOn the new depends on
	 */
	public void setDependsOn(DependsOn dependsOn) {
		this.dependsOn = dependsOn;
	}

	/**
	 * Gets the capbility.
	 *
	 * @return the capbility
	 */
	public Capability getCapbility() {
		return capbility;
	}
	
	/**
	 * Sets the capbility.
	 *
	 * @param capbility the new capbility
	 */
	public void setCapbility(Capability capbility) {
		this.capbility = capbility;
	}
	
	/**
	 * Gets the parameter array list.
	 *
	 * @return the parameter array list
	 */
	public ArrayList<Parameter> getParameterArrayList() {
		return parameterArrayList;
	}
	
	/**
	 * Sets the parameter array list.
	 *
	 * @param parameterArrayList the new parameter array list
	 */
	public void setParameterArrayList(ArrayList<Parameter> parameterArrayList) {
		this.parameterArrayList = parameterArrayList;
	}
	
	/**
	 * Gets the flag array list.
	 *
	 * @return the flag array list
	 */
	public ArrayList<Flag> getFlagArrayList() {
		return flagArrayList;
	}
	
	/**
	 * Sets the flag array list.
	 *
	 * @param flagArrayList the new flag array list
	 */
	public void setFlagArrayList(ArrayList<Flag> flagArrayList) {
		this.flagArrayList = flagArrayList;
	}
	
	/**
	 * Gets the action array list.
	 *
	 * @return the action array list
	 */
	public ArrayList<Action> getActionArrayList() {
		return actionArrayList;
	}
	
	/**
	 * Sets the action array list.
	 *
	 * @param actionArrayList the new action array list
	 */
	public void setActionArrayList(ArrayList<Action> actionArrayList) {
		this.actionArrayList = actionArrayList;
	}
	
	/**
	 * Gets the task id.
	 *
	 * @return the task id
	 */
	public int getTaskId() {
		return taskId;
	}
	
	/**
	 * Sets the task id.
	 *
	 * @param taskId the new task id
	 */
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * Gets the task name.
	 *
	 * @return the task name
	 */
	public String getTaskName() {
		return taskName;
	}
	
	/**
	 * Sets the task name.
	 *
	 * @param taskName the new task name
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**
	 * Gets the task status.
	 *
	 * @return the task status
	 */
	public PlannedTaskStatus getTaskStatus() {
		return taskStatus;
	}
	
	/**
	 * Sets the task status.
	 *
	 * @param taskStatus the new task status
	 */
	public void setTaskStatus(PlannedTaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
}
