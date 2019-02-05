package edu.cmu.gizmo.management.taskorchestrator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import edu.cmu.gizmo.management.dataaccess.TaskCapabilityDBAccess;
import edu.cmu.gizmo.management.dataaccess.jdbc.TaskCapabilityDBAccessImpl;
import edu.cmu.gizmo.management.util.ManifestReader;

/**
 * CapabilityResourceTable keeps track of the capability to resource
 * and resource to capability key-value match in hashtables. 
 * 
 * keyValueLookup and capabilityLookup would be useful to find out 
 * which capabilities are sharing the resource. 
 * 
 * inputLookup holds <key:capability name>-<value:inputVector>. 
 * outputLookup holds <key:capability name>-<value:outputVector>.
 * 
 * Overview of Data Structure
 * capabilityLookup - ConcurrentHashMap
 * ----------------------------
 * |    KEY     |    VALUE    |
 * ----------------------------
 * | capability | "key,value" |
 * ----------------------------
 * | capability | "key,value" |
 * ----------------------------
 * 
 * keyValueLookup - ConcurrentHashMap
 * ----------------------------
 * |    KEY     |    VALUE    |
 * ----------------------------
 * | "key,value" | capability |
 * ----------------------------
 * | "key,value" | capability |
 * ----------------------------
 * 
 * @author Seong Lee
 * @see TaskCapabilityDBAccess
 * @see ManifestReader
 */
public class CapabilityResourceTable {
	/** keyValue-capability hashtable **/
	private Map<String, Vector<String>> keyValueLookup = null;
	
	/** capability-keyValue hashtable **/
	private Map<String, Vector<String>> capabilityLookup = null;

	/** capability-input hashtable **/
	private Map<String, Vector<String>> inputLookup = null;
	
	/** capability-output hashtable **/
	private Map<String, Vector<String>> outputLookup = null;

	/** A complete list of all of the capabilities in primitive_capability table **/
	private Vector<String> capabilityList = null;

	/** TaskCapabilityDBAccess is needed due to the primitive name provided in a Task Script **/
	private TaskCapabilityDBAccess dbo = null;
	
	/** ManifestReader is used to read a configuration file for capability **/
	private ManifestReader manifestReader = null;

	/**
	 * Returns manifest reader
	 * @return the references to the manifest reader object
	 */
	public ManifestReader getManifestReader() {
		return manifestReader;
	}

	/**
	 * Returns the inputLookup hashmap. An inputLookup holds <key:capability name>-<value:inputVector>. 
	 * @return the inputLookup hahsmap 
	 */
	public Map<String, Vector<String>> getInputLookup() {
		return inputLookup;
	}

	/**
	 * Returns the outputLookup hashmap. An outputLookup holds <key:capability name>-<value:outputVector>.
	 * @return the outputLookup hashmap 
	 */
	public Map<String, Vector<String>> getOutputLookup() {
		return outputLookup;
	}

	/**
	 * Returns the hashmap of "parameter,value" string to capability 
	 * @return the hashmap of "parameter,value" string to capability
	 */
	public Map<String, Vector<String>> getKeyValueLookup() {
		return keyValueLookup;
	}

	/**
	 * Returns the hashmap of capability to "parameter,value" string
	 * @return the hashmap of capability to "parameter,value" string
	 */
	public Map<String, Vector<String>> getCapabilityLookup() {
		return capabilityLookup;
	}

	/**
	 * Constructor initializes all the resources needed
	 */
	public CapabilityResourceTable() {
		dbo = new TaskCapabilityDBAccessImpl();
		capabilityList = dbo.listPrimitives();
		keyValueLookup = new ConcurrentHashMap<String, Vector<String>>();
		capabilityLookup = new ConcurrentHashMap<String, Vector<String>>();
		inputLookup = new ConcurrentHashMap<String, Vector<String>>();
		outputLookup = new ConcurrentHashMap<String, Vector<String>>();
		manifestReader = new ManifestReader();
	}
	
