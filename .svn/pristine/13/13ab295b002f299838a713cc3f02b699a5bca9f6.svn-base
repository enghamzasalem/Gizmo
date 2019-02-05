/*
 * TaskReservation.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskType;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;


/**
 * The class TaskReservation. A task reservation contains the information needed
 * to start a task. Reservations are the request objects used to initiate and 
 * monitor a  task 
 * 
 * @version 1.0
 * @author Jeff Gennari
 */
public class TaskReservation 
implements Serializable, Comparable<TaskReservation> {
	
	/** The task plan. */
	private String taskPlan;
	
	/** The task duration. */
	private Integer taskDuration;
	
	/** The entity that requested the new task. */
	private TaskRequester taskRequester;
	
	/** 
	 * Task-specific information such as the reason for scheduling a task and 
	 * pre-loaded parameters
	 */
	private ConcurrentHashMap<Object,Object>taskConfig;
	
	/** The task execution type - how the task will be run. */
	private TaskType taskExeType;
	
	/** The name of the new task. */
	private String taskName;
	
	/**
	 * Instantiates a new task reservation.
	 *
	 * @param name the name of the task associated with the reservation
	 * @param duration the duration of the task in minutes
	 * @param requestor the task requester 
	 * @param config the configuration for the task
	 * @param type the type of task execution
	 * @param plan the task plan/capability 
	 */
	public TaskReservation(String name, 
			Integer duration, 
			TaskRequester requester,
			ConcurrentHashMap<Object,Object> config,
			TaskType type,
			String plan) { 
		
		taskName = name;
		taskDuration = duration;
		taskRequester = requester;
		taskConfig = config;
		taskExeType = type;
		taskPlan = plan;
		
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
	 * Gets the task duration.
	 *
	 * @return the task duration
	 */
	public Integer getTaskDuration() {
		return taskDuration;
	}
	
	/**
	 * Gets the task requestor.
	 *
	 * @return the task requestor
	 */
	public TaskRequester getTaskRequester() {
		return taskRequester;
	}
	
	/**
	 * Gets the task config.
	 *
	 * @return the task config
	 */
	public ConcurrentHashMap<Object, Object> getTaskConfig() {
		return taskConfig;
	}

	/**
	 * Gets the task exe type.
	 *
	 * @return the task exe type
	 */
	public TaskType getTaskExeType() {
		return taskExeType;
	}
	
	/**
	 * Gets the task plan.
	 *
	 * @return the task plan
	 */
	public String getTaskPlan() { 
		return taskPlan;
	}

	/**
	 * Compare two reservations based on their constituent parts.
	 * 
	 * @param other the other TaskReservation to compare against.
	 */
	@Override
	public int compareTo(TaskReservation other) {
		if (other.taskName.equals(this.taskName)) {
			if (other.taskRequester == this.taskRequester) {
				if (other.taskDuration.compareTo(this.taskDuration) == 0) { 
					if (other.taskExeType == this.taskExeType) {
						if (other.taskPlan.equals(this.taskPlan)) {
							return 0;
						}
					}
				}
			}
		}
		return 1;
	}
}