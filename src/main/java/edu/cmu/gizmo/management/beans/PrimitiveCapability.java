/*
 * PrimitiveCapability.java Jul 12, 2012 1.0
 */
package edu.cmu.gizmo.management.beans;

import java.io.Serializable;

/**
 * The Class PrimitiveCapability.
 */
public class PrimitiveCapability implements Serializable {
	
	/** The primitive. */
	private String primitive;
	
	/** The capability. */
	private String capability;

	/**
	 * Instantiates a new primitive capability.
	 *
	 * @param primitive the primitive
	 * @param capability the capability
	 */
	public PrimitiveCapability(String primitive, String capability) {
		this.primitive = primitive;
		this.capability = capability;
	}
	
	/**
	 * Gets the primitive.
	 *
	 * @return the primitive
	 */
	public String getPrimitive() {
		return primitive;
	}
	
	/**
	 * Sets the primitive.
	 *
	 * @param primitive the new primitive
	 */
	public void setPrimitive(String primitive) {
		this.primitive = primitive;
	}
	
	/**
	 * Gets the capability.
	 *
	 * @return the capability
	 */
	public String getCapability() {
		return capability;
	}
	
	/**
	 * Sets the capability.
	 *
	 * @param capability the new capability
	 */
	public void setCapability(String capability) {
		this.capability = capability;
	}
}