	/**
	 * CreateDataStructure creates a data structure for keeping the resource contention
	 * Below is the sequence of execution:
	 * 1. Get a capability list from db
	 * 2. Foreach capability do the following:
	 * 3. Foreach MF file:
	 * 4. add capability, "key,value"
	 * 5. add "key,value", capability
	 */
	public void createDataStructure() {
		for(int i=0; i < capabilityList.size(); ++i) {
			String capabilityName = (String)capabilityList.get(i);
			ConcurrentHashMap<Object, Object> capabilityHashMap = 
					manifestReader.readCapabilityManifest(capabilityName);

			for(Map.Entry<Object, Object> entry: capabilityHashMap.entrySet())
	        {
				String key = (String)entry.getKey();
				String value = (String)capabilityHashMap.get((Object)key);
				String keyValue = null;
				if(!key.equals("ui.display") && !key.equals("ui.class") && 
						!key.equals("capability.directory")) {
					keyValue = "[" + key + "," + value + "]";
					Vector<String> temp = new Vector<String>();
					
					if(key.equals("output.name")) {
					
						// capability exists
						if(outputLookup.containsKey(capabilityName)) {
							temp = outputLookup.get(capabilityName);
							temp.add(value);
							outputLookup.put(capabilityName, temp);
						}
						// capability does not exist
						else {
							temp = new Vector<String>();
							temp.add(value);
							outputLookup.put(capabilityName, temp);
						}					
					} 
					else if(key.equals("parameter.name")) {
						
						// capability exists
						if(inputLookup.containsKey(capabilityName)) {
							temp = inputLookup.get(capabilityName);
							temp.add(value);
							inputLookup.put(capabilityName, temp);
						}
						// capability does not exist
						else {
							temp = new Vector<String>();
							temp.add(value);
							inputLookup.put(capabilityName, temp);
						}	
					}					
				}
	        }
			for(Map.Entry<Object, Object> entry: capabilityHashMap.entrySet())
	        {
				String key = (String)entry.getKey();
				String value = (String)capabilityHashMap.get((Object)key);
				String keyValue = null;
				if(!key.equals("ui.display") && !key.equals("ui.class") && 
						!key.equals("capability.directory") && !key.equals("output.name") &&
						!key.equals("parameter.name")) {
					keyValue = "[" + key + "," + value + "]";
					Vector<String> temp = new Vector<String>();
				
					// capability exists
					if(capabilityLookup.containsKey(capabilityName)) {
						temp = capabilityLookup.get(capabilityName);
						temp.add(keyValue);
						capabilityLookup.put(capabilityName, temp);
					}
					// capability does not exist
					else {
						temp = new Vector<String>();
						temp.add(keyValue);
						capabilityLookup.put(capabilityName, temp);
					}
				
					// keyValue exists
					if(keyValueLookup.containsKey(keyValue)) {
						temp = keyValueLookup.get(keyValue);
						temp.add(capabilityName);
						keyValueLookup.put(keyValue, temp);
					}
					// keyValue does not exist
					else {
						temp = new Vector<String>();
						temp.add(capabilityName);
						keyValueLookup.put(keyValue, temp);
					}
				}
			}
		}
	}
	
	/**
	 * The returnCapabilityInput function returns the list of inputs for a capability
	 * 
	 * @param capabilityName the name of a capability
	 * @return the list of inputs for the capability
	 */
	public Vector<String> returnCapabilityInput(String capabilityName) {
		
		for(Map.Entry<String, Vector<String>> entry: inputLookup.entrySet()) {
		
			String key = (String)entry.getKey();
		
			if(key.toLowerCase().equals(capabilityName.toLowerCase())) {
				Vector<String> value = (Vector<String>)inputLookup.get((Object)key);
				return value;	
			}
		}
		return null;
	}
	
	/**
	 * The returnCapabiltyOutput function returns the list of outputs for a capability
	 * 
	 * @param capabilityName the name of a capability
	 * @return
	 */
	public Vector<String> returnCapabilityOutput(String capabilityName) {
		for(Map.Entry<String, Vector<String>> entry: outputLookup.entrySet()) {
			String key = (String)entry.getKey();
			if(key.toLowerCase().equals(capabilityName.toLowerCase())) {
				Vector<String> value = (Vector<String>)outputLookup.get((Object)key);
				return value;	
			}
		}
		return null;
	}

