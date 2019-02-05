/*
 * DummyCapability.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

import edu.cmu.gizmo.management.robot.Cobot3CommandStatus;
import edu.cmu.gizmo.management.robot.Robot;

/**
 * The Class DummyCapability.
 */
public class DummyCapability extends Capability implements PausableCapability {

	/** The description. */
	private final String DESCRIPTION = "DummyCapability";

	/** The cmd status. */
	Cobot3CommandStatus cmdStatus = new Cobot3CommandStatus();

	/**
	 * Instantiates a new dummy capability.
	 * 
	 * @param cobot
	 *            the cobot
	 */
	public DummyCapability(final Robot cobot) {
		System.out.println("[DummyCapability] starts...");
	}

	public DummyCapability() {
		System.out.println("[DummyCapability] Empty Constructor starts...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#execute()
	 */
	@Override
	public void execute() {
		System.out.println("[DummyCapability] Execute...");
		try {
			Thread.currentThread();
			Thread.sleep(1000); // sleep for 1s
		} catch (final Exception ie) {
			ie.printStackTrace();
		}

		System.out.println("[DummyCapability] " + DESCRIPTION
				+ " in complete state");
		// COMPLETE
		setStatus(CapabilityStatus.COMPLETE, getCapabilityName()
				+ " in complete state");
		System.out.println("[DummyCapability] Complete...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#getCapabilityName()
	 */
	@Override
	public String getCapabilityName() {
		return new String();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getCapabilityDescription()
	 */
	@Override
	public String getCapabilityDescription() {
		return new String();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.PausableCapability#pause()
	 */
	@Override
	public Object pause() {
		final ConcurrentHashMap<Object, Object> hp = new ConcurrentHashMap<Object, Object>();
		return hp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.PausableCapability#resume(java.lang
	 * .Object)
	 */
	@Override
	public void resume(final Object state) {

	}

	/**
	 * Handle message.
	 * 
	 * @param message
	 *            the message
	 */
	protected void handleMessage(final Message message) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#terminate()
	 */
	@Override
	public void terminate() {

	}

	/**
	 * Gets the configuration parameter.
	 * 
	 * @param param
	 *            the param
	 * @return the configuration parameter
	 */
	public Object getConfigurationParameter(final Object param) {
		return new Object();
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getInputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getInputRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getOutputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getOutputRequirements() {
		// TODO Auto-generated method stub
		return null;
	}
}
