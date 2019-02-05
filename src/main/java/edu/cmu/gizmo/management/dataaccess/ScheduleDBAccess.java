/*
 * ScheduleDBAccess.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.dataaccess;

/**
 * This class is used to fetch schedule information. Schedule class is used 
 * to the following:
 * 
 * 1. Return pre-loaded tasks
 * 2. Find available times
 * 3. Find scheduled times
 * 4. Scheduled configuration information
 * 5. Create schedule entry
 * 6. Schedule a task
 * 
 * create table schedule
 * (
 * schedID int not null,
 * taskStartTime timestamp(14),
 * taskDuration int,	
 * groupedTaskID int not null
 * );
 *  
 * @author sjnicklee
 *
 */

import java.sql.Date;
import java.sql.Timestamp;

import edu.cmu.gizmo.management.taskmanager.TaskReservation;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTask;


/**
 * The Interface ScheduleDBAccess.
 */
public interface ScheduleDBAccess {
	
	/**
	 * Find available time Return earliest time it can be scheduled.
	 *
	 * @param druration the duration
	 * @return the earliest date the task can be scheduled
	 */
	public Date findEarliestTime(int druration);

	/**
	 * Scheduled configuration information.
	 *
	 * @param user the user ID for the task requester 
	 * @param startTIme The start time of the task
	 * @param parameterName parameter name of the task
	 * @param parameterValue parameter value of the task
	 */
	public void setParameterForScheduledTask(String user, Timestamp startTime,
			String parameterName, String parameterValue);

	/**
	 * Return the next task for a given user name.
	 *
	 * @param user the user for which to return the next task 
	 * 
	 * @return A TaskReservation for the next task or null if one does not exist
	 */
	public TaskReservation loadNextScheduledTask(String user);

	/**
	 * Create schedule entry.
	 *
	 * @param groupedTaskID groupedTask ID
	 * @param taskStartTime start time of the task
	 * @param taskDuration total minutes of the task
	 * @return return true, if the task is scheduled. return false, if the task
	 * cannot be scheduled.
	 */
	public boolean addScheduleEntry(String userName, Timestamp taskStartTime,
			Integer taskDuration, String taskName, String scriptName);

	
	/**
	 * Remove a schedule entry.
	 *
	 * @param groupedTaskID groupedTask ID
	 * @param taskStartTime start time of the task
	 * @param taskDuration total minutes of the task
	 * @return return true, if the task is scheduled. return false, if the task
	 * cannot be scheduled.
	 */
	public void deleteScheduleEntry(String userName, Timestamp taskStartTime,
			Integer taskDuration, String taskName, String scriptName);
}
