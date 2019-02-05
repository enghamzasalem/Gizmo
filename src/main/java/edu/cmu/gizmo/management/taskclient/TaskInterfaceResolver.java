/*
 * TaskInterfaceResolver.java Jul 10, 2012 1.0
 */
package edu.cmu.gizmo.management.taskclient;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;



/**
 * This class is responsible for loading.
 *
 * @author jsg
 */
public class TaskInterfaceResolver {
	
	/** The loader. */
	private final FileTaskLoader loader;

	/**
	 * Instantiates a new task interface resolver.
	 */
	public TaskInterfaceResolver() {
		loader = new FileTaskLoader();
	}

	/**
	 * This method should load the HTML/UI elements for a capability.
	 *
	 * @param capabilityName the capability name
	 * @return the object
	 */
	public Object resolve(final String capabilityName) {
		if (capabilityName.equals("Cobot3DashboardCapability"))
			return createDashboardUI();
		
		return capabilityName + "$>";
		// return loader.load(capabilityName);
	}

	/**
	 * Creates the dashboard ui.
	 *
	 * @return the j frame
	 */
	private JFrame createDashboardUI() { 
		//1. Create the frame.
		JFrame frame = new JFrame("CoBot 3 Dashboard");
		JLabel label = new JLabel("CoBot iz here!");
		frame.setSize(100, 100);
		
		
		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//3. Create components and put them in the frame.
		//...create emptyLabel...
		frame.getContentPane().add(label, BorderLayout.CENTER);

		//4. Size the frame.
		frame.pack();

		//5. Show it.
		return frame;
	}
	
	/**
	 * this is currently a simple CLI interface.
	 *
	 * @author jsg
	 */
	private class FileTaskLoader {
		
		/** The task ui dir. */
		private final String TASK_UI_DIR = "C:\\Users\\jsg\\tmp\\gizmoUI\\";

	};

}
