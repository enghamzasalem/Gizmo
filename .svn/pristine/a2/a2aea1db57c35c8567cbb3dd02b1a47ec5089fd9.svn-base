/*
 * GoToRoomCapability.java 1.0 12/06/18
 */
package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.robot.Cobot3Command;
import edu.cmu.gizmo.management.robot.Cobot3CommandStatus;
import edu.cmu.gizmo.management.robot.Cobot3CommandStatus.CommandStatus;
import edu.cmu.gizmo.management.robot.Cobot3Robot;
import edu.cmu.gizmo.management.robot.exceptions.InvalidCommandException;
import edu.cmu.gizmo.management.robot.exceptions.InvalidCommandStatusException;
import edu.cmu.gizmo.management.robot.exceptions.InvalidRoomException;
import edu.cmu.gizmo.management.robot.exceptions.RobotUnavailableException;

/**
 * This class is the capability used to send the robot to a specified room on
 * the 2nd floor of SCR.
 * 
 * @version 1.0 18 Jun 2012
 * @author Jeff Gennari
 */
public class GoToRoomDriveCapability extends Capability implements
		PausableCapability {

	/** The human-readable description for this capability. */
	private final String DESCRIPTION = "Go To A Room";

	/** The human-readable description for this capability. */
	private final String NAME = "GoToRoomDriveCapability";

	/** Room number is a requirement for this capability. */
	private final String INPUT_ROOM = "room";

	/** The output status. */
	private final String OUTPUT_STATUS = "status";

	/** The interface to the CoBot 3 robot. */
	private Cobot3Robot cobot;

	/** The destination room. */
	private String room;

	/** A handle to the active goto room command. */
	private Cobot3Command driveCommand;

	/** controls whether the capability is running. */
	private Boolean running;

	/** robot configuration Cr */
	private final String ROBOT_CONFIG = "robot.object";

	/**
	 * Initialize the new go to room capability.
	 * 
	 * @param robot
	 *            a connection to the CoBot 3 robot
	 */
	public GoToRoomDriveCapability() {

		cobot = null;
		room = null;
		driveCommand = null;
		running = false;
	}

	/**
	 * Load and the resources needed to execute this capability. Aside from
	 * identifiers and the bus, check that the robot is not null
	 * 
	 * @param tid
	 *            a unique identifier for this task.
	 * @param cid
	 *            a unique identifier for this capability.
	 * @return true if loaded OK, false otherwise
	 */
	@Override
	public Boolean load(final Integer tid, final Integer cid,
			final ConcurrentHashMap<Object, Object> config) {

		if (config.containsKey(ROBOT_CONFIG)) {
			cobot = (Cobot3Robot) config.get(ROBOT_CONFIG);
			if (cobot != null) {
				return super.load(tid, cid, config);
			}
		}

		// Not connected to CoBot 3 - signal that an error occurred
		setStatus(CapabilityStatus.ERROR, "Cobot 3 not connected");
		return false;
	}

	/**
	 * Execute the capability.
	 */
	@Override
	public void execute() {

		running = true;
		// while the capability is executing
		while (running == true) {

			if (getStatus() == CapabilityStatus.RUNNING) {

				// execute a new drive command
				if (driveCommand == null) {

					try {
						// only execute the drive command if the a room is
						// specified
						if (room != null) {
							driveCommand = cobot.goToRoom(room);
						}
					} catch (final RobotUnavailableException e) {
						// for now just mark the lost connection and move on
						setStatus(CapabilityStatus.ERROR,
								"Robot not available: " + e.getMessage());
						e.printStackTrace();
					} catch (final InvalidRoomException e) {
						setStatus(CapabilityStatus.ERROR, "Room not valid: "
								+ e.getMessage());
						e.printStackTrace();
					} 
					catch (final InvalidCommandStatusException e) {
						e.printStackTrace();
					} 
					catch (final NullPointerException e) {
						e.printStackTrace();
					} 
					catch (final Exception e) {
						setStatus(CapabilityStatus.ERROR,
								"Error occured: " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					// The capability is currently executing.
					try {

						Cobot3CommandStatus cmdStatus = null;

						try {

							cmdStatus = cobot.getCommandStatus(driveCommand);

						} catch (final InvalidCommandException e) {
							setStatus(CapabilityStatus.ERROR,
									"Invalid command: " + e.getMessage());
							e.printStackTrace();
						} catch (final InvalidCommandStatusException e) {
							cmdStatus = null;
							e.printStackTrace();
						}

						if (cmdStatus != null) {
							if (cmdStatus.getStatus() == CommandStatus.COMPLETE) {

								// the capability completed successfully
								sendOutput(OUTPUT_STATUS,
										cmdStatus.getStatusMessage());
								setStatus(CapabilityStatus.COMPLETE,
										"Arrived at room");
								driveCommand = null;
							} else if (cmdStatus.getStatus() == CommandStatus.ERROR) {
								// the command could not complete
								setStatus(CapabilityStatus.ERROR,
										cmdStatus.getStatusMessage());
							}
							else if (cmdStatus.getStatus() == CommandStatus.CANCELED) {
									// the command was canceled 
									setStatus(CapabilityStatus.CANCELED,
											cmdStatus.getStatusMessage());
									
							} else if (cmdStatus.getStatus() == CommandStatus.RUNNING) {
								// send the command status message to the client
								sendOutput(OUTPUT_STATUS,
										cmdStatus.getStatusMessage());
							}
						} else {
							System.out
									.println("[GoToRoomDriveCapability] Could not get status");
						}
					} catch (final RobotUnavailableException e) {

						e.printStackTrace();
					}
				}
			} else if (getStatus() == CapabilityStatus.COMPLETE) {
				// The capability completed successfully
				// send the last output and terminate
				running = false;

			} else if (getStatus() == CapabilityStatus.PAUSED) {
				running = false;

			} else if (getStatus() == CapabilityStatus.CANCELED) {
				// The capability has been canceled -> Kill the active
				// command.
				try {
					
					// terminate the current drive command
					cobot.cancelCommand(driveCommand);
					driveCommand = null;

				} catch (final Exception e) {
					e.printStackTrace();
				}
				// stop capability execution
				running = false;
			} else if (getStatus() == CapabilityStatus.ERROR) {
				// The capability experienced an error - currently just
				// stop executing
				running = false;
			}

			try {

				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get a configuration parameter for this capability.
	 * 
	 * @param param
	 *            the param
	 * @return the input parameter value
	 * @returns the value of <code>parameter</code> or null if the parameter is
	 *          not specified
	 */
	@Override
	public Object getInputParameterValue(final Object param) {

		if (((String) param).equals(INPUT_ROOM)) {
			return room;
		}
		return null;
	}

	/**
	 * set input for this capability.
	 * 
	 * @param input
	 *            the input parameters
	 */
	@Override
	public synchronized void setInput(
			final ConcurrentHashMap<Object, Object> input) {

		// there is not an active drive command
		if (driveCommand == null) {

			// the capability has been loaded
			if (input.containsKey(INPUT_ROOM)) {

				room = (String) input.get(INPUT_ROOM);

				System.out.println("[GoToRoomDriveCapability] Going to room "
						+ room);
			}
		}
	}

	/**
	 * pause the capability.
	 * 
	 * @return a ConcurrentHashMap containing the saved capability state
	 */
	@Override
	public Object pause() {

		@SuppressWarnings("serial")
		final ConcurrentHashMap<Object, Object> state = new ConcurrentHashMap<Object, Object>();
		state.put(INPUT_ROOM, room);

		try {
			cobot.cancelCommand(driveCommand);
		} catch (final RobotUnavailableException e) {

			e.printStackTrace();
			setStatus(CapabilityStatus.ERROR,
					"Could not terminate CoBot command");
			return null;

		} catch (final Exception e) {

			setStatus(CapabilityStatus.ERROR,
					"Unknown error while terminating command");
			return null;
		}
		room = null;
		setStatus(CapabilityStatus.PAUSED, "Capability suspended");

		return state;
	}

	/**
	 * Resume a paused task.
	 * 
	 * @param rawState
	 *            the raw state
	 */
	@Override
	public void resume(final Object rawState) {

		final ConcurrentHashMap<Object, Object> state = (ConcurrentHashMap<Object, Object>) rawState;

		if (getStatus() == CapabilityStatus.PAUSED) {
			if (state == null) {
				return;
			} else if (state.containsKey(INPUT_ROOM)) {
				room = (String) state.get(INPUT_ROOM);

				// force re-issue of drive command
				driveCommand = null;
				setStatus(CapabilityStatus.RUNNING, "Resumed task");
			}
		}
	}

	/**
	 * Get the capability description.
	 * 
	 * @return the human-readable capability description
	 */
	@Override
	public String getCapabilityDescription() {
		return DESCRIPTION;
	}

	/**
	 * Get the capability name.
	 * 
	 * @return the capability name
	 */
	@Override
	public String getCapabilityName() {
		return NAME;
	}

	/**
	 * Describe the required input for this capability.
	 * 
	 * @return the output descriptor for this capability. In this case, the
	 *         input is a string labeled "room".
	 */
	@Override
	public ConcurrentHashMap<String, Class> getInputRequirements() {

		final ConcurrentHashMap<String, Class> inputReqs = new ConcurrentHashMap<String, Class>();

		inputReqs.put(INPUT_ROOM, String.class);

		return inputReqs;
	}

	/**
	 * Describe the required input for this capability.
	 * 
	 * @return the output descriptor for this capability. In this case, the
	 *         output is a string labeled "status".
	 */
	@Override
	public ConcurrentHashMap<String, Class> getOutputRequirements() {
		final ConcurrentHashMap<String, Class> outputReqs = new ConcurrentHashMap<String, Class>();

		outputReqs.put(OUTPUT_STATUS, String.class);

		return outputReqs;
	}

	/**
	 * Terminate the capability. This means stop the active command and kill the
	 * thread.
	 */
	@Override
	public synchronized void terminate() {

		try {
			if (getStatus() != CapabilityStatus.COMPLETE) {
				// this capability is now canceled
				setStatus(CapabilityStatus.CANCELED, "Capability terminated");
				cobot.cancelCommand(driveCommand);
			}
			driveCommand = null;
			room = null;
			running = false;

		} catch (final RobotUnavailableException e) {
			setStatus(CapabilityStatus.ERROR,
					"Capability could not be canceled because the robot "
							+ "is unavailable");
			e.printStackTrace();
		} catch (final Exception e) {
			setStatus(CapabilityStatus.ERROR,
					"Capability could not be canceled because of exception: "
							+ e.getMessage());
		}

	}
}
