/*
 * TaskPlanEvaluator.java Jul 20, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.taskplanner;

import java.util.Collection;

import edu.cmu.gizmo.management.taskmanager.TaskExecutor;

// TODO: Auto-generated Javadoc
/**
 * The Interface TaskPlanEvaluator.
 */
public interface TaskPlanEvaluator {

	/**
	 * Evaluate a new task against running tasks and generate a task plan for
	 * the task manager to execute. Evaluation of a new task occurs in the
	 * context of the currently running tasks
	 *
	 * @param newTask the new task to evaluate
	 * 
	 * @param runningTasks the running tasks
	 * 
	 * @return the task plan
	 * 
	 * @see TaskExecutor
	 */
	public TaskPlan generate(TaskExecutor newTask,
			Collection<TaskExecutor> runningTasks);
}
