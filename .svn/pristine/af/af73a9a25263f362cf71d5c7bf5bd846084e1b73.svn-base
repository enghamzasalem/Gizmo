package edu.cmu.gizmo.management.taskorchestration.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;


import edu.cmu.gizmo.management.taskclient.actions.GizmoAction;
import edu.cmu.gizmo.management.taskorchestrator.TaskScriptOrchestrator.TaskType;
import edu.cmu.gizmo.management.taskorchestrator.TaskScriptOrchestrator;
import edu.cmu.gizmo.management.taskorchestrator.formbeans.OrchestrationStep;
import edu.cmu.gizmo.management.taskorchestrator.formbeans.OrchestrationStep.OrchestrationStatus;
import edu.cmu.gizmo.management.taskorchestrator.formbeans.Primitive;




/**
 * @author majedalzayer
 *
 * This class is a Struts {@link Action} that is responsible of
 * responding to the Http requests in order to orchestrate a task.
 * 
 */
public class OrchestrationAction extends GizmoAction{

	/**
	 * The orchestration step. It represents the user interface element
	 * that contains the list of primitives to be added and the list of task
	 * elements that the selected primitive can depend on.
	 */
	private OrchestrationStep orchStep;

	
	/** (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 *
	 * This is the Strut's Action main method. The body of this method is
	 * organized in the form of if statements, where each if statement
	 * checks for the value of the "action" request parameter. Different
	 * values of the "action" parameter represents different types
	 * of requests.
	 */
	@Override
	public String execute(){
		HttpServletRequest request = getServletRequest();
		String action = request.getParameter("action");
		
		if (action != null) {
			/**
			 * If the "init" action is requested, the task orchestrator is created
			 * and stored in the session, initialized, and the first
			 * orchestration step is loaded
			 */
			if (action.equals("init")) {
				TaskScriptOrchestrator orchestrator = new TaskScriptOrchestrator();
				request.getSession().setAttribute("orchestrator", orchestrator);
				
				orchestrator.initializeTakScript();
				orchestrator.addGroupedTask();
				
				orchStep = populateOrchestrationStep(0, null, orchestrator);
				
				/**orchestration step is streamed in JSON format. 
				 * See struts.xml to the see the configuration details
				 */
				return "streamResults";
			}
			
			/**
			 * When the "addTask" is requested, a call is made to the
			 * orchestrator to add a new Task entry to the task description
			 * based on the passed parameters
			 */
			if (action.equals("addTask")) {
				String primitiveName = request.getParameter("primitiveName");
				Integer dependsOnId = new Integer(request.getParameter("dependsOnID"));
				String dependsOnName = request.getParameter("dependsOnName");
				
				/** the taskType represents the type of orchestration of the next
				 * Task entry. It is either "SEQUENTIAL" or "PARALLEL". it is
				 * saved in the session and restored before calling 
				 * the orchestrator to add a new Task entry
				 */
				String futureTaskType = request.getParameter("taskType");
				
				/**
				 * Both the input and output maps are comma separated
				 * strings
				 */
				String inputMap = request.getParameter("inputMap");
				String outputMap = request.getParameter("outputMap");
				
				TaskScriptOrchestrator orchestrator = getOrchestrator(request);
				
				TaskType prevTaskType = (TaskType) request.getSession()
						.getAttribute("taskType");
				
				//Add the Task entry
				addTask(primitiveName, dependsOnId, 0, prevTaskType, orchestrator,
						inputMap, outputMap, dependsOnName);
				
				//Get a new orchestration step
				orchStep = populateOrchestrationStep(0, primitiveName, orchestrator);
				
				//Store the taskType for the next addition
				if (futureTaskType != null) {
					request.getSession().setAttribute("taskType", TaskType.valueOf(futureTaskType));
				}
				
				
				return "streamResults";
			}
			
			/**
			 * When the "deleteTask" action is called, the most recently added
			 * Task entry is simply deleted
			 */
			if(action.equals("deleteTask")) {
				String primitiveName = request.getParameter("primitiveName");
				TaskScriptOrchestrator orchestrator = getOrchestrator(request);
				
				orchestrator.deleteTask(0, primitiveName);
			}
			
			/**
			 * When the "save" action is called, the last Task Entry at the
			 * user interface is added and the whole file is saved under
			 * the name supplied by the user
			 */
			if (action.equals("save")) {
				String primitiveName = request.getParameter("primitiveName");
				Integer dependsOnId = new Integer(
						request.getParameter("dependsOnID"));
				String dependsOnName = request.getParameter("dependsOnName");
				
				String fileName = request.getParameter("fileName");
				String inputMap = request.getParameter("inputMap");
				String outputMap = request.getParameter("outputMap");
				
				TaskType prevTaskType = (TaskType) request.getSession()
						.getAttribute("taskType");
				
				TaskScriptOrchestrator orchestrator = getOrchestrator(request);
				
				addTask(primitiveName, dependsOnId, 0, prevTaskType, orchestrator,
						inputMap, outputMap, dependsOnName);
				
				boolean result = orchestrator.writeTestPlanWithFileName(fileName, getScriptsHomeDirectory());
				
				orchStep = new OrchestrationStep();
				
				//set the save status based on the saving result
				if (result == true) {
					orchStep.setSaveStatus(OrchestrationStatus.SUCCESS);
				} else {
					orchStep.setSaveStatus(OrchestrationStatus.FAILURE);
					orchestrator.deleteTask(0, primitiveName);
					
				}
				
				//Stream the saving result in JSON format
				return "streamResults"; 
				
			}
		}
		

		return SUCCESS;
	}


	/**
	 * @returnthe orchestration step. Needed for the JSON streaming plugin.
	 */
	public OrchestrationStep getOrchStep() {
		return orchStep;
	}


