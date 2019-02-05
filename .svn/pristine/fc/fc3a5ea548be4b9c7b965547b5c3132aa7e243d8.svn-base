package edu.cmu.gizmo.management.taskclient.observers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;


import edu.cmu.gizmo.management.taskclient.TaskClient;
import edu.cmu.gizmo.management.taskclient.observers.ICapabilityObserver.CapabilityUIStatus;

/**
 * @author majedalzayer
 *
 * This class has three main responsibilities: updating the task's UI model
 * when the task's capabilities start, updating the task's UI model
 * when the task's capabilities finish, and routing the capabilities output
 * to their associated capabilities observers. This class observes
 * the TaskClient associated with the current user's task
 */
public class TaskClientObserver
	implements Observer {

	/**
	 * The name of the task
	 */
	private String taskName;
	
	
	
	/**
	 * The status of the task
	 */
	private TaskStatusCommand taskStatus;
	
	/**
	 * Holds the value of the message that comes with any
	 * task status update. Used mostly to hold the reason
	 * for a rejected or cancelled task
	 */
	private String taskStatusMessage;
	
	private enum TaskStatusCommand {
		COMPLETED, CANCELLED, REJECTED 
	}
	
	/**
	 * The task client associated with the current user session.
	 */
	private TaskClient taskClient;
	
	
	/**
	 * The list of capabilities observers associated with the task
	 */
	private ArrayList<ICapabilityObserver> capabilityObservers;
	
	
	/**
	 * The constructor. Both the list of capabilities observers
	 * and the task client instance members are initialized
	 * 
	 * @param taskClient
	 */
	public TaskClientObserver(TaskClient taskClient)
	{
		System.out.println("[TaskClientObserver] " 
				+ "Initializing with task " + taskName);
		this.capabilityObservers = new ArrayList<ICapabilityObserver>();
		this.taskClient = taskClient;
	}
	
	/**
	 * @return the task name instance member
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public TaskStatusCommand getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatusCommand taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskStatusMessage() {
		return taskStatusMessage;
	}

	public void setTaskStatusMessage(String taskStatusMessage) {
		this.taskStatusMessage = taskStatusMessage;
	}

	public TaskClient getTaskClient() {
		return taskClient;
	}

	public void setTaskClient(TaskClient taskClient) {
		this.taskClient = taskClient;
	}

	/**
	 * @return the list of capabilities observers instance member
	 */
	public ArrayList<ICapabilityObserver> getCapabilityObservers() {
		return capabilityObservers;
	}


	/**
	 * @param capabilityObservers
	 */
	public void setCapabilityObservers(
			ArrayList<ICapabilityObserver> capabilityObservers) {
		this.capabilityObservers = capabilityObservers;
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(
	 * java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			
			/* Get the message object associated with the task client
			 * notification. Usually, it contains the command type and
			 * a number of parameters depending on the type of the command
			 */
			
			ConcurrentHashMap<Object,Object> msg = 
				(ConcurrentHashMap<Object,Object>) arg1;
			
			//Should be referencing the task client instance member.
			TaskClient observableTaskClient = (TaskClient) arg0;
			
			/*
			 * When the command is LOAD_UI, a new capability has been started
			 * and the UI of this capability needs to be loaded. For this,
			 * a new capability observer is instantiated with the needed
			 * parameters so that the capability UI can be loaded
			 */
			if (msg.get("cmd") == TaskClient.TaskClientCommands.LOAD_UI) {
				System.out.println("[TaskClientObserver] " 
						+ "got a LOAD_UI event . . .");
				
				/*
				 * Get the class name of the capability observer
				 */
				String className = (String) msg.get("ui.class");
				
				/*
				 * Get the UI file name of the capability, e.g.
				 * goToRoom.jsp
				 */
				String uiName = (String) msg.get("ui.display");
				
				/*
				 * Get the capability name
				 */
				String capabilityName = (String) msg.get("name");
				
				System.out.println("[TaskClientObserver] class name: "
						+ className);
				System.out.println("[TaskClientObserver] uiName: "
						+ uiName);
				System.out.println("[TaskClientObserver] capability name: " 
						+ capabilityName);
				
				int taskId = Integer.parseInt(msg.get("taskId").toString());
				int capabilityId = Integer.parseInt(
						msg.get("capabilityId").toString());
				
				System.out.println("[TaskClientObserver] task id: "
						+ taskId);
				System.out.println("[TaskClientObserver] capability id: "
						+ capabilityId);
				/*
				 * Create the capability observer by reflection
				 */
				ICapabilityObserver anObserver= 
					getObserverInstance(className);
				
				System.out.println("[TaskClientObserver] " 
						+ "instantiated the observer: " 
						+ anObserver.getStatus());
				/*
				 * instantiate the observer by the parameters passed with
				 * the task client notification message
				 */
				anObserver.setTaskId(taskId);
				anObserver.setCapabilityId(capabilityId);
				anObserver.setCapabilityUiDirectory(uiName);
				anObserver.setCapabilityName(capabilityName);
				
				//Add the observer to list of observers
				capabilityObservers.add(anObserver);
				System.out.println("[TaskClientObserver] " 
						+ "adding the observer to the list: "
						+ capabilityObservers.size());
				/*
				 * This if block deals with feeding the capability created
				 * with the output of the previous capability. This is where
				 * there is not user intervention needed to give the input.
				 * Currently, it's almost hardcoded and only specified for
				 * the GoToRoomDriveCapability.
				 */
				if (msg.containsKey("defaultInput")) {
					ConcurrentHashMap<Object, Object> defaultInput = 
							(ConcurrentHashMap<Object, Object>) 
								msg.get("defaultInput");
					
					anObserver.setDeafultInput(defaultInput);
				}
				
				/*
				 * When the command is UNLOAD_UI, the capability observer associated
				 * with this command is updated by changing its status to COMPLETED.
				 * This marks the end of the capability, but does not mean that its
				 * UI will be unloaded since it is unloaded only when the user
				 * chooses to continueTask()
				 */
			} else if (msg.get("cmd") == TaskClient.TaskClientCommands.UNLOAD_UI) {
				int taskId = Integer.parseInt(msg.get("taskId").toString());
				int capabilityId = Integer.parseInt(
						msg.get("capabilityId").toString());
				
				System.out.println("[TaskClientObserver] Got an unload event");
				System.out.println("[TaskClientObserver] task id: " + taskId);
				System.out.println("[TaskClientObserver] capability id: "
						+ capabilityId);
				/*
				 * Get the capability observer matching the passed taskId and
				 * capabilityId
				 */
				ICapabilityObserver currentCapabilityObserver = 
					getCapabilityObserverFromList(taskId, capabilityId);
				
				System.out.println("[TaskClientObserver] Got a capability" 
						+ " observer to change the status");
				System.out.println("[TaskClientObserver] " 
						+ currentCapabilityObserver.getStatus());

				// set its status to COMPLETED
				currentCapabilityObserver.setStatus(
						ICapabilityObserver.CapabilityUIStatus.COMPLETED);
				
				System.out.println("[TaskClientObserver] changed the status to: "
						+ currentCapabilityObserver.getStatus());
			  /*
			   * When the command is OUTPUT, the output object passed with the
			   * task client notification message is routed to the capability
			   * observer associated with the command.
			   */
			} else if (msg.get("cmd") == TaskClient.TaskClientCommands.OUTPUT) {
				int taskId = Integer.parseInt(msg.get("taskId").toString());
				int capabilityId = Integer.parseInt(
						msg.get("capabilityId").toString());
				
				ICapabilityObserver anObserver = 
					getCapabilityObserverFromList(taskId, capabilityId);
				
				ConcurrentHashMap<Object, Object> outputMap = 
					(ConcurrentHashMap<Object, Object>) msg.get("output");
				
				anObserver.setOutput(outputMap);
				
			} else if (msg.get("cmd") == 
					TaskClient.TaskClientCommands.CANCEL_TASK) {
				
				int taskId = Integer.parseInt(msg.get("taskId").toString());
				endAllCapabilities(taskId);
				
				taskStatus = TaskStatusCommand.CANCELLED;
				//Set status message
				taskStatusMessage = (String) msg.get("reason");
				
			} else if (msg.get("cmd") == 
					TaskClient.TaskClientCommands.REJECT_TASK) {
				taskStatus = TaskStatusCommand.REJECTED;
				taskStatusMessage = (String) msg.get("reason");
			} else if (msg.get("cmd") == 
					TaskClient.TaskClientCommands.COMPLETE_TASK) {
				taskStatus = TaskStatusCommand.COMPLETED;
				int taskId = Integer.parseInt(msg.get("taskId").toString());
				endAllCapabilities(taskId);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println("[TaskClientObserver] " 
				+ "exiting the update method peacefully!");
		
	}
	
	/**
	 * This method creates an instance of the class whose name is passed
	 * as a parameter. Reflection is used as the instantiation mechanism
	 *
	 * @param className
	 * @return an instance of the capability observer whose class
	 * name is passed
	 */
	private ICapabilityObserver getObserverInstance(String className) {
		
		Class anObserverClass = null;
		ICapabilityObserver anObserverInstance = null;
		
		try {
			anObserverClass = Class.forName(className);
			
			anObserverInstance = (ICapabilityObserver)
				anObserverClass.newInstance();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return anObserverInstance;
		
	}
	
	/**
	 * 
	 * @param taskId
	 * @param capabilityId
	 * @return the capability observer whose task id and capability id matches
	 * the ones that are passed as parameters to this method
	 */
	private ICapabilityObserver 
		getCapabilityObserverFromList(int taskId, int capabilityId) {
		
		ICapabilityObserver currentCapabilityObserver = null;
		
		/*
		 * The dummy capability observer is used to get the index of
		 * the capability observer whose task id and capability id matches
		 * the dummy's.
		 * 
		 * @see equals method of the AbstractCapabilityObserver
		 */
		ICapabilityObserver dummyObserver =
			getDummyCapabilityObserver(taskId, capabilityId);
		
		int currentCapabilityObserverIndex =
			capabilityObservers.indexOf(dummyObserver);
		if (currentCapabilityObserverIndex >= 0) {
			currentCapabilityObserver = capabilityObservers
				.get(currentCapabilityObserverIndex);
		}
		return currentCapabilityObserver;
	}
	
	
	/**
	 * @param taskId
	 * @param capabilityId
	 * @return the output object of the capability observer whose task
	 * id and capability id matches the ones passed as parameters
	 */
	public Object getOutputFromCapabilityObserver(
			int taskId, int capabilityId) {
		
		System.out.println("[TaskClientObserver] " 
				+ "Getting the output from the capability");
		System.out.println("[TaskClientObserver] taskId: " 
				+ taskId + " capId:" + capabilityId);
		ICapabilityObserver anObserver = 
			getCapabilityObserverFromList(taskId, capabilityId);
		if (anObserver != null) {
			System.out.println("[TaskClientObserver] got an observer: " 
					+ anObserver.getCapabilityUiDirectory());
			return anObserver.getOutput();
		}
		return null;
	}
	
	public Object getDefaultInputFromCapabilityObserver(
			int taskId, int capabilityId) {
		System.out.println("[TaskClientObserver] " 
				+ "Getting the default input from the capability");
		System.out.println("[TaskClientObserver] taskId: " 
				+ taskId + " capId:" + capabilityId);
		ICapabilityObserver anObserver = 
			getCapabilityObserverFromList(taskId, capabilityId);
		if (anObserver != null) {
			System.out.println("[TaskClientObserver] got an observer: " 
					+ anObserver.getCapabilityUiDirectory());
			return anObserver.getDefaultInput();
		}
		return null;
	}

	
	/**
	 * This method sets the status of the capability observer to
	 * COMPLETED
	 * @param taskId
	 * @param capabilityId
	 */
	public void setCapabilityAsEnded(int taskId, int capabilityId) {
		//replace later with getCapabilityObserverFromList()
		ICapabilityObserver dummuyObserver = 
			getDummyCapabilityObserver(taskId, capabilityId);
		int currentCapabilityObserverIndex = 
			capabilityObservers.indexOf(dummuyObserver);
		ICapabilityObserver currentCapabilityObserver = 
			capabilityObservers.get(currentCapabilityObserverIndex);
		
		currentCapabilityObserver.setStatus(CapabilityUIStatus.ENDED);
	}
	
	/**
	 * This method creates a dummy capability observer object
	 * populated with the task id and the capability
	 * id passed as parameters to this
	 * method
	 *
	 * @param taskId
	 * @param capabilityId
	 * @return a dummy capability observer object populated with the
	 * task id and the capability id passed as parameters to this
	 * method
	 */
	private ICapabilityObserver 
		getDummyCapabilityObserver(int taskId, int capabilityId) {
		
		GoToRoomObserver anObserver = new GoToRoomObserver();
		anObserver.setCapabilityId(capabilityId);
		anObserver.setTaskId(taskId);

		return anObserver;
	}
	
	private void endAllCapabilities(int taskId) {
		for (int count = 0; 
				 count < capabilityObservers.size(); 
				 count++) {
			ICapabilityObserver anObserver = capabilityObservers
					.get(count);
			
			if (anObserver.getTaskId() == taskId) {
				anObserver.setStatus(CapabilityUIStatus.ENDED);
			}
		}
	}
	

	public void setCapabilityObserverDefaultInput(int taskId, int capabilityId,
					ConcurrentHashMap<Object, Object> defaultInput) {
		ICapabilityObserver anObserver = getCapabilityObserverFromList(taskId, capabilityId);
		anObserver.setDeafultInput(defaultInput);
	}
}
