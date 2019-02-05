/*
 * SecondDummyCapability.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

import edu.cmu.gizmo.management.robot.Cobot3CommandStatus;
import edu.cmu.gizmo.management.robot.Robot;

/**
 * The Class SecondDummyCapability.
 */
public class SecondDummyCapability extends Capability implements
		PausableCapability {

	/** The cmd status. */
	Cobot3CommandStatus cmdStatus = new Cobot3CommandStatus();

	/**
	 * Instantiates a new second dummy capability.
	 * 
	 * @param cobot
	 *            the cobot
	 */
	public SecondDummyCapability(final Robot cobot) {
		System.out.println("[SecondDummyCapability] starts...");
		/*
		 * setStatus(CapabilityStatus.INIT, getCapabilityName() +
		 * " in initial state");
		 * 
		 * try{ Thread.currentThread().sleep(1000); //sleep for 1s }
		 * catch(Exception ie){ ie.printStackTrace(); } // Communication happens
		 * with TaskManager // COMPLETE
		 * 
		 * setStatus(CapabilityStatus.RUNNING, getCapabilityName() +
		 * " in running state");
		 * System.out.println("[SecondDummyCapability] Running...");
		 * 
		 * 
		 * try{ Thread.currentThread().sleep(20000); //sleep for 10s }
		 * catch(Exception ie){ ie.printStackTrace(); }
		 * 
		 * setStatus(CapabilityStatus.COMPLETE, getCapabilityName() +
		 * " in complete state");
		 * System.out.println("[SecondDummyCapability] Complete...");
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#execute()
	 */
	@Override
	public void execute() {
		System.out.println("[SecondDummyCapability] Execute...");
		try {
			Thread.currentThread();
			Thread.sleep(1000); // sleep for 10s
		} catch (final Exception ie) {
			ie.printStackTrace();
		}

		// COMPLETE
		setStatus(CapabilityStatus.COMPLETE, getCapabilityName()
				+ " in complete state");
		System.out.println("[SecondDummyCapability] Complete...");
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
	public void resume(final Object rawState) {

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
	public void setInput(final Object param, final Object value) {

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

	@Override
	public void setInput(final ConcurrentHashMap<Object, Object> input) {
		// TODO Auto-generated method stub

	}
}