	/**
	 * @param orchStep
	 */
	public void setOrchStep(OrchestrationStep orchStep) {
		this.orchStep = orchStep;
	}
	
	/**
	 * @param the primitiveName based on which the primitives
	 * of the next orchestration step are loaded.
	 * @param the orchestrator object saved in the session
	 * @return a vector of primitives of the orchestration step
	 */
	private Vector<Primitive> getPrimitivesList(String primitiveName, 
			TaskScriptOrchestrator orchestrator) {
		
		Vector<String> primitivesStringList = 
				orchestrator.listPrimitives(primitiveName);
		Vector<Primitive> primitivesBeanList = 
				new Vector<Primitive>();
		
		//convert the returned strings list to primitives list
		Primitive aPrimitive;
		for (int count = 0; count < primitivesStringList.size(); count++) {
			String primitiveStr = primitivesStringList.get(count);
			aPrimitive = new Primitive();
			aPrimitive.setId(primitiveStr);
			aPrimitive.setName(primitiveStr);
			primitivesBeanList.add(aPrimitive);
		}
		
		return primitivesBeanList;
	}
	
	/**
	 * @param groupId. Pass 0 in all cases!
	 * @param the orchestrator object saved in the session
	 * @return a vector of the primitives that the selected primitive
	 * can depend on
	 */
	private Vector<Primitive> getPrimitiveDependency(int groupId, 
			TaskScriptOrchestrator orchestrator) {
		
		ConcurrentHashMap<String, String> dependencyList = 
				orchestrator.listDependsOnLeavesHashMap(groupId);
		Vector<Primitive> dependencyBeanList = 
				new Vector<Primitive>();
		
		//convert the returned hashMap to primitives list
		Primitive aPrimitive;
		Enumeration<String> dependencyListKeys = 
				dependencyList.keys();
		Enumeration<String> dependencyListValues = 
				dependencyList.elements();
		String id;
		String value;
		while (dependencyListKeys.hasMoreElements()) {
			id = dependencyListKeys.nextElement();
			value = dependencyListValues.nextElement();
			
			aPrimitive = new Primitive();
			aPrimitive.setId(id);
			aPrimitive.setName(value);
			
			dependencyBeanList.add(aPrimitive);
		}
		
		return dependencyBeanList;
		
	}
	
	/**
	 * @param groupId. Pass 0 in cases!
	 * @param the primitiveName based on which the primitives
	 * of the next orchestration step are loaded.
	 * @param the orchestrator object saved in the session
	 * @return an orchestration step that contains the list of
	 * primitives that could be added as a Task entry and the list
	 * of primitives that the selected primitive can depend on
	 */
	private OrchestrationStep populateOrchestrationStep(int groupId,
			String primitiveName, TaskScriptOrchestrator orchestrator) {
		
		Vector<Primitive> primitivesList = 
				getPrimitivesList(primitiveName, orchestrator);
		
		Vector<Primitive> primitiveDependenciesList = 
				getPrimitiveDependency(groupId, orchestrator);
		
		OrchestrationStep anOrchestrationStep = 
				new OrchestrationStep();
		
		anOrchestrationStep.setPrimitivesList(primitivesList);
		anOrchestrationStep
			.setPrimitiveDependenciesList(primitiveDependenciesList);
		
		return anOrchestrationStep;
	}
	
	/**
	 * @param The selected primitive name
	 * @param The ID of the primitive that the selected primitive
	 * depends on
	 * @param groupId. Pass 0 in all cases!
	 * @param The type of orchestration of this Task entry. 
	 * "SEQUENTIAL" or "PARALLEL"
	 * @param the orchestrator object saved in the session
	 * @param A comma separated string that contains the mapped inputs
	 * @param A comma separated string that contains the mapped outputs
	 * @param The name of the primitive that the selected primitive
	 * depends on
	 */
	private void addTask(String primitiveName, Integer dependsOnId, int groupId,
						 TaskType taskType, TaskScriptOrchestrator orchestrator,
						 String inputMap, String outputMap, String dependsOnName) {
		
		if (taskType == null) {
			taskType = TaskType.SEQUENTIAL;
		}
		
		//Add the Task entry
		orchestrator.addTask(taskType, 0, dependsOnId, primitiveName);
		
		//Map the inputs to the outputs
		if (!dependsOnName.isEmpty() && !inputMap.isEmpty() && !outputMap.isEmpty()) {
			String[] inputMapArray = inputMap.split(",");
			String[] outputMapArray = outputMap.split(",");
			
			//The mapping will only happen if the inputs match 
			if (inputMapArray.length == outputMapArray.length) {
				
				orchestrator.addInput(primitiveName, 0);
				orchestrator.addOutput(dependsOnName, 0);
				
				for (int count = 0; count < inputMapArray.length; count++) {
					orchestrator.addInputOutput(primitiveName, 0, 
							outputMapArray[count], inputMapArray[count]);
				}
			}
		}
	}
	
	/**
	 * @param The request object
	 * @return The stored orchestrator object in the session
	 */
	private TaskScriptOrchestrator 
		getOrchestrator(HttpServletRequest request) {
		
		return (TaskScriptOrchestrator) request.getSession()
				.getAttribute("orchestrator");
	}
	
	
	/**
	 * @return the web application root directory
	 * 
	 * This method is used to pass the root directory of the web application
	 * to the orchestrator to be able to read the configuration files
	 */
	private String getScriptsHomeDirectory() {
		HttpServletRequest request= getServletRequest();
		
		String path = request.getServletContext().getRealPath("/config.properties");
		File file = new File(path);
		return file.getParent() + "/WEB-INF/classes";
		
	}
	
	
}
