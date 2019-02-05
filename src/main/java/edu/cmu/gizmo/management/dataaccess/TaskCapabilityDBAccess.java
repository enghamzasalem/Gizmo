/*
 * TaskCapabilityDBAccess.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.dataaccess;

/**
 * This class is used to fetch schedule information. TaskDAO class is used to the following:
 * 1. Compose a new task
 * 2. Find information about a task/grouped task
 * 3. Find a dependency
 * 4. Scheduled configuration information
 * 5. Create schedule entry
 * 6. find grouped task are available
 * 7. Add a capability to a grouped task
 * 8. Find task name using ID
 * 9. Find status msg for task name
 * 10.Find status value for task name
 * 11.Find action msg for task name
 * 12.Find action status for task name
 * 13.Find grouped dependson
 * 14.Find grouped id
 * 15.isParameterSet (Task Name)
 * 16.setParameter (task Name, Parameter Name, Parameter Value)
 * 17.return grouped task plan	
 * 
 * create table groupedTask
 * (
 * groupedTaskID int not null,
 * groupedTaskName varchar(30),
 * groupedTaskFlagStatus int,
 * groupedTaskFlagMsg varchar(30),
 * groupedTaskActionStatus int,
 * groupedTaskActionMsg varchar(30),
 * groupedTaskDependsOnId int,
 * groupedTaskDependsOnIdStatus int);
 * 
 * create table task 
 * (
 * taskID int not null,
 * groupedTaskID int,
 * taskName varchar(30),
 * taskDescrip varchar(30),
 * taskExecutionType int,
 * taskCompositionRestriction int,
 * taskExecutionRestriction int,
 * taskType varchar(30),
 * taskParameterID int,
 * taskParameterName varchar(30),
 * taskParameterValue varchar(30),
 * taskFlagStatus int,
 * taskFlagMsg varchar(30),
 * taskActionStatus int,
 * taskActionMsg varchar(30),
 * taskDependsOnId int,
 * taskDependsOnIdStatus int
 * );
 * create table taskExecutionTypeLookup 
 * (
 * taskExecutionTypeLookupId int,
 * taskExecutionTypeLookupName varchar(30)
 * );
 *  
 * @author sjnicklee
 *
 */

import java.awt.Desktop.Action;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import edu.cmu.gizmo.management.beans.PrimitiveCapability;
import edu.cmu.gizmo.management.beans.TaskDef;
import edu.cmu.gizmo.management.taskmanager.scripttask.DependsOn;
import edu.cmu.gizmo.management.taskmanager.scripttask.Flag;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTask;
import edu.cmu.gizmo.management.taskmanager.scripttask.Task;


/**
 * The Interface TaskCapabilityDBAccess.
 */
public interface TaskCapabilityDBAccess {

	public String retrievePrimitive(String capability);

	public Vector<String> listPrimitivesFromPrimitveCapabilityTB();

	public Vector<String> listPrimitives();
	
	/**
	 * Insert capability-primitive mapping.
	 *
	 * @param pc the pc
	 * @return the status of the operation
	 */
	public void insertPrimitiveCapability(PrimitiveCapability pc);

	
	/**
	 * Insert taskDef
	 *
	 * @param td TaskDef
	 * @return the status of the operation
	 */
	public void insertTaskDef(TaskDef td);


	public boolean findCapabilityByCapaiblityName(String capabilityName);

	/**
	 * Close.
	 */
	public void close();


	/**
	 * Retrieve capability.
	 *
	 * @param primitive the primitive
	 * @return the string
	 */
	public String retrieveCapability(String primitive);
	
	/**
	 * Compose a new task (FindJane).
	 *
	 * @param taskName name of the task getting defined
	 * @param taskList the task list
	 * @return the status of the operation
	 */
	public boolean composeTask(String taskName, ArrayList<Task> taskList);

	/**
	 * Find information about a task or grouped task.
	 *
	 * @param taskID task ID of the task searched for
	 * @return returns true if the task is found, false if not.
	 */
	public boolean findTask(int taskID);

	/**
	 * Find information about a task or grouped task.
	 *
	 * @param taskDef bean
	 * @return returns true if the task is found, false if not.
	 */
	public boolean findTaskByTaskName(String taskName);

	/**
	 * Find a dependency.
	 *
	 * @param taskID task ID of the task you want to find the dependsOn tag value
	 * @return returns a value of the dependsOn tag value
	 */
	public int findDependendsOn(int taskID);

	/**
	 * Scheduled configuration information.
	 *
	 * @param taskID task(so-called capability) ID
	 * @param taskParameterName name of the parameter for the task
	 * @param taskParameterValue value of the parameter for the task
	 */
	public void setParameter(int taskID, String taskParameterName,
			String taskParameterValue);

