/*
 * SkypeCommunicationCapability.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.capability;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class SkypeCommunicationCapability.
 */
public class SkypeCommunicationCapability extends Capability implements
		PausableCapability {

	/** The human-readable description for this capability. */
	private final String DESCRIPTION = "Run the Skype capability";

	/** The human-readable description for this capability. */
	private final String NAME = "SkypeCommunicationCapability";

	/** The input time. */
	private final String INPUT_TIME = "time";

	/** The time to run Skype. */
	private Integer time;

	/** The running. */
	private Boolean running;

	/**
	 * Instantiates a new skype communication capability.
	 */
	public SkypeCommunicationCapability() {
		time = null;
		running = false;

		setStatus(CapabilityStatus.INIT, getCapabilityName()
				+ " in initial state");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#execute()
	 */
	@Override
	public void execute() {
		running = true;

		System.out.println("[SkypeCommunicationCapability] started");

		// don't go into skype mode until the time allocated is set
		while ((time == null) && (running == true)) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
			}
		}

		// if the capability is executing
		if (running == false) {
			return;
		}

		// simply wait until the the user is done and then complete

		try {
			Integer counter = 0;
			while ((counter < time) && (running == true)) {
				Thread.sleep(1000);
				counter++;
			}
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("[SkypeCommunicationCapability] ended");
		setStatus(CapabilityStatus.COMPLETE, "Skype over");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.Capability#terminate()
	 */
	@Override
	public synchronized void terminate() {
		running = false;
		notify();
		System.out.println("[SkypeCommunicationCapability] terminated");
		setStatus(CapabilityStatus.CANCELED, "Capability terminated");
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
	public synchronized void setInput(
			final ConcurrentHashMap<Object, Object> input) {

		if (time == null) {
			// the capability has been loaded

			if (input.containsKey(INPUT_TIME)) {

				if (input.get(INPUT_TIME) != null) {

					// for the sake of simplicity, time is in seconds
					try {
						time = Integer.parseInt((String) input.get(INPUT_TIME));
					} catch (final NumberFormatException e) {
						time = 3600;
					}
				}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.cmu.gizmo.management.capability.PausableCapability#pause()
	 */
	@Override
	public Object pause() {
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getInputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getInputRequirements() {
		final ConcurrentHashMap<String, Class> outputReqs = new ConcurrentHashMap<String, Class>();

		outputReqs.put(INPUT_TIME, String.class);

		return outputReqs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.cmu.gizmo.management.capability.Capability#getOutputRequirements()
	 */
	@Override
	public ConcurrentHashMap<String, Class> getOutputRequirements() {
		return null;
	}

}
