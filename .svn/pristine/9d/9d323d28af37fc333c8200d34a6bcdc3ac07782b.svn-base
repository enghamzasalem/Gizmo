/*
 * Cobot3Robot.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.robot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;

import edu.cmu.gizmo.management.robot.Cobot3CommandStatus.CommandStatus;
import edu.cmu.gizmo.management.robot.exceptions.InvalidCommandException;
import edu.cmu.gizmo.management.robot.exceptions.InvalidCommandStatusException;
import edu.cmu.gizmo.management.robot.exceptions.InvalidRoomException;
import edu.cmu.gizmo.management.robot.exceptions.RobotUnavailableException;
import edu.cmu.gizmo.management.taskclient.Cobot3TaskClient;
import edu.cmu.gizmo.management.taskclient.GizmoTaskClient;


/**
 * This is the communication interface used by the Task Manager to control the
 * CoBot 3 robot. This is the single point of contact for the CoBot 3 robot.
 * This interface serves as the API between the Robot Platform and the
 * Management System. This interface must support two types of commands:
 * 
 * <ol>
 * <li>long running commands
 * <li>short, interactive commands
 * </ol>
 * 
 * This API must also support fetching output from the CoBot 3 robot.
 * 
 * To deal with long running commands, this interface expects the implementing
 * element to maintain a list of active commands.
 * 
 * @version 1.0
 * @author jsg
 * 
 */
public class Cobot3Robot implements Robot {	
	
	/** This is the socket through which an object of this * class can communicate with the Robot Manager. */
	private Socket cobotSocket;
	
	private Socket cobotTaskSocket;
	
	/** This object will be used to write command strings to the server through the opened socket. */
	private PrintWriter commandSender;

	/** This object will be used to read string messages from the server through the opened socket. */
	private BufferedReader commandReceiver;

	/**
	 * This method is used to establish a connection to the robot.
	 *
	 * @return a reference to a connected CoBot 3
	 * @pre <ul>
	 * <li>The CoBot 3 is available
	 * </ul>
	 * @post <ul>
	 * <li>On success, a connection is established with the CoBot 3 and
	 * the reference is returned
	 * <li>On failure, the returned Cobot3robot reference is null
	 * <li>The command is added to the active command list
	 * </ul>
	 * @rationale There can only be one active connection to the CoBot 3 robot,
	 * so this method is a singleton
	 */
	protected Cobot3Robot() {
		cobotSocket = null;
		cobotTaskSocket = null;
		commandSender = null;
		commandReceiver = null;
	}

	/**
	 * Make CoBot 3 say something through its speech synthesizer.
	 *
	 * @param phrase the phrase to speak
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is connected.
	 * </ul>
	 * @post The state of the CoBot 3 is unchanged
	 * @rationale This command is needed to use the CoBot 3's speech facilities.
	 * @example
	 * 
	 * <pre>
	 * try {
	 * cobot.speak(&quot;Hello world!&quot;);
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public void speak(final String phrase) throws RobotUnavailableException {
	
	}

	/* (non-Javadoc)
	 * @see edu.cmu.gizmo.management.robot.Robot#connect(java.net.Socket)
	 */
	@Override
	public void connect(final Socket connection) {
		this.cobotSocket = connection;

		try {
			
			cobotSocket.setSoTimeout(2000);	
			cobotSocket.setKeepAlive(true);
			
			commandSender = 
				new PrintWriter(
						cobotSocket.getOutputStream());
			
			commandReceiver = 
				new BufferedReader(new InputStreamReader(
					cobotSocket.getInputStream()));
			
			new BufferedInputStream(
					cobotSocket.getInputStream());

		} catch (final IOException e) {	
			e.printStackTrace();
		}		
	}

