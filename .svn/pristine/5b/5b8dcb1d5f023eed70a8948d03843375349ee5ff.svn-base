/*
 * Cobot3DashboardCapability.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.capability.Capability.CapabilityStatus;
import edu.cmu.gizmo.management.robot.Cobot3Robot;
import edu.cmu.gizmo.management.robot.exceptions.RobotUnavailableException;

/**
 * The dashboard for cobot 3. This capability should provide basic,
 * cross-cutting services, such as camera control, the map coordinates, and
 * video
 * 
 * @version 1.0
 * @author Jeff Gennari
 * 
 */
public class Cobot3DashboardCapability extends Capability {

	/** The description of this capability. */
	private final String DESCRIPTION = "The CoBot 3 Dashboard";

	/** The name. */
	private final String NAME = "Cobot3DashboardCapability";

	/** camera operation is a requirement for this capability. */
	private final String INPUT_CAMERA = "camera";

	/** camera operation is a requirement for this capability. */
	private final String INPUT_MOVE_INCREMENTAL = "moveCobot";
	
	/** This capability generates Images. */
	private final String OUTPUT_IMAGE = "image";

	private final String ROBOT_CONFIG = "robot.object";

	/** The cobot. */
	private Cobot3Robot cobot;

	/** a flag to control capability execution */
	private Boolean running;

	/**
	 * Instantiates a new cobot3 dashboard capability.
	 * 
	 * @param robot
	 *            the robot
	 */
	public Cobot3DashboardCapability() {
		cobot = null;
		running = false;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#execute()
	 */
	@Override
	public void execute() {

		// The dashboard needs to wait while the cobot fires up
		// try {
		// Thread.sleep(5000);
		// } catch(InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		System.out.println("[Cobot3DashboardCapability] executing");
		running = true;
		while (running == true) {
			while ((getStatus() == CapabilityStatus.RUNNING) && running) {
				try {

					final byte[] img = cobot.getVideoImage();
					if (img != null) {
						sendOutput("image", img);
					}

					// pause between image requests
					Thread.sleep(250);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
			}
		}

		System.out.println("[Cobot3DashboardCapability] completed");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#terminate()
	 */
	@Override
	public void terminate() {
		System.out.println("[Cobot3DashboardCapability] terminated");
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#getCapabilityName()
	 */
	@Override
	public String getCapabilityName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getCapabilityDescription()
	 */
	@Override
	public String getCapabilityDescription() {
		return DESCRIPTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#setInput(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void setInput(final ConcurrentHashMap<Object, Object> input) {

		/*
		 * Send the camera control command to the robot
		 */
		if (input.containsKey(INPUT_CAMERA)) {
			final Float[] camParams = (Float[]) input.get(INPUT_CAMERA);
			try {
				synchronized (cobot) {
					cobot.moveCamera(camParams[0], camParams[1]);
				}
			} catch (final RobotUnavailableException e) {
				e.printStackTrace();
			}
		}
		/*
		 * Send the move command to the robot
		 */
		else if (input.containsKey(INPUT_MOVE_INCREMENTAL)) {
			final Float[] moveParams = (Float[]) input.get(INPUT_MOVE_INCREMENTAL);
			try {
				synchronized (cobot) {
					cobot.moveRobotIncremental(moveParams[0], moveParams[1]);
				}
			} catch (final RobotUnavailableException e) {
				e.printStackTrace();
			}	
		}
		/*
		 * FOR TESTING - REMOVE AFTER DEMO
		 */
		
		else if (input.containsKey("emergency")) {
			try {
				synchronized (cobot) {
					cobot.emergency();
				}
			} catch (final RobotUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getInputParameterValue
	 * (java.lang.Object)
	 */
	@Override
	public Object getInputParameterValue(final Object param) {
		return null;
	}

	/**
	 * Pause.
	 * 
	 * @return null because the dashboard keeps no state
	 */
	public Object pause() {
		running = false;
		setStatus(CapabilityStatus.PAUSED, "Capability suspended");
		return running;
	}

	/**
	 * Resume.
	 * 
	 * @param state
	 *            the state
	 */
	public void resume(final Object state) {
		if (getStatus() == CapabilityStatus.PAUSED) {
			if (state != null) {
				setStatus(CapabilityStatus.RUNNING, "Resumed task");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getInputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getInputRequirements() {
		final ConcurrentHashMap<String, Class> inputReqs = new ConcurrentHashMap<String, Class>();

		// describe the input
		inputReqs.put(INPUT_CAMERA, Float[].class);

		return inputReqs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getOutputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getOutputRequirements() {

		final ConcurrentHashMap<String, Class> outputReqs = new ConcurrentHashMap<String, Class>();

		// describe the input
		outputReqs.put(OUTPUT_IMAGE, byte[].class);

		return outputReqs;
	}
}
