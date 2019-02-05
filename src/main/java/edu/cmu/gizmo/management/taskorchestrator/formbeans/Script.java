package edu.cmu.gizmo.management.taskorchestrator.formbeans;

import java.io.File;

import edu.cmu.gizmo.management.taskmanager.scripttask.PlannedTasks;
import edu.cmu.gizmo.management.taskorchestrator.TaskElement;

public class Script implements TaskElement {

	private File taskFile;
	
	private String name;
	
	private PlannedTasks task;
	
	public Script(String filePath) {
		
		
	}
		
	
	@Override
	public Boolean load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean unload() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskElementType getType() {
		return TaskElementType.SCRIPT;
	}


	public String listName() {
		// TODO Auto-generated method stub
		return name;
	}

}
