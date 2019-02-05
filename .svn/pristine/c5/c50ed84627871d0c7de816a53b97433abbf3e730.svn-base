/*
 * ManifestReader.java Jul 12, 2012 1.0
 */



package edu.cmu.gizmo.management.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This is the Reader for <code>Capability</code> configuration. It is used by 
 * <code>ScriptTaskStrategy</code> to read in the properties of the <code>Capability</code>.
 * SYSTEM_MANIFEST describes the configuration for the system and 
 * CAPABILITY_DIRECTORY describes the configuration for the capability. 
 * 
 * @version 1.0
 * @author Seong Lee
 * 
 * @see ScriptTaskStrategy
 * @see Capability
 */
public class ManifestReader {
	/** system configuration file **/
	public static final String SYSTEM_MANIFEST = "config.properties";
	
	/** capability configuration file **/
	public static final String CAPABILITY_DIRECTORY = "capability.directory";
	
	/** The configuration. */
	ConcurrentHashMap<Object, Object> configuration = new ConcurrentHashMap<Object, Object>(0);

	/**
	 * Read configuration file for the system and creates a hashmap of the configuration.
	 *
	 * @return the hash map
	 */
	public ConcurrentHashMap<Object, Object> readConfig() {
    	Properties prop = new Properties();
    	Set keySet;
    	String key = null, value = null;

    	try {
    		File currentDirectory = new File(".");

    		// NEW CODE NOT TESTED YET
    		ClassLoader loader = this.getClass().getClassLoader();    		
    		InputStream in = loader.getResourceAsStream("config.properties");
    		InputStreamReader is = new InputStreamReader(in);
    		StringBuilder sb=new StringBuilder();
    		BufferedReader br = new BufferedReader(is);
    		String read = br.readLine();

    		while(read != null) {
    		    //System.out.println(read);
    		    sb.append(read);
    		    read =br.readLine();
    		}
    		System.out.println("Property: " + sb.toString());

    		String[] str_array = sb.toString().split("=");
    		key = str_array[0]; 
    		value = str_array[1];
    		value.replace(".", "/");
    		System.out.println("value: " + value);
    		configuration.put(key, value);

    		
    		
    		System.out.println(configuration.get(CAPABILITY_DIRECTORY) + "/" + 
					"GoToRoomDriveCapability" + ".mf");

    		
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
    	return configuration;
	}	

	/**
	 * Read configuration file for a capability (capability manifest file).
	 *
	 * @param capabilityName the capability name
	 * @return the hash map
	 */
	public ConcurrentHashMap<Object, Object> readCapabilityManifest(String capabilityName) {
		readConfig();
		
		ConcurrentHashMap<Object, Object> hm = new ConcurrentHashMap<Object, Object>(0);
    	Properties prop = new Properties();
    	Set keySet;
    	
    	String key = null, value = null;
    	String capabilityDirectory = null;

    	try {

    		
    		ClassLoader loader = this.getClass().getClassLoader();    		
    		InputStream in = loader.getResourceAsStream(configuration.get(CAPABILITY_DIRECTORY) + "/" + 
					capabilityName + ".mf");
    		InputStreamReader is = new InputStreamReader(in);
    		StringBuilder sb=new StringBuilder();
    		BufferedReader br = new BufferedReader(is);
    		String read = br.readLine();

			System.out.println("---Start ManifestReader---");
    		while(read != null) {
    		    System.out.println(read);

        		System.out.println("Property: " + sb.toString());
        		if(read.contains("=")) {
        			String[] str_array = read.split("=");
        			key = str_array[0]; 
        			value = str_array[1];
        			value.replace(".", "/");
        			System.out.println("value: " + value);
        			hm.put(key, value);
        		}
    		    
    		    
    		    read =br.readLine();
    		}
			System.out.println("---End ManifestReader---");



    		
    		
     		
    		
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
		return hm;
	}
}
