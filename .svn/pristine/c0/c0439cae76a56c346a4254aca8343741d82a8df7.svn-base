package edu.cmu.gizmo.management.taskclient.actions;

import com.opensymphony.xwork2.Action;

import edu.cmu.gizmo.management.dataaccess.jdbc.ScheduleDBAccessImpl;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskExecutor;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;

/**
 * @author majed alzayer
 * 
 * This class is a Struts {@link Action} class that is
 * responsible of retrieving a list of scheduled tasks
 * of the user so that the user can select from the
 * returned list.
 *
 */
public class TaskSelectorAction extends GizmoAction {
	
	
	TaskReservation taskReservation;
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		ScheduleDBAccessImpl scheduleDBAccess = new ScheduleDBAccessImpl();
		String user = getUserNameFromSession();
		taskReservation = scheduleDBAccess.loadNextScheduledTask(user);
		/*taskReservation = new TaskReservation("FindPerson", 
												60, 
												TaskRequester.TASK_CLIENT, 
												null, 
												TaskExecutor.TaskType.SCRIPT_TASK , 
												"FindPerson.xml");*/
		
		return SUCCESS;
		
	}
	public TaskReservation getTaskReservation() {
		return taskReservation;
	}
	public void setTaskReservation(TaskReservation taskReservation) {
		this.taskReservation = taskReservation;
	}
	
	private String getUserNameFromSession() {
		return "mzayer";
	}
	
	


}