	/**
	 * Disconnect from the robot.
	 */
	@Override
	public void disconnect() {
		try {
			if (cobotSocket != null) {
				cobotSocket.shutdownInput();
				cobotSocket.shutdownOutput();
				cobotSocket.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send the CobBt 3 to a specified room.
	 *
	 * @param roomNumber a String indicating the room number to send the robot
	 * @return <code>Command</code> a Command object that represents the active
	 * command
	 * 
	 * <p>
	 * @throws RobotUnavailableException if the robot is not available
	 * @throws InvalidRoomException if the specified room does not exist
	 * @throws InvalidCommandStatusException if the retuned command status is either has an empty,
	 * undefined, or unformatted value
	 * @pre <ul>
	 * <li> The CoBot 3 must be connected and available to execute this
	 * command
	 * </ul>
	 * @post <ul>
	 * <li> On success, the robot is en-route to the specified room number
	 * and the Command object is created. <li> On failure, the returned
	 * Command object is null <li> The command is added to the active
	 * command list
	 * </ul>
	 * @rationale Sending the robot to a room may take a significant amount of
	 * time. Rather than have this method block while the robot is in
	 * transit, it makes more sense to return a reference to the
	 * executed command. This reference can be used later to
	 * determine command status. Note that now the TaskExecutor and
	 * RobotManager will need to maintain a list of active commands.
	 * @See Cobot3Command
	 */
	public synchronized Cobot3Command goToRoom(final String roomNumber)
	throws RobotUnavailableException, InvalidRoomException,
	InvalidCommandStatusException {

			String response = null;
			Cobot3Command goToRoomCommand = null;
			if ((cobotSocket == null) || !cobotSocket.isConnected()) {
				// check if the socket is (1) initialized (2) connected
				throw new RobotUnavailableException();
			}
			try {
				/*
				 * Send a message to the Robot Manager (GoToRoom + [roomNumber])
				 */
				commandSender.write("GoToRoom," + roomNumber);
				commandSender.flush();

				// get the response from the Robot Manager
				response = commandReceiver.readLine();
				if (response == null) {
					throw new InvalidCommandStatusException("Empty response");
				}
				
				final String[] responseTokens = response.split(",");
				final String commandStatus = responseTokens[0];
				
				if (commandStatus.equals("invalid room")) {
					throw new InvalidRoomException("Invalid room!");
				}
				if (commandStatus.equals("success")) {
					/*
					 * the capability can be executed by the Robot Manager, any
					 * other message will be considered as a denial of service
					 */
					if (responseTokens.length < 2) {
						throw new InvalidCommandStatusException(
								"success message must be "
								+ "followed with a command ID");
					}
					goToRoomCommand = new Cobot3Command();
					/*
					 * set the command number that is generated from the Robot
					 * Manager
					 */
					final int commandNumber = Integer.parseInt(responseTokens[1]);
					goToRoomCommand.setCmdNumber(commandNumber);
				}
				
			} 
			catch (final IOException e) {
				throw new RobotUnavailableException();
			}

			return goToRoomCommand;
	}

	/**
	 * Send the CoBot to a specified (X,Y) coordinate with a theta rotation.
	 *
	 * @param x the x coordinate to go to
	 * @param y the y coordinate to go to
	 * @param theta the rotation amount
	 * @return <code>Command</code> a Command object that represents the active
	 * command
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * </ul>
	 * @post <li>On success, the robot is en-route to the specified location and
	 * the Command object is created. <li>On failure, the returned Command
	 * object is null <li>The command is added to the active command list
	 * <li>
	 * @rationale Sending the robot to a position may take a significant amount
	 * of time. Rather than have this method block while the robot is
	 * in transit, it makes more sense to return a reference to the
	 * executed command. This reference can be used later to
	 * determine command status. Note that now the TaskExecutor and
	 * RobotManager will need to maintain a list of active commands.
	 */
	public Cobot3Command goToPosition(final Float x, final Float y,
			final Float theta) throws RobotUnavailableException {
		return null;
	}

	/**
	 * Incrementally move the robot's position.
	 *
	 * @param forward the amount to move the robot forwards or backward
	 * @param sideways the amount to move the roboot sideways
	 * @param turn the amount to turn the robot
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * <li>The robot is in teleoperation mode
	 * </ul>
	 * @post The robot moves the specified amount
	 * @rationale The CoBot 3 robot doesn't have a mechanism to track
	 * teleoperation mode, so we will have to. However, we need to
	 * track this to determine when the robot should allow manual
	 * control vs. MDS.
	 */
	public void moveRobotIncremental(final Float forward, final Float rotation) 
	throws RobotUnavailableException {
		
		try {
			/*
			 * Send a message to the Robot Manager: (MoveCamera,PAN,TILT)
			 */
			synchronized (cobotSocket) {
				commandSender.write("MoveCobot," + forward.toString() 
						+ "," + rotation.toString());

				commandSender.flush();
			}
		} 
		catch (final Exception e) {
			throw new RobotUnavailableException();
		}
		
	}

	/**
	 * Get the status of an active robot command.
	 *
	 * @param command The active command for which to fetch status
	 * @return the Cobot3CommandStatus in
	 * @throws RobotUnavailableException if the robot is not available
	 * @throws InvalidCommandException the invalid command exception
	 * @throws InvalidCommandStatusException the invalid command status exception
	 * @pre <ul>
	 * <li>the <code>command</code> object represents an active command
	 * </ul>
	 * @post <ul>
	 * <li>The status member of the <code>command</code> is updated to
	 * reflect the command's status
	 * </ul>
	 * @rationale Fetching the status of a command is a way to deal with long
	 * running commands. Each specific command is responsible for
	 * indicating its status and what that status means
	 * @see Cobot3Command
	 * @see Cobot3CommandStatus
	 * @example <pre>
	 * Cobot3Command cmd = cobot.goToRoom(newString(&quot;261&quot;));
	 * Cobot3CommandStatus status = cobot.getCommandStaus(cmd);
	 * if (status.getStatus() == Cobot3CommandStatus.COMPLETED) {
	 * status.getStatusMessage();
	 * }
	 * </pre>
	 */
	public synchronized Cobot3CommandStatus getCommandStatus(final Cobot3Command command)
			throws RobotUnavailableException, InvalidCommandException,
			InvalidCommandStatusException {
		
		String response = null;
		Cobot3CommandStatus commandStatus = null;

		try {
			/*
			 * send the status command to the Robot Manager
			 * (status,[commandNumber])
			*/
			while (true) {
				try {		
					synchronized (cobotSocket) {
						commandSender.write("status," + command.getCmdNumber());
						commandSender.flush();
						response = commandReceiver.readLine();
					}
					
					break;
			
				} catch (SocketTimeoutException e) { 
					System.out.println(
							"[Cobot3Robot] getCommandStatus timeout");
				} catch(NullPointerException e ) {
					if (cobotSocket.isConnected() == false)
						break;
				}
			}
			
			if (response == null) {
				throw new InvalidCommandStatusException("Empty response");
			}
			final String[] responseTokens = response.split(",");
			final String status = responseTokens[0];
			if (status.equals("does not exist")) {
				throw new InvalidCommandException("unknown Id");
			}
			if (responseTokens.length <= 0 ) {
				throw new InvalidCommandStatusException(
						"status response must be followed " 
						+ "with a message ("+command.getCmdNumber() 
						+ "="+response+")");
			}
			commandStatus = new Cobot3CommandStatus();

			if (status.equals("running")) {
				commandStatus.setStatus(CommandStatus.RUNNING);
			} 
			else if (status.equals("complete")) {
				commandStatus.setStatus(CommandStatus.COMPLETE);
			} 
			else if (status.equals("error")) {
				commandStatus.setStatus(CommandStatus.ERROR);
			} 
			else if (status.equals("waiting")) {
				commandStatus.setStatus(CommandStatus.WAITING);
			} 
			else if (status.equals("canceled")) {
					commandStatus.setStatus(CommandStatus.CANCELED);
			} 
			else {
				throw new InvalidCommandStatusException(
						"Unknown command status string. " + "Must be in "
						+ "[running, cancled, complete, error, waiting]");
			}
			commandStatus.setStatusMessage(responseTokens[1]);

		} catch (final IOException e) {
			throw new RobotUnavailableException();
		}

		return commandStatus;
	}

	/**
	 * Get an estimate (in seconds) of how long it will take CoBot to move from
	 * its current location to a specified room.
	 *
	 * @param roomNumber the room number
	 * @return the time estimate
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @throws InvalidRoomException the invalid room exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * </ul>
	 * @post The estimate is returned
	 * @rationale This method uses the existing CoBot infrastructure to get a
	 * time estimate. These estimates are needed for task planning.
	 * The robot knows it's position and driving capabilities best,
	 * so it should provide these estimates.
	 * @example <pre>
	 * try {
	 * Cobot3Robot cobot = RobotFactory.getRobot(&quot;cobot3&quot;);
	 * Float est = cobot.estimateNavigationTime(newString(&quot;261&quot;));
	 * if (est.compareTo(3600.00) == 1) {
	 * // ...
	 * }
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public Float estimateNavigationTime(final String roomNumber)
			throws RobotUnavailableException, InvalidRoomException {
		return null;
	}

	/**
	 * Get an estimate (in seconds) of how long it will take CoBot to move from
	 * its current location to a specified location.
	 *
	 * @param x the first part of a (X,Y) coordinate that indicates location
	 * @param y the second part of a (X,Y) coordinate that indicates location
	 * @return the time estimate
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * </ul>
	 * @post The estimate is returned
	 * @rationale This method uses the existing CoBot infrastructure to get a
	 * time estimate. These estimates are needed for task planning.
	 * The robot knows it's position and driving capabilities best,
	 * so it should provide these estimates.
	 * @example <pre>
	 * try {
	 * Cobot3Robot cobot = RobotFactory.getRobot(&quot;cobot3&quot;);
	 * Float est = cobot.estimateNavigationTime(21.3, 54.9);
	 * if (est.compareTo(3600.00) == 1) {
	 * // ...
	 * }
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public Float estimateNavigationTime(final Float x, final Float y)
			throws RobotUnavailableException {
		return null;
	}

	/**
	 * Return the status of the robot in terms of whether it is tasked, in
	 * error, or idle.
	 *
	 * @return the Cobot3Status object indicating the robot's status
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * </ul>
	 * @post The status is returned
	 * @rationale This method should provide the TaskExecutor with enough
	 * information to determine the status of the robot, such as
	 * whether the robot is ready for tasking, tasked, or has
	 * suffered an error
	 * @see Cobot3Status
	 * @example <pre>
	 * try {
	 * Cobot3Status status = cobot.getCobotStatus()
	 * if (status.getStatus() == Cobot3Status.IDLE) {
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public synchronized Cobot3Status getCobotStatus() 
	throws RobotUnavailableException {
		return null;
	}

	/**
	 * This method obtains an image from the main camera installed on CoBot. It is used for the video frame in the
	 * CoBot3DashBoard.
	 * 
	 * @return The Image object obtained from the Robot Platform.
	 * @throws RobotUnavailableException In case that we can't establish a connection with the Robot
	 */	
	
	
	public synchronized byte[] getVideoImage() throws RobotUnavailableException {
		while (true) {
			try {		
				
				commandSender.write("GetImage");
				commandSender.flush();
				
				return Base64.decodeBase64(
						commandReceiver.readLine()
				);
			}
			catch (SocketTimeoutException e) {

				System.out.println("[Cobot3Robot] getImage recv timeout");
			}
			catch (Exception e) { 
				return null;
			}
		}
	}
	
	/**
	 * This method fetches CoBot 3 state. The specific data values are set by
	 * the Robot Manager
	 *
	 * @return a <code>ConcurrentHashMap<k,V></code> containing a set of key/value pairs
	 * for CoBot 3 output. Included in this hash is the following keys
	 * 
	 * <ul>
	 * <li>y: the y coordinate of CoBot 3's position. This will be a
	 * Float
	 * <li>theta: CoBot 3's rotation. This will be a Float
	 * <li>map: the current map ("300SCRG"). This will be a String
	 * <li>kinect: the points needed to draw the kinect. This will be a
	 * Float[]
	 * <li>path: the path for CoBot 3 to follow. This will be a Float[]
	 * <li>bl: true/false value for whether backlight is on. This will
	 * be a Boolean
	 * <li>cp: camera pan. This will be a Float
	 * <li>ct: camera tilt. This will be a Float
	 * <li>cz: camera zoom level. This will be a Float
	 * <li>es: emergency stopped state. This will be a Boolean
	 * <li>pb: is path blocked. This will be a Boolean
	 * <li>bv: battery voltage. This will be a Float
	 * <li>lw: last words. This will be a String
	 * <li>image: this is the CoBot video image. This will be an Object
	 * </ul>
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is connected.
	 * </ul>
	 * @post The state of the CoBot 3 is unchanged
	 * @see ConcurrentHashMap
	 * @rationale This method will need to be called within it's own thread and
	 * be dedicated it's own socket to interact with the CoBot 2
	 * Client. A generic ConcurrentHashMap was used so as not to bind a
	 * specific type of object to this interface.
	 * @example <pre>
	 * try {
	 * ConcurrentHashMap state = cobot.getCobotState();
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public synchronized ConcurrentHashMap<String, Object> getCobotState()
			throws RobotUnavailableException {
			
		return null;
	}
 
	/**
	 * Get a specific setting for the Cobot.
	 *
	 * @param parameter the name of the parameter
	 * @param value the value to assign the parameter
	 * @return the requested setting
	 */
	public Object getCobotSettings(final String parameter, 
			final String value) {
				
		return null;
	}
 	
	/**
	 * Change a CoBot setting.
	 *
	 * @param parameter the name of the setting to change
	 * @param value the value of the setting
	 */
	public void setCobotSetting(final String parameter, 
			final Object value) {

		
	}
	
	/**
	 * This command places CoBot in teleoperation mode.
	 *
	 * @return true is in joystick mode; false otherwise.
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * <li>The CoBot 3 is not in teleoperation mode
	 * <ul>
	 * @post The robot is in teleoperation mode
	 * @rationale Teleoperation mode is the default mode when the robot is not
	 * navigating autonomously. In teleoperation mode, the robot is
	 * able be incrementally moved by the keyboard.
	 * @example <pre>
	 * 
	 * try { if (cobot.enableTeleopMode() == true) return true; } catch
	 * (RobotUnavailableException rue) { rue.printStackTrace(); }
	 * 
	 * </pre>
	 */
	public Boolean enableTeleopControl() throws RobotUnavailableException {
		return true;
	}

	/**
	 * This command takes CoBot 3 out of teleop mode.
	 *
	 * @return true if out of teleop mode; false otherwise.
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * <li>The CoBot 3 is currently in teleop mode
	 * <ul>
	 * @post The robot is in teleopeation mode
	 * @rationale This command checks CoBot's state to determine when
	 * teleoperation mode has been ended. When removed from
	 * teleoperation mode, the robot should resume the last
	 * navigation command (if it existed)
	 * @example <pre>
	 * 
	 * try { if (cobot.disableJoystickControl() == true) return true; }
	 * catch (RobotUnavailableException rue) { rue.printStackTrace(); }
	 * 
	 * </pre>
	 */
	public Boolean disableTeleopControl() throws RobotUnavailableException {
		return true;
	}

	/**
	 * This command signals that CoBot 3 is ready for joystick control.
	 *
	 * @return true is in joystick mode; false otherwise.
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * <li>The CoBot 3 is not in joystick mode
	 * <ul>
	 * @post The robot is in joystick mode
	 * @rationale This command checks to see if CoBot is in Joystick mode.
	 * @example <pre>
	 * 
	 * try { if (cobot.enableJoystickControl() == true) return true; }
	 * catch (RobotUnavailableException rue) { rue.printStackTrace(); }
	 * 
	 * </pre>
	 */
	public Boolean enableJoystickControl() throws RobotUnavailableException {
		return true;
	}

	/**
	 * This command indicates when CoBot 3 is out of joystick mode.
	 *
	 * @return true if out of joystick mode; false otherwise.
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is on and connected
	 * <li>The CoBot 3 is currently in joystick mode
	 * <ul>
	 * @post The robot is not in joystick mode
	 * @rationale This command checks CoBot's state to determine when joystick
	 * mode has been ended. This command will block until CoBot is
	 * out of joystick mode.
	 * @example <pre>
	 * 
	 * try { if (cobot.disableJoystickControl() == true) return true; }
	 * catch (RobotUnavailableException rue) { rue.printStackTrace(); }
	 * 
	 * </pre>
	 */
	public Boolean disableJoystickControl() throws RobotUnavailableException {
		return true;
	}

	/**
	 * Cancel a CoBot 3 command.
	 *
	 * @param command the <code>command</code> to cancel
	 * @return the boolean
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @returns true if the command could be canceled; false otherwise
	 * @pre <ul>
	 * <li>The <code>command</code> to cancel exists and is actively being
	 * executed
	 * <li>The CoBot 3 is on and connected
	 * @post The CoBot 3 is no longer executing the <code>command</code>
	 * @rationale The user can interrupt long-running commands on the robot;
	 * thus, there needs to be a way to stop an executing command.
	 * Canceled commands cannot be restarted; rather the system will
	 * need to issue a new command. This is a much simpler way to
	 * handle interruption and re-tasking. It is up to the
	 * TaskExecutor to supply the correct command to cancel.
	 * @example <pre>
	 * 
	 * try { cobot.cancelCommand(command) == true) return true; } catch
	 * (RobotUnavailableException rue) { rue.printStackTrace(); }
	 * 
	 * <pre>
	 */
	public Boolean cancelCommand(final Cobot3Command command)
			throws RobotUnavailableException {
		return true;
		
	}

	/**
	 * Moves the CoBot's camera up/down and left/right.
	 *
	 * @param horizontal the left/right position for the camera
	 * @param vertical the up/down position for the camera
	 * 
	 * * @pre
	 * <ul>
	 * <li>The <code>command</code> to cancel exists and is actively
	 * being executed
	 * <li>The CoBot 3 is connected.
	 * </ul>
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @post The CoBot 3 is no longer executing the <code>command</code>
	 * @rationale The camera can be moved incrementally by a user; thus there
	 * needs to be a convenience method for moving the camera
	 * @example
	 * @example <pre>
	 * try {
	 * cobot.moveCamera(x,y);
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * 
	 * <pre>
	 */
	public synchronized void moveCamera(final Float horizontal, final Float vertical)
			throws RobotUnavailableException {
		
		try {
			/*
			 * Send a message to the Robot Manager: (MoveCamera,PAN,TILT)
			 */
			synchronized (cobotSocket) {
				commandSender.write("MoveCamera," 
						+ horizontal.toString() 
						+ "," + vertical.toString());

				commandSender.flush();
			}
		} 
		catch (final Exception e) {
			throw new RobotUnavailableException();
		}
	}

	/**
	 * zoom the CoBot's camera.
	 *
	 * @param horizontal the horizontal
	 * @param vertical the vertical
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is connected.
	 * </ul>
	 * @post The CoBot 3's camera is zoomed
	 * @rationale The camera can be zoomed by a user; thus there needs to be a
	 * convenience method for zooming the camera
	 * @example
	 * @example <pre>
	 * try {
	 * cobot.moveCamera(x,y);
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * 
	 * <pre>
	 */
	public void zoomCamera(final Float horizontal, final Float vertical)
			throws RobotUnavailableException {
	}

	/**
	 * present a question to a participant through the CoBot's screen interface.
	 *
	 * @param question the question to ask
	 * @param choices the answer options
	 * @throws RobotUnavailableException the robot unavailable exception
	 * @pre <ul>
	 * <li>The CoBot 3 is connected.
	 * </ul>
	 * @post The state of the CoBot 3 is unchanged
	 * @rationale This command is used to make CoBot 3 ask a question
	 * 
	 * * @example
	 * 
	 * <pre>
	 * try {
	 * cobot.askQuestion(&quot;How are you?&quot;, responses);
	 * } catch (RobotUnavailableException rue) {
	 * rue.printStackTrace();
	 * }
	 * </pre>
	 */
	public void askQuestion(final String question, final String[] choices)
			throws RobotUnavailableException {
	}

	/**
	 * TEST CODE - REMOVE AFTER DEMO 
	 * 
	 * @throws RobotUnavailableException
	 */
	public void emergency() throws RobotUnavailableException {
		try {
			synchronized (cobotSocket) {
				commandSender.write("emergency");
				commandSender.flush();
			}
		} 
		catch (final Exception e) {
			throw new RobotUnavailableException();
		}

		
	}
}
