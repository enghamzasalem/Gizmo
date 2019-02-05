
/* 
 * RobotFactory.java 1.0 2012-06-20
 */
package edu.cmu.gizmo.management.robot;

import java.net.ServerSocket;
import java.net.Socket;

import edu.cmu.gizmo.management.taskclient.Cobot3TaskClient;


/**
 * Get a connected reference to a new robot.
 *
 * @author
 */
public class RobotFactory {
	
	public static enum RobotModel {
		COBOT3 /* Good ole' Cobot 3 */
	};
	
	/** The robot. */
	private static Robot robot=null;
	
	private static RobotTaskProxy proxy=null;
	
	/** The Constant CONTROL_PORT. */
	private final static Integer ROBOT_CONTROL_PORT = 4242;
	
	/** The Constant MANAGER_PORT. */
	private final static Integer ROBOT_PROXY_PORT = 4244;
	
	/**
	 * New robot.
	 *
	 * @param type the type
	 * @return the robot
	 */
	public static Robot newRobot(RobotModel type) {
		if (robot == null) {
			if (type == RobotModel.COBOT3) {
				return setupRobotConnection();
			}
		}
		return robot;
	}
	
	public static RobotTaskProxy newTaskProxy(RobotModel type) {
		if (proxy == null) {
			if (type == RobotModel.COBOT3) {
				return setupRobotProxy();
			}
		}
		return proxy;
		
	}
	
	private static RobotTaskProxy setupRobotProxy() {
		while (true)  {
			try {
				
				proxy = new Cobot3TaskClient();
				
				System.out.print("[RobotFactory] Waiting for proxy to connect ... ");
				Socket s = (new ServerSocket(ROBOT_PROXY_PORT,10)).accept();
				System.out.println("Connected");	
				
				proxy.installTaskProxy(s);
				
				return proxy;
				
			} catch (Exception e) { 
				e.printStackTrace();
			}			
		}
	}

	/**
	 * Make the connection to the robot manager.
	 *
	 * @return a connected Robot
	 */
	private static Robot setupRobotConnection() {
		while (true)  {
			try {
				
				robot = new Cobot3Robot();
				
				System.out.print("[RobotFactory] Waiting for CoBot 3 to connect ... ");
				Socket s = (new ServerSocket(ROBOT_CONTROL_PORT, 10)).accept();
				System.out.println("Connected");	
				
				robot.connect(s);			
				
				return robot;
			} catch (Exception e) { 
				e.printStackTrace();
			}			
		}			
	}	
}
