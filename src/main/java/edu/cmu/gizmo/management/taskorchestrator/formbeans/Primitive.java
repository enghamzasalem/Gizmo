package edu.cmu.gizmo.management.taskorchestrator.formbeans;

/**
 * @author majedalzayer
 * 
 * This class is a bean that holds the primitive data. A primitive bean 
 * contains name of the primitive and the id of the primitive.
 *
 */
public class Primitive {

	/**
	 * The name of the primitive
	 */
	private String name;
	
	
	/**
	 * The id of the primitive
	 */
	private String id;
	
	
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	
	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
}