	/**
	 * Returns the number of actual capabilities available for the parameter and its value
	 * 
	 * @param keyValue the string of input parameter and value
	 * @return the number of capabilities available for the keyValue
	 */
	public int returnNumOfCapabilityForKeyValue(String keyValue) {
		for(Map.Entry<String, Vector<String>> entry: keyValueLookup.entrySet()) {
			String key = (String)entry.getKey();
			if(key.equals(keyValue)) {
				Vector<String> value = (Vector<String>)keyValueLookup.get((Object)key);
				return value.size();			
			}
		}

		return 0;
	}
	
	/**
	 * Returns the list of the capabilities available for the parameter and its value
	 * 
	 * @param keyValue the string of parameter name and its value
	 * @return the list of capabilities
	 */
	public Vector<String> returnListOfCapabilitiesUsingSameResource(String keyValue) {
		for(Map.Entry<String, Vector<String>> entry: keyValueLookup.entrySet()) {
			String key = (String)entry.getKey();
			if(key.toLowerCase().equals(keyValue.toLowerCase())) {
				Vector<String> value = (Vector<String>)keyValueLookup.get((Object)key);
				return value;	
			}
		}
		return null;
	}
	
	/**
	 * Returns the list of resources(parameters) used by the capability
	 * 
	 * @param capabilityName the name of the capability
	 * @return the list of resources(parameters)
	 */
	public Vector<String> returnListOfResourcesCapabilityIsUsing(String capabilityName) {
		for(Map.Entry<String, Vector<String>> entry: capabilityLookup.entrySet()) {
			String key = (String)entry.getKey();
			if(key.toLowerCase().equals(capabilityName.toLowerCase())) {
				Vector<String> value = (Vector<String>)capabilityLookup.get((Object)key);
				return value;	
			}
		}
		return null;
	}

	/**
	 * For a debugging purpose, prints out the capabilityLookup hashmap
	 * @return null
	 */
	public Vector<String> printCapabilityLookup() {
		System.out.println("Capability PrintOut");
		for(Map.Entry<String, Vector<String>> entry: capabilityLookup.entrySet()) {
			String key = (String)entry.getKey();
			System.out.println("key: " + key);
			Vector<String> value = (Vector<String>)capabilityLookup.get((Object)key);
			for(int i=0; i < value.size(); ++i) {
				System.out.println("value" + i + ": " + value.get(i));				
			}
		}
		return null;
	}
	
	/**
	 * For a debugging purpose, prints out keyValueLookup hashmap
	 * @return null
	 */
	public Vector<String> printKeyValueLookup() {
		System.out.println("KeyValue PrintOut");
		for(Map.Entry<String, Vector<String>> entry: keyValueLookup.entrySet()) {
			String key = (String)entry.getKey();
			System.out.println("key: " + key);
			Vector<String> value = (Vector<String>)keyValueLookup.get((Object)key);
			for(int i=0; i < value.size(); ++i) {
				System.out.println("value" + i + ": " + value.get(i));				
			}
		}
		return null;
	}

	/**
	 * For a debugging purpose, prints out inputLookup hashamp
	 * @return null
	 */	
	public Vector<String> printInputLookup() {
		System.out.println("Input PrintOut");
		for(Map.Entry<String, Vector<String>> entry: inputLookup.entrySet()) {
			String key = (String)entry.getKey();
			System.out.println("key: " + key);
			Vector<String> value = (Vector<String>)inputLookup.get((Object)key);
			for(int i=0; i < value.size(); ++i) {
				System.out.println("value" + i + ": " + value.get(i));				
			}
		}
		return null;
	}
	
	/**
	 * For a debugging purpose, prints out outputLookup hashmap
	 * @return null
	 */
	public Vector<String> printOutputLookup() {
		System.out.println("Output PrintOut");
		for(Map.Entry<String, Vector<String>> entry: outputLookup.entrySet()) {
			String key = (String)entry.getKey();
			System.out.println("key: " + key);
			Vector<String> value = (Vector<String>)outputLookup.get((Object)key);
			for(int i=0; i < value.size(); ++i) {
				System.out.println("value" + i + ": " + value.get(i));				
			}
		}
		return null;
	}

}
