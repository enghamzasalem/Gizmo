/*
 * ScheduleDBAccessImpl.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.dataaccess.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import edu.cmu.gizmo.management.dataaccess.ScheduleDBAccess;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor.TaskType;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTask;
import edu.cmu.gizmo.management.util.DBConnection;


/**
 * The Class ScheduleDBAccessImpl.
 */
public class ScheduleDBAccessImpl implements ScheduleDBAccess {

	/** The conn. */
	private Connection conn = null;

	private final Integer NUMBER_OF_MS_PER_HR = 3600000;
	
	/** The result set. */
	private ResultSet resultSet = null;

	/** The db name. */
	private String dbName = "tasksdb";

	public ScheduleDBAccessImpl() { 
		connectToDB();
	}

	/**
	 * Conn.
	 */
	public void connectToDB() {		
		conn = DBConnection.getConnection(dbName);
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.dataaccess.TaskCapabilityDBAccess#close()
	 */
	public void close() {
		try {
			DBConnection.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.dataaccess.ScheduleDBAccess#findEarliestTime(int)
	 */
	@Override
	public Date findEarliestTime(int druration) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.dataaccess.ScheduleDBAccess#loadScheduledTask(int)
	 */
	@Override
	public TaskReservation loadNextScheduledTask(String user) {

		try {
			
			Calendar cal = Calendar.getInstance();
			
			java.util.Date date = cal.getTime();
			Timestamp then = new Timestamp(date.getTime() + NUMBER_OF_MS_PER_HR);
			
			/* 
			 * The following query will pull the current user's next task 
			 */
			
			String queryString = 
				"SELECT *" 
				+ " FROM tasksdb.tasks_schedule, tasksdb.task_sched_params, tasksdb.task_def" 
				+ " where tasksdb.tasks_schedule.TASK_ID = tasksdb.task_sched_params.TASK_ID"
				+ " AND tasksdb.task_sched_params.TASK_ID = tasksdb.task_def.TASK_ID"
				+ " AND tasksdb.tasks_schedule.USERNAME = '" + user + "'"
				+ " AND tasksdb.tasks_schedule.task_dt =" 
				+ " (SELECT MIN(tasksdb.tasks_schedule.task_dt)"
				+ " FROM tasksdb.tasks_schedule"
				+ " WHERE tasksdb.tasks_schedule.USERNAME = 'mzayer');";

			System.out.println(queryString);
			Statement stmt = conn.createStatement();
			resultSet = stmt.executeQuery(queryString);      

			while (resultSet.next()) {
				
				// return the first returned task as a reservation
				String taskName = resultSet.getString("TASK_NAME");
				String duration = resultSet.getString("TASK_DURTN_MIN");
				String scriptName = resultSet.getString("TASK_SCRPT_NAME");

				// generate a reservation for the task to run
				TaskReservation rsvp = 
					new TaskReservation(
							taskName, 
							Integer.parseInt(duration),
							TaskRequester.TASK_CLIENT, 
							null, 
							TaskType.SCRIPT_TASK, 
							scriptName);

				return rsvp;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.dataaccess.ScheduleDBAccess#addSchedule(int, java.util.Date, int)
	 */
	@Override
	public boolean addScheduleEntry(String userName, Timestamp taskStartTime,
			Integer taskDuration, String taskName, String scriptName) {
		
		try {
			
		    CallableStatement proc =
		        conn.prepareCall("{ CALL addScheduleEntry(?,?,?,?,?,?) }");
		   
		    proc.setString(1, userName);
		    proc.setTimestamp(2, taskStartTime);
		    proc.setInt(3, taskDuration);
		    proc.setString(4, taskName);
		    proc.setString(5, scriptName);
		    
		    proc.registerOutParameter(6, Types.BOOLEAN);
		    
		    proc.execute();
		    Boolean result = proc.getBoolean(6); 
		    proc.close();
		    
		    return result;
		}
		catch (SQLException e){
		   e.printStackTrace();
		}
		
		return false;
	}

	
	@Override
	public void deleteScheduleEntry(String userName, Timestamp taskStartTime,
			Integer taskDuration, String taskName, String scriptName) {

		try {
			String query = "DELETE FROM tasksdb.tasks_schedule WHERE USERNAME = '" 
				+ userName +"'"; 
			
			conn.createStatement().executeUpdate(query);
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void setParameterForScheduledTask(String user, Timestamp startTime,
			String parameterName, String parameterValue) {
		// TODO Auto-generated method stub
		
	}
}
