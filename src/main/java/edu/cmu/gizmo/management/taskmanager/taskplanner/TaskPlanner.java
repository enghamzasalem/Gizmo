/*
 * TaskPlanner.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskmanager.taskplanner;

import java.util.Collection;

import edu.cmu.gizmo.management.taskmanager.TaskExecutor;

/**
 * The task planner maintains the rules used by the task manager to decide what
 * tasks to assign a robot in the event of an emergency.
 */
public class TaskPlanner {

	/** the specific evaluation strategy */
	private final TaskPlanEvaluator eval;

	/**
	 * Create a new TaskPlanner with a task evaluation strategy.
	 * 
	 * @param strategy
	 *            the strategy for evaluating a task plan
	 */
	public TaskPlanner(final TaskPlanEvaluator strategy) {

		eval = strategy;
	}

	public TaskPlan evaluate(final TaskExecutor newRequest,
			final Collection<TaskExecutor> tasks) {

		return eval.generate(newRequest, tasks);
	}

}
