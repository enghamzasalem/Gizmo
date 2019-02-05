/*
 * RobotTaskProxy.java Jul 19, 2012 1.0
 */
package edu.cmu.gizmo.management.robot;

import java.net.Socket;

import edu.cmu.gizmo.management.taskclient.GizmoTaskClient;
import edu.cmu.gizmo.management.taskmanager.TaskStatus;

/**
 * The robot task proxy is the interface between the tasking system 
 * and a robot.
 * 
 * @author Jeff Gennari
 * @version 1.0
 */
public interface RobotTaskProxy extends GizmoTaskClient {
	
	/**
	 * Install a new task proxy. All robot interfaces should support some way to
	 * install a task proxy.
	 * 
	 * @param taskChannel the socket connection on which to receive tasking.
	 */
	public void installTaskProxy(Socket taskChannel);
	
	/**
	 * Terminate the tsk proxy
	 */
	public void uninstallTaskProxy();
	
}
