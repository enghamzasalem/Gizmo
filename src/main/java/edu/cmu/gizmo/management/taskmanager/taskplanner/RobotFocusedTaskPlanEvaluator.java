/*
 * RobotBiasedTaskEvaluator.java Jul 20, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.taskplanner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.taskmanager.TaskExecutor;
import edu.cmu.gizmo.management.taskmanager.TaskManager.TaskRequester;
import edu.cmu.gizmo.management.taskmanager.TaskReservation;

/**
 * Manage conflicts between tasks. The rules are simple:
 *
 * Robot-requested tasks take precedence. In the event of two robot-requested
 * tasks, the latest one wins. User tasks will be terminated when a robot
 * requested task is created.
 */
public class RobotFocusedTaskPlanEvaluator implements TaskPlanEvaluator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.taskmanager.taskplanner.TaskEvaluator#generate
	 * (edu.cmu.gizmo.management.taskmanager.TaskExecutor, java.util.Collection)
	 */
	@Override
	public TaskPlan generate(final TaskExecutor newTask,
			final Collection<TaskExecutor> tasks) {

		final TaskReservation rsvp = newTask.getReservation();
		final TaskPlan newPlan = new TaskPlan();

		/* 
		 * the new task is from the robot client. Determine if the requested 
		 * task can operate in the presence of other tasks. 
		 */
		if (rsvp.getTaskRequester() == TaskRequester.ROBOT_CLIENT) {

			// the robot takes precedent, so kill the task client's tasks
			for (final TaskExecutor exe : tasks) {

				if (exe.getReservation().getTaskRequester() != TaskRequester.ROBOT_CLIENT) {

					System.out.println("[RobotFocusedTaskPlanEvaluator] Killing: " 
							+ exe.getReservation().getTaskName() );

					newPlan.addTaskToKill(exe);

					ConcurrentHashMap<Object, Object> config = rsvp.getTaskConfig();

					// for those tasks that are about to die, provide a reason
					if (config.containsKey("reason")) {
						newPlan.addKillReason(
								(String) config.get("reason"));

						System.out.println("[RobotFocusedTaskPlanEvaluator] reason to die: " 
								+ config.get("reason") );

					}
				}
			}
			
		}
		
		/*
		 * Evaluate new user requests - these will be accepted if 
		 */
		else if (rsvp.getTaskRequester() == TaskRequester.TASK_CLIENT) {
			for (final TaskExecutor exe : tasks) {
				if (exe.getReservation().getTaskRequester() == TaskRequester.ROBOT_CLIENT) {
					
					System.out.println("[RobotFocusedTaskPlanEvaluator] rejected task request");
					
					// the robot is executing a task meaning the user 
					// task cannot be accepted (i.e. added to the birth list).
					return newPlan;
				}
			}
		}
		
		// add the task to start
		newPlan.addTaskToStart(newTask);
		return newPlan;
	}
}
