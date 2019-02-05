/*
 * TaskExecutionStrategy.java 1.0 12\06\19
 */
package edu.cmu.gizmo.management.taskmanager;

import java.util.Observable;


/**
 *  This is the abstract class that different types of tasks must extend.
 *  <p>  
 *  This abstract class. The TaskExecutionStrategy is an 
 *  abstract class to allow it to be Observable. This abstract class must 
 *  be observable to all the TaskExecutor to detect status 
 *  and respond appropriately. 
 *  
 * @version 1.0 19 Jun 2012
 * @author Jeff Gennari
 */
abstract class TaskExecutionStrategy extends Observable {
	
	/**
	 * execute the task.
	 *
	 * @pre
	 * The concrete strategy has been instantiated
	 * and is ready to execute
	 * @post
	 * The concrete strategy is executing
	 * @rationale
	 * The task should know how to execute itself. This call should
	 * run in the current thread and block until execution completes.
	 * @example
	 * <pre>
	 * TaskExecutionStrategy s = new ScriptExecutionStrategy();
	 * s.execute();
	 * </pre>
	 */
	public abstract void execute();
	
	
	/**
	 * Pause the task.
	 *
	 * @return the state of the paused task
	 * @pre
	 * The task is running
	 * @post
	 * the task is paused
	 * @rationale
	 * Every task should be able to pause itself meaning it should save the
	 * state necessary to restart itself as though it was never paused. To
	 * facilitate this tasks should create a TaskState object with the
	 * information necessary to restore it to the state it was in before
	 * it was paused.
	 * @example
	 * <pre>
	 * Tasks t = exe.pause();
	 * // ...
	 * exe.resume(t);
	 * </pre>
	 */
	public abstract Object pause();
	
	/**
	 * Resume a paused task.
	 *
	 * @param state the TaskState object to use to restore state
	 * @pre
	 * The task is currently paused
	 * @post
	 * The task is resumed
	 * @rationale
	 * Every task should be able to resume itself when paused meaning it should
	 * be able to restore itself from a saved state as though it was never
	 * paused. To facilitate this tasks should use a TaskState object to restore
	 * itself  to the state it was in before it was paused.
	 * @example
	 * <pre>
	 * Tasks t = exe.pause();
	 * // ...
	 * exe.resume(t);
	 * </pre>
	 */
	public abstract void resume(Object state);
	
	
	/**
	 * Terminate the underlying task.
	 *
	 * @pre
	 * The task is currently PAUSED, RUNNING, or READY
	 * 
	 * @post
	 * The task is terminated and status is set to CANCELED
	 * 
	 * @rationale
	 * Terminating a task is an operation common to all tasks
	 * 
	 * @example
	 * <pre>
	 * exe.terminate();
	 * </pre>
	 */
	public abstract void terminte(String reason);
	
};