	/**
	 * Create schedule entry.
	 *
	 * @param groupedTaskName Name for a collection of tasks/capabilities e.g. FindJane
	 * @param flags A combination of flag status and message. e.g. Flag status=1,
	 * Flag msg=Success
	 * @param acitons the acitons
	 * @param dependsOns A combination of dependsOn id and dependsOn status. e.g.
	 * DependsOn id=1, status=1
	 * @param tasks A combination of task object that contains task id, task name
	 * and task Parameters etc.
	 * @return No return value
	 * @rationale This function will make a series of entries to the groupedTask
	 * table with task name, flags, actions and dependsOn for grouped
	 * tasks. It will also make a series of entries to the task table
	 * linking groupedTaskID with the tasks.
	 */
	public void createItinerary(String groupedTaskName, ArrayList<Flag> flags,
			ArrayList<Action> acitons, ArrayList<DependsOn> dependsOns,
			ArrayList<Task> tasks);

	/**
	 * Schedule a task.
	 *
	 * @param startTime start date and time the task
	 * @param minutes duration of the task in minutes
	 * @param groupedTaskID task ID of the groupedTaskID
	 * @return true if the operation is successful, false if the operation fails
	 * @rationale Add an entry to the schedule database of the
	 */
	public boolean scheduleTask(Date startTime, int minutes, int groupedTaskID);

	/**
	 * Find grouped task are available.
	 *
	 * @param groupedTaskName name of the groupedTask
	 * @return true if the groupedTask is available, false if the goupredTask is
	 * not available
	 */
	public boolean isGroupedTaskAvailable(String groupedTaskName);

	/**
	 * Add a capability to a grouped task First Need to confirm whether the
	 * task(so-called capability) exists or not Then, add the task to the
	 * groupedTask as the last task of the sequence of the groupedTask.
	 *
	 * @param groupedTaskId task ID of the groupedTask that the new task gets added to.
	 * @param taskId task ID of the task that needs to be added
	 * @return true, if the task is added to the groupedTask. false, if the task
	 * cannot be added to the groupedTask
	 */
	public boolean addTaskToGroupedTask(int groupedTaskId, int taskId);

	/**
	 * Add a capability to a grouped task First Need to confirm whether the
	 * task(so-called capability) exists or not Then, add the task to the
	 * groupedTask as the last task of the sequence of the groupedTask.
	 *
	 * @param groupedTaskName name of the groupedTask that the new task gets added to.
	 * @param taskName task name of the task that needs to be added
	 * @return true, if the task is added to the groupedTask. false, if the task
	 * cannot be added to the groupedTask
	 */
	public boolean addTaskToGroupedTask(String groupedTaskName, String taskName);

	/**
	 * Find task name using ID.
	 *
	 * @param taskID task ID of the task searched for
	 * @return taskName task name of the task searched for
	 */
	public String findTaskName(int taskID);

	/**
	 * Find status msg for task name.
	 *
	 * @param taskName task name of the task searched for
	 * @param status status of the task searched for
	 * @return taskMsg task message of the task for the status value
	 */
	public String findTaskMsg(String taskName, int status);

	/**
	 * Find status value for task name with a certain task msg.
	 *
	 * @param taskName task name searched for
	 * @param msg task msg of the task searched for
	 * @return flagStatus value of the flagStatus with the msg
	 */
	public int findStatusValue(String taskName, String msg);

	/**
	 * Find action msg for task name.
	 *
	 * @param taskName task name of the task searched for
	 * @param actionStatus action status value of the task searched for
	 * @return taskMsg task msg of the task searched for
	 */
	public String findTaskActionMsg(String taskName, int actionStatus);

	/**
	 * Find action status for task name.
	 *
	 * @param taskName task name of hte task searched for
	 * @param actionMsg action msg of the task searched for
	 * @return taskStatus task status of the task searched for
	 */
	public int findTaskActionStatus(String taskName, String actionMsg);

	/**
	 * Find grouped dependson.
	 *
	 * @param groupedTaskId groupedTask Id of the gropuedTask searched for
	 * @return groupedTaskId of the groupedTask that depends on
	 */
	public int findGroupedDependsOn(int groupedTaskId);

	/**
	 * Find grouped id.
	 *
	 * @param taskId task ID of the task searched for
	 * @return groupedTaskId returns the gorupedTaskId of the task
	 */
	public int findGroupedIdForTaskId(int taskId);

	/**
	 * isParameterSet (Task Name).
	 *
	 * @param taskName task name of the task searched for
	 * @param parameterName parameter name of the task searched for
	 * @return returns true if the parameter of the task is set. false, if the
	 * parameter of the task is not set.
	 */
	public boolean isParameterSet(String taskName, String parameterName);

	/**
	 * setParameter (task Name, Parameter Name, Parameter Value).
	 *
	 * @param taskName name of the task
	 * @param parameterName name of the parameter
	 * @param parameterValue value of the parameter
	 */
	public void setParameterForTask(String taskName, String parameterName,
			String parameterValue);

	/**
	 * return grouped task plan.
	 *
	 * @param groupedTaskId groupedTask Id of the groupedTask
	 * @return GroupedTask object
	 */
	public GroupedTask retrieveGroupedTask(int groupedTaskId);

}
