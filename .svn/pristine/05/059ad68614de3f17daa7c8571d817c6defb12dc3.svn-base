package edu.cmu.gizmo.management.taskorchestrator.formbeans;

import java.util.Vector;

/**
 * @author majedalzayer
 * 
 * This class a bean that stores the data of an orchestration step.
 * 
 * An orchestration step contains the list of primitives from which
 * the user can choose the primitive to be orchestrated and the list of primitives
 * that the selected primitive can depend on.
 * 
 * The saveStatus represents the result of saving the task script to be
 * presented to the user.
 *
 */
public class OrchestrationStep {

	/**
	 * The list of primitives from which the user can choose the primitive
	 * to be orchestrated
	 */
	private Vector<Primitive> primitivesList;
	
	
	/**
	 * the list of primitives that the selected primitive can depend on.
	 */
	private Vector<Primitive> primitiveDependenciesList;
	
	
	/**
	 * The saveStatus represents the result of saving the task script to be 
	 * presented to the user.
	 */
	private OrchestrationStatus saveStatus;
	
	/**
	 * An enumeration that represents the possible values of 
	 * the saving status of the task script
	 *
	 */
	public enum OrchestrationStatus {
		SUCCESS, FAILURE
	}
	
	
	/**
	 * @return the primitesList
	 */
	public Vector<Primitive> getPrimitivesList() {
		return primitivesList;
	}
	
	
	/**
	 * @param primitivesList
	 */
	public void setPrimitivesList(Vector<Primitive> primitivesList) {
		this.primitivesList = primitivesList;
	}
	
	
	/**
	 * @return the primitiveDependenciesList
	 */
	public Vector<Primitive> getPrimitiveDependenciesList() {
		return primitiveDependenciesList;
	}
	
	
	/**
	 * @param primitiveDependenciesList
	 */
	public void setPrimitiveDependenciesList(
			Vector<Primitive> primitiveDependenciesList) {
		this.primitiveDependenciesList = primitiveDependenciesList;
	}
	
	
	/**
	 * @return saveStatus
	 */
	public OrchestrationStatus getSaveStatus() {
		return saveStatus;
	}
	
	
	/**
	 * @param saveStatus
	 */
	public void setSaveStatus(OrchestrationStatus saveStatus) {
		this.saveStatus = saveStatus;
	}
	
	
	
}
