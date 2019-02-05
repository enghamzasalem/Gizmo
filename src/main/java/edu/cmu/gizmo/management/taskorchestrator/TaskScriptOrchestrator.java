/*
 * 2012-08-03: Seong. Initial version
 * 2012-10-03: Jeff. Unified task elements 
 */

package edu.cmu.gizmo.management.taskorchestrator;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.cmu.gizmo.management.dataaccess.TaskCapabilityDBAccess;
import edu.cmu.gizmo.management.dataaccess.jdbc.TaskCapabilityDBAccessImpl;
import edu.cmu.gizmo.management.taskmanager.TaskResolver;
import edu.cmu.gizmo.management.taskmanager.TaskScriptParser;
import edu.cmu.gizmo.management.taskmanager.scripttask.PlannedTasks;
import edu.cmu.gizmo.management.util.ManifestReader;

/**
 * TaskScriptOrchestrator provides the functionality to create a task script
 * from scratch and also to modify the existing task script.
 * 
 * Here is the sequence of the tasks that can be called to create a task script:
 * 1. Create a PlannedTask Create barebone script with <JobDescription> 2. Add
 * <Tasks> tag into the script 3. Add a groupedTask Add <GroupedTask> tag into
 * the <Tasks> tag 4. Add a task in groupedTask Add <Task> tag into the
 * <GroupedTask> tag 5. Add Flag 6. Add Action 7. Add another task in
 * groupedTask If not first, specify dependency OR type of task Repeat from 4-6
 * 8. Add a dependency of task Set Dependency 9. Verify Againt Format 10.
 * RunSimulation
 * 
 * @version 1.0
 * @author Seong Lee
 * 
 * @see TaskResolver
 * @see TaskCapabilityDBAccess
 * @see CapabilityResourceTable
 * @see TaskScriptParser
 */
public class TaskScriptOrchestrator {
	/**
	 * Both Task and GroupedTask can have TaskType 
	 */
	public static enum TaskType {
		PARALLEL,
		SEQUENTIAL
	};

	/**
	 * Both Task and GroupedTask can use TaskSeq to change the order of the sequence 
	 */
	public static enum TaskSeq {
		UP,
		DOWN
	};
	
	/**
	 * Document object is created for task script generation 
	 */	
	private Document dom = null;
	
	/** 
	 * TaskResolver is used for the primitive name 
	 */
	private TaskResolver tr = null;
	
	/** 
	 * TaskCapabilityDBAccess is used to retrieve capability name 
	 */
	private TaskCapabilityDBAccess dbo = null;
	private CapabilityResourceTable capabilityResourceTable = null;
	private TaskScriptParser taskScriptParser = null;

	/**
	 *  In constructor, it initiates all the objects needed to create the task
	 */
	public TaskScriptOrchestrator() {
		taskScriptParser = new TaskScriptParser();
		tr = new TaskResolver();
		dbo = new TaskCapabilityDBAccessImpl();
		capabilityResourceTable = new CapabilityResourceTable();
		capabilityResourceTable.createDataStructure();
		
		try {
			// 1. Get an instance of factory
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			// 2. Get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// 3. Create an instance of DOM
			dom = db.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The initializeTaskScript function sets up the beginning of the task script
	 * such as header tags(e.g. <JobDescription><Tasks>)
	 */	
	public void initializeTakScript() {
		// Create and add a root element
		Element jobDescriptionElement = dom.createElement("JobDescription");
		dom.appendChild(jobDescriptionElement);

		Element root = dom.getDocumentElement();
		Element tasksElement = dom.createElement("Tasks");
		root.appendChild(tasksElement);

	}

	/**
	 * The writeTestPlanWithFileName function writes the task plan in a XML file
	 * with the name the user specifies.
	 * 
	 * @param fileName
	 *            the file name/task name the user wants the plan to be written
	 */
	public boolean writeTestPlanWithFileName(String fileName, String path) {
		boolean retVal = false;

		try {
			/**
			 * Write the content into xml file
			 */
			ManifestReader mf = capabilityResourceTable.getManifestReader();
			ConcurrentHashMap<Object, Object> map = mf.readConfig();
			String directoryToWrite = (String)map.get("capability.directory");
			//String directoryAndFileName = directoryToWrite + "/" + fileName;

			String directoryAndFileName = path + "/" + fileName;
			
    		ClassLoader loader = this.getClass().getClassLoader();    		

    		try {
    			//InputStream in = loader.getResourceAsStream(directoryAndFileName);
    			InputStream in = loader.getResourceAsStream(fileName);
        		InputStreamReader is = new InputStreamReader(in);
    		}
    		catch(Exception e) {
    			retVal = true;
    		}
			
			if(retVal) {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(dom);
				StreamResult result = new StreamResult(new File(directoryAndFileName));
		    		
	    		//StreamResult result = new StreamResult(fileWriteTo);
				transformer.transform(source, result);
			}
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}

		return retVal;
	}
	
	/**
	 * The writeTestPlan function writes the DOCUMENT object created. The
	 * writeTestPlan function can write the outputs to System.out or file or any
	 * other streaming output. The writeTestPlan will be kicked off when SAVE
	 * button is clicked on the UI
	 * 
	 * @param outputStream
	 *            Type of stream the user wants DOCUMENT object to be written
	 */
	public void writeTestPlan(OutputStream outputStream) {
		try {
			/**
			 * Write the content into xml file
			 */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			//StreamResult result = new StreamResult(new File("C:\\file.xml"));

			StreamResult result = new StreamResult(outputStream);

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}

	/**
	 * The addGroupedTask adds <GroupedTask> tag to the script
	 * This will be called when a new group task is defined
	 */
	public void addGroupedTask() {
		Element root = dom.getDocumentElement();
		Node node = root.getChildNodes().item(0);

		Node groupedTaskNode = root.getChildNodes().item(0);

		int numOfGroupedTask=0;
		for(int i = 0; i < groupedTaskNode.getChildNodes().getLength(); ++i) {
			if(groupedTaskNode.getChildNodes().item(i).getNodeName().equals("GroupedTask")) {
				++numOfGroupedTask;
			}
		}

		Element groupedTask = dom.createElement("GroupedTask");
		node.appendChild(groupedTask);

		Element groupedTaskId = dom.createElement("GroupedTaskID");
		groupedTaskId.appendChild(dom.createTextNode(new Integer(numOfGroupedTask).toString()));
		groupedTask.appendChild(groupedTaskId);
	}	

	/**
	 * listGroupedID returns all groupIDs that are defined so far
	 * @return A complete list of groupIDs
	 */
	public Vector<String> listGroupID() {
		Vector<String> groupIDList = new Vector<String>();
				
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);
		NodeList tasksNodeList = tasksNode.getChildNodes();
		
		for(int i =0; i < tasksNodeList.getLength(); ++i) {
			Node temp = tasksNodeList.item(i);			
			if(temp.getNodeName().equals("GroupedTask")) {
				
				Node groupedTaskId = temp.getChildNodes().item(0);
				groupIDList.add(groupedTaskId.getTextContent());
			}
		}
		return groupIDList;
	}

	/**
	 * The listDependsOn function lists all of the possible task IDs that can be dependent on 
	 * within a specific group task.
	 * @param groupedTaskNum group task number that can be used for complete list of task IDs
	 * @return A complete list of all task IDs in the groupedTask 
	 * with groupedTaskNum as groupedTaskID
	 */
	public Vector<String> listDependsOn(Integer groupedTaskNum) {
		// List all possible task name under than groupID
		Vector<String> dependsOnList = new Vector<String>();

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		for(int i=0; i < taskNodeList.getLength(); ++i) {
			Node taskNode = groupedTaskNode.getChildNodes().item(i); // Task
			if(taskNode.getNodeName().equals("Task")) {
				dependsOnList.add(taskNode.getChildNodes().item(1).getTextContent());				
			}				
		}		

		return dependsOnList;		
	}

	/**
	 * The listDependsOnLeaves function lists all of the possible task IDs that
	 * can be dependent on. MODIFICATION of listDependsOn function
	 * 
	 * 1. Add the taskID 2. Check whether it has a dependency 3. Remove the
	 * dependency ID
	 * 
	 * @param groupedTaskNum
	 *            groupedTaskNumber that the search will be done Currently, it
	 *            is assumed to be 0 by default. There will only be 1 taskID
	 * @return a complete list of all possible taskIDs
	 */
	public Vector<String> listDependsOnLeaves(Integer groupedTaskNum) {
		Vector<String> dependsOnList = new Vector<String>();		
		LinkedList<String> dependsOnLL=new LinkedList<String>();

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		for(int i=0; i < taskNodeList.getLength(); ++i) {
			Node taskNode = groupedTaskNode.getChildNodes().item(i); // Task
			if(taskNode.getNodeName().equals("Task")) {
				dependsOnLL.add(taskNode.getChildNodes().item(0).getTextContent());

				for(int k=0; k < taskNode.getChildNodes().getLength(); ++k) {
					if(taskNode.getChildNodes().item(k).getNodeName()
							.toLowerCase().equals("dependson")) {
						//remove from LL
						String attributeId = ((Element)taskNode.getChildNodes().item(k)).getAttribute("id");
						dependsOnLL.remove(attributeId);
					}
				}
			}
		}

		dependsOnList.addAll(dependsOnLL);
		return dependsOnList;
	}

	/**
	 * The listDependsOnLeavesHashMap function returns all possible tasks that can be modified
	 * 
	 * @param groupedTaskNum
	 * @return
	 */
	public ConcurrentHashMap<String, String> listDependsOnLeavesHashMap(Integer groupedTaskNum) {
		ConcurrentHashMap<String, String> idName = new ConcurrentHashMap<String, String>();

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();

		NodeList taskNodeList = groupedTaskNode.getChildNodes();

		for(int i=0; i < taskNodeList.getLength(); ++i) {
			Node taskNode = groupedTaskNode.getChildNodes().item(i); // Task
			if(taskNode.getNodeName().equals("Task")) {
				String id = taskNode.getChildNodes().item(0).getTextContent();
				String name = taskNode.getChildNodes().item(1).getTextContent();
				idName.put(id, name);
				
				for(int k=0; k < taskNode.getChildNodes().getLength(); ++k) {
					if(taskNode.getChildNodes().item(k).getNodeName()
							.toLowerCase().equals("dependson")) {
						//remove from LL
						String attributeId = ((Element)taskNode.getChildNodes().item(k)).getAttribute("id");
						idName.remove(attributeId);
					}
				}
			}
		}
				
		return idName;
	}
	
	/**
	 * The listAllScripts function lists all of the scripts defined in the file system
	 */
	public void listAllScripts() {
		String directory = System.getProperty("user.dir");
		File dir = new File(directory);

		String[] children = dir.list();
		if (children == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i<children.length; i++) {
		        // Get filename of file or directory
		        String filename = children[i];
		    	if(filename.endsWith("xml") && !filename.equals("pom.xml"))
		    		System.out.println(filename.substring(0, filename.lastIndexOf(".xml")));
		    }
		}		
	}
	
	/**
	 * This method will list all the task elements from various places on the 
	 * system. 
	 * 
	 * @return a <code>Vector</code> with all the task elements
	 */
	public Vector<TaskElement> listTaskElements() {
		return null;
	}

	/**
	 * The loadExistingTaskDocument function parses and loads the already defined 
	 * task for modification
	 * @param fileName file name of the task script
	 * @return parsed document object of the task script
	 */
	public Document loadExistingTaskDocument(String fileName) {
		taskScriptParser.setTaskPlan(fileName);
		taskScriptParser.parse();
		dom = taskScriptParser.getDocument();
		return dom;
	}
	
	/**
	 * The loadExistingTaskPlannedTaskObject function parses and load the already 
	 * defined task for modification. The only difference between the 
	 * loadExistingTaskPlannedTaskObject and loadExistingTaskDocuemnt functions is that 
	 * loadExistingTaskPlannedTaskObject function returns PlannedTasks object
	 * which is the data structure that back-end is using.
	 * @param fileName file name for the task script
	 * @return plannedTasks data structure used in back-end
	 */
	public PlannedTasks loadExistingTaskPlannedTaskObject(String fileName) {
		taskScriptParser.setTaskPlan(fileName);
		taskScriptParser.parse();	
		PlannedTasks plannedTasks = taskScriptParser.getPlannedTasks();
		return plannedTasks;
	}

	/**
	 * The deleteGroupedTaskDependency function removed the groupedTask with groupedTaskID as groupedTsakNum
	 * @param groupedTaskNum
	 */
	public void deleteGroupedTaskDependency(Integer groupedTaskNum) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);
		
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();
		
		for(int i =0; i < groupedTaskNodeList.getLength(); ++i) {
			Node temp = groupedTaskNodeList.item(i);
			if(temp.getNodeName().equals("GroupedTaskDependsOn"))
				groupedTaskNode.removeChild(temp);			
		}
	}
	
	/**
	 * The deleteTaskDependency function removed the task dependency of a task
	 * with taskNum in a groupedTask with groupedTaskNum
	 * 
	 * @param groupedTaskNum
	 *            defines the groupedTaskNum of the the GroupedTask
	 * @param taskNum
	 *            defines the taskNum of the Task
	 */
	public void deleteTaskDependency(Integer groupedTaskNum, Integer taskNum) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);
		
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		Node taskNode = groupedTaskNode.getChildNodes().item(taskNum.intValue() + 1); // Task
		Node taskIdNode = taskNode.getChildNodes().item(0);
		NodeList taskNodeDescriptionList = taskNode.getChildNodes();		
		
		for(int i =0; i < taskNodeDescriptionList.getLength(); ++i) {
			Node temp = taskNodeDescriptionList.item(i);
			System.out.println(temp.getNodeName());
			if(temp.getNodeName().equals("DependsOn"))
				taskNode.removeChild(temp);			
		}		
	}


	
	
	
	
	
	
	/**
	 * The deleteTask function removes the <Task> block with TaskName as
	 * taskName in a GroupedTask with a groupedTaskNum
	 * 
	 * @param groupedTaskNum
	 *            is the id of a GroupedTask
	 * @param taskName
	 *            is the string name of a Task
	 */
	public void deleteTask(Integer groupedTaskNum, String taskName) {
		// 1. deal with sequence
		// 2. deal with dependency
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		boolean stop = false;
		for(int i=0; ((i + 1) < taskNodeList.getLength()) && !stop; ++i) {
			Node taskNode = groupedTaskNode.getChildNodes().item(i + 1); // Task
			System.out.println("taskNode.getNodeName(): " + taskNode.getNodeName());
			Node taskNameNode = taskNode.getChildNodes().item(1);
			System.out.println("taskNameNode.getNodeName(): " + taskNameNode.getNodeName());
			
			if(taskNameNode.getTextContent().equals(taskName.toString())) {
				groupedTaskNode.removeChild(taskNode);
			}
			
		}
			 
	}
	
	/**
	 * The deleteTask function removes the Task with taskNum in a GroupedTask
	 * with id as gorupedTaskNum
	 * 
	 * @param groupedTaskNum
	 *            the id of a GroupedTask
	 * @param taskNum
	 *            the id of a Task
	 */
	public void deleteTask(Integer groupedTaskNum, Integer taskNum) {
		// 1. deal with sequence
		// 2. deal with dependency
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		Node taskNode = groupedTaskNode.getChildNodes().item(taskNum.intValue() + 1); // Task
		Node taskIdNode = taskNode.getChildNodes().item(0);
		
		if(taskIdNode.getTextContent().equals(taskNum.toString())) {
			//root.getChildNodes().item(0).getChildNodes().item(groupedTaskNum.intValue()).removeChild(taskNode);
			groupedTaskNode.removeChild(taskNode);
		}
	}
	
	/**
	 * The deleteGrouepdTask function removes the GroupedTask block in a task
	 * script
	 * 
	 * @param groupedTaskNum
	 *            the id of a GroupedTask
	 */
	public void deleteGroupedTask(Integer groupedTaskNum) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);
		
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());		
		NodeList groupedTaskNodeList = tasksNode.getChildNodes();

		tasksNode.removeChild(groupedTaskNode);
	}
	
	/**
	 * The moveTask function moves a task up and down in a boundary of a
	 * GroupedTask
	 * 
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param taskNum
	 *            id of a Task
	 * @param taskSeq
	 *            direction of movement
	 */	
	public void moveTask(Integer groupedTaskNum, Integer taskNum, TaskSeq taskSeq) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);		

		if(taskSeq == TaskSeq.DOWN) {
			Node currGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());
			Node currGroupedTaskIdNode = currGroupedTaskNode.getChildNodes().item(0);
			Node currTaskNode = currGroupedTaskNode.getChildNodes().item(taskNum.intValue()+1); // GroupedTaskID was the first one
			Node currTaskIdNode = currTaskNode.getChildNodes().item(0);
			Node currTaskIdNextNode = currTaskNode.getChildNodes().item(1);
			
			Node nextTaskNode = currGroupedTaskNode.getChildNodes().item(taskNum.intValue()+2);
			Node nextTaskIdNode = nextTaskNode.getChildNodes().item(0);
			Node nextTaskIdNextIdNode = nextTaskNode.getChildNodes().item(1);

			// Switching taskID tags
			currTaskNode.removeChild(currTaskIdNode);
			nextTaskNode.removeChild(nextTaskIdNode);

			currTaskNode.insertBefore(nextTaskIdNode, currTaskIdNextNode);
			nextTaskNode.insertBefore(currTaskIdNode, nextTaskIdNextIdNode);

			// Switching Task tags
			currGroupedTaskNode.removeChild(nextTaskNode);
			currGroupedTaskNode.insertBefore(nextTaskNode, currTaskNode);
		} else {
			Node currGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());
			Node currGroupedTaskIdNode = currGroupedTaskNode.getChildNodes().item(0);
			Node currTaskNode = currGroupedTaskNode.getChildNodes().item(taskNum.intValue()+1); // GroupedTaskID was the first one
			Node currTaskIdNode = currTaskNode.getChildNodes().item(0);
			Node currTaskIdNextNode = currTaskNode.getChildNodes().item(1);
			
			Node prevTaskNode = currGroupedTaskNode.getChildNodes().item(taskNum.intValue());
			Node prevTaskIdNode = prevTaskNode.getChildNodes().item(0);
			Node prevTaskIdNextIdNode = prevTaskNode.getChildNodes().item(1);

			// Switching taskID tags
			currTaskNode.removeChild(currTaskIdNode);
			prevTaskNode.removeChild(prevTaskIdNode);

			currTaskNode.insertBefore(prevTaskIdNode, currTaskIdNextNode);
			prevTaskNode.insertBefore(currTaskIdNode, prevTaskIdNextIdNode);

			// Switching Task tags
			currGroupedTaskNode.removeChild(currTaskNode);
			currGroupedTaskNode.insertBefore(currTaskNode, prevTaskNode);			
		}
	}
	
	/**
	 * The moveGroupedTask function moves a block of GroupedTask up and down
	 * Below is the sequence of execution: 1. Create a new Node with a list 2.
	 * Replace the node in the list 3. Reassign the nodeList
	 * 
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param groupedTaskSeq
	 *            direction of movement
	 */
	public void moveGroupedTask(Integer groupedTaskNum, TaskSeq groupedTaskSeq) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);		

		if(groupedTaskSeq == TaskSeq.UP) {
			Node currGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());
			Node currGroupedTaskIdNode = currGroupedTaskNode.getChildNodes().item(0);
			Node currGroupedTaskNextNode = currGroupedTaskNode.getChildNodes().item(1);
			
			Node prevGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()-1);
			Node prevGroupedTaskIdNode = prevGroupedTaskNode.getChildNodes().item(0);
			Node prevGroupedTaskNextNode = prevGroupedTaskNode.getChildNodes().item(1);

			// Switching GroupedTaskID tags
			currGroupedTaskNode.removeChild(currGroupedTaskIdNode);
			prevGroupedTaskNode.removeChild(prevGroupedTaskIdNode);

			currGroupedTaskNode.insertBefore(prevGroupedTaskIdNode, currGroupedTaskNextNode);
			prevGroupedTaskNode.insertBefore(currGroupedTaskIdNode, prevGroupedTaskNextNode);

			// Switching GroupedTask tags
			tasksNode.removeChild(currGroupedTaskNode);
			tasksNode.insertBefore(currGroupedTaskNode, prevGroupedTaskNode);
		} else {
			Node nextGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()+1);
			Node nextGroupedTaskIdNode = nextGroupedTaskNode.getChildNodes().item(0);
			Node nextGroupedTaskNextNode = nextGroupedTaskNode.getChildNodes().item(1);

			Node currGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());
			Node currGroupedTaskIdNode = currGroupedTaskNode.getChildNodes().item(0);
			Node currGroupedTaskNextNode = currGroupedTaskNode.getChildNodes().item(1);
			
			// Switching GroupedTaskID tags
			currGroupedTaskNode.removeChild(currGroupedTaskIdNode);
			nextGroupedTaskNode.removeChild(nextGroupedTaskIdNode);

			currGroupedTaskNode.insertBefore(nextGroupedTaskIdNode, currGroupedTaskNextNode);
			nextGroupedTaskNode.insertBefore(currGroupedTaskIdNode, nextGroupedTaskNextNode);

			// Switching GroupedTask tags
			tasksNode.removeChild(nextGroupedTaskNode);
			tasksNode.insertBefore(nextGroupedTaskNode, currGroupedTaskNode);
		}
	}
	
	/**
	 * List the primitives from the database
	 * 
	 * @param primitiveName
	 * @return
	 */
	public Vector<String> listPrimitives(String primitiveName) {
		// First primitive for the orchestration
		if(primitiveName == null) {
			return dbo.listPrimitivesFromPrimitveCapabilityTB();
		}
		
		Vector<String> capabilityList = null;
		
		System.out.println("[TaskScriptOrchestrator] listPrimitives: primitiveName(" + primitiveName + ")");
		String capabilityName = null;
		try {
			capabilityName = dbo.retrieveCapability(primitiveName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[TaskScriptOrchestrator] listPrimitives: capabilityName(" + capabilityName + ")");
		
		// capabilityName can be NULL
		if(capabilityName == null)
			return null;
		
		try {
			capabilityList = dbo.listPrimitives();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("[TaskScriptOrchestrator] listPrimitives: capabilityList.size(" + capabilityList.size() + ")");
		for(String resourcesUsedByCapability:
			capabilityResourceTable.returnListOfResourcesCapabilityIsUsing(capabilityName)) {
			for(String capabilitySharingSameResource: 
				capabilityResourceTable.returnListOfCapabilitiesUsingSameResource(resourcesUsedByCapability)) {
				capabilityList.remove(capabilitySharingSameResource);
			}
		}
		
		Vector<String> primitiveList = new Vector<String>();
		for(int i=0; i  < capabilityList.size(); ++i) {
			primitiveList.add(dbo.retrievePrimitive(capabilityList.get(i)));			
		}		
		
		return primitiveList;
	}

	/**
	 * The getTaskName function returns the name of a task given a taskNum and
	 * groupedTaskNum
	 * 
	 * @param groupedTaskNum
	 *            id of a GropuedTask
	 * @param taskNum
	 *            id of a Task
	 * @return name of a Task
	 */
	public String getTaskName(Integer groupedTaskNum, Integer taskNum) {
		String taskName = null;
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		Node currGroupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue());
		Node currGroupedTaskIdNode = currGroupedTaskNode.getChildNodes().item(0);
		String currGroupedTaskIdString = currGroupedTaskIdNode.getTextContent();
		Node currTaskNode = currGroupedTaskNode.getChildNodes().item(taskNum.intValue()+1);
		Node currTaskNameNode = currTaskNode.getChildNodes().item(1);
		String currTaskNameNodeString = currTaskNameNode.getTextContent();
		
		return currTaskNameNodeString;
	}
	
	/**
	 * The addGroupedTaskDependency function adds a dependency to a GroupedTask
	 * 
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param dependentGroupedTaskNum
	 *            id of a dependent GropuedTask
	 */
	public void addGroupedTaskDependency(Integer groupedTaskNum,
			Integer dependentGroupedTaskNum) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		Element groupedTaskDependsOn = dom.createElement("GroupedTaskDependsOn");

		Attr groupedTaskDependsOnId = dom.createAttribute("id");
		groupedTaskDependsOnId.setValue(dependentGroupedTaskNum.toString());
		groupedTaskDependsOn.setAttributeNode(groupedTaskDependsOnId);

		groupedTaskNode.appendChild(groupedTaskDependsOn);		
	}	
	
	/**
	 * The addTaskDependency function adds a dependency to a task
	 * 
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param taskNum
	 *            id of a Task
	 * @param dependentTaskNum
	 *            id of a Task that a Task depends on
	 * @param status
	 *            exit status of a Task that a Task depends on
	 * @param capabilityName
	 *            name of the capability that is dependent on the Task with
	 *            taskNum as an id
	 */
	public void addTaskDependency(Integer groupedTaskNum, Integer taskNum, 
			Integer dependentTaskNum, Integer status, String capabilityName) {
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		Node taskNode = groupedTaskNode.getChildNodes().item(taskNum.intValue() + 1); // Task
		Node taskIdNode = taskNode.getChildNodes().item(0);
		
		Element dependsOn = dom.createElement("DependsOn");

		Attr dependsOnId = dom.createAttribute("id");
		dependsOnId.setValue(dependentTaskNum.toString());
		dependsOn.setAttributeNode(dependsOnId);

		Attr dependsOnStatus = dom.createAttribute("status");
		dependsOnStatus.setValue(status.toString());
		dependsOn.setAttributeNode(dependsOnStatus);
		
		taskNode.appendChild(dependsOn); // dependsOn
	}


	/**
	 * The addInput function adds an TaskInput tag in a Task block in a
	 * GroupedTask with groupedTaskNum as an id
	 * 
	 * @param groupedTaskNum
	 */
	public void addInput(String primitiveName, Integer groupedTaskNum) {
		String capabilityName = dbo.retrieveCapability(primitiveName);
		// Look into mf
		Vector<String> inputVector = capabilityResourceTable.returnCapabilityInput(capabilityName);
		if(inputVector == null)
			return;

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		
		Node taskNode = null;
		int taskNum = -1;
		for(int i = 0; taskNum == -1 && i < taskNodeList.getLength(); ++i) {
			taskNode = taskNodeList.item(i + 1); // Task
			Node taskNameNode = taskNode.getChildNodes().item(1);
			if(taskNameNode.getTextContent().equals(primitiveName))
				taskNum = i;			
		}

		for(int i=0; i < inputVector.size(); ++i) {
			String parameterInfo = inputVector.get(i);
			Element parameter = dom.createElement("Parameter");
			Element parameterName = dom.createElement("ParameterName");
			parameterName.appendChild(dom.createTextNode(parameterInfo));
			parameter.appendChild(parameterName);
			taskNode.appendChild(parameter);
		}
	}

	/**
	 * The getListOfPreviousOutputs function provides a list of all the outputs
	 * that a task can have in a GroupedTask
	 * 
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param taskNum
	 *            id of a Task
	 * @return a list of Outputs that a Task can depend on
	 */
	public Vector<String> getListOfPreviousOutputs(Integer groupedTaskNum, Integer taskNum) {
		Vector<String> prevOutputLists = new Vector<String>();

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);
		
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask
		NodeList groupedTaskNodeList = groupedTaskNode.getChildNodes();

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		Node prevTaskNode = groupedTaskNode.getChildNodes().item(taskNum.intValue() + 1); // Task
		Node taskIdNode = prevTaskNode.getChildNodes().item(0);
		NodeList taskNodeDescriptionList = prevTaskNode.getChildNodes();		
		
		for(int i =0; i < taskNodeDescriptionList.getLength(); ++i) {
			Node temp = taskNodeDescriptionList.item(i);
			if(temp.getNodeName().equals("Output")) {
				prevOutputLists.add(temp.getTextContent());
			}
		}
		return prevOutputLists;
	}
	
	/**
	 * The addInputOutput function adds a TaskInputOut tag into a Task to show a
	 * mapping of an Ouptut with an Input
	 * 
	 * @param primitiveName
	 *            name of a Task
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 * @param srcOutputNameString
	 *            output name of a dependent Task
	 * @param dstParameterNameString
	 *            input name of a Task with primitiveName
	 */
	public void addInputOutput(String primitiveName, Integer groupedTaskNum,
			String srcOutputNameString, String dstParameterNameString) {
		String capabilityName = dbo.retrieveCapability(primitiveName);
		// Look into mf
		Vector<String> outputVector = capabilityResourceTable.returnCapabilityOutput(capabilityName);
		if(outputVector == null)
			return;
		
		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();

		Node taskNode = null;
		int taskNum = -1;
		for(int i = 0; taskNum == -1 && i < taskNodeList.getLength(); ++i) {
			taskNode = taskNodeList.item(i + 1); // Task
			Node taskNameNode = taskNode.getChildNodes().item(1);
			if(taskNameNode.getTextContent().equals(primitiveName))
				taskNum = i;			
		}

		String dependsOnId = null;
		for(int i=0; dependsOnId == null && i < taskNode.getChildNodes().getLength(); ++i) {
			Node tempNode = taskNode.getChildNodes().item(i);
			if(tempNode.getNodeName().toLowerCase().equals("dependson")) {
				dependsOnId = ((Element)tempNode).getAttribute("id");
				System.out.println("a:" + tempNode.getNodeName());
				System.out.println("id: " + ((Element)tempNode).getAttribute("id"));
				
			}
		}

		
		for(int i=0; i < outputVector.size(); ++i) {
			String parameterInfo = outputVector.get(i);
			Element parameter = dom.createElement("TaskInputMap");
			
			Element inputCapabilityId = dom.createElement("InputCapabilityId");
			
			// Get info from dependsOn
			inputCapabilityId.appendChild(dom.createTextNode(dependsOnId)); //update
			parameter.appendChild(inputCapabilityId);
			
			Element srcOutputName = dom.createElement("SrcOutputName");
			srcOutputName.appendChild(dom.createTextNode(srcOutputNameString));
			parameter.appendChild(srcOutputName);
			
			Element dstParameterName = dom.createElement("DstParameterName");
			dstParameterName.appendChild(dom.createTextNode(dstParameterNameString));
			parameter.appendChild(dstParameterName);
			
			
			taskNode.appendChild(parameter);
		}		
	}
	
	/**
	 * The addOutput function adds an TaskOutput tag to a Task in a GroupedTask
	 * with a groupedTaskNum
	 * 
	 * @param primitiveName
	 *            name of a Task that TaskOuput is getting added to
	 * @param groupedTaskNum
	 *            id of a GroupedTask
	 */
	public void addOutput(String primitiveName, Integer groupedTaskNum) {
		String capabilityName = dbo.retrieveCapability(primitiveName);
		// Look into mf
		Vector<String> outputVector = capabilityResourceTable.returnCapabilityOutput(capabilityName);
		if(outputVector == null)
			return;

		Element root = dom.getDocumentElement();
		Node tasksNode = root.getChildNodes().item(0);

		NodeList groupedTaskNodeList = tasksNode.getChildNodes();
		Node groupedTaskNode = tasksNode.getChildNodes().item(groupedTaskNum.intValue()); // GroupedTask

		NodeList taskNodeList = groupedTaskNode.getChildNodes();
		
		Node taskNode = null;
		int taskNum = -1;
		for(int i = 0; taskNum == -1 && i < taskNodeList.getLength(); ++i) {
			taskNode = taskNodeList.item(i + 1); // Task
			Node taskNameNode = taskNode.getChildNodes().item(1);
			if(taskNameNode.getTextContent().equals(primitiveName))
				taskNum = i;			
		}
		
		for(int i=0; i < outputVector.size(); ++i) {
			String parameterInfo = outputVector.get(i);
			Element parameter = dom.createElement("Output");
			Element parameterName = dom.createElement("OutputName");
			parameterName.appendChild(dom.createTextNode(parameterInfo));
			parameter.appendChild(parameterName);
			taskNode.appendChild(parameter);
		}
	}
	
	/**
	 * The addTask function adds a Task with the following properties: taskType
	 * - parallel/sequential groupedTask# dependency
	 * 
	 */
	public void addTask(TaskType taskType, Integer groupedTaskNum, Integer dependentTaskNum, String taskNameString) {
		Element root = dom.getDocumentElement();
		
		Node groupedTaskNode = root.getChildNodes().item(0)
				.getChildNodes().item(groupedTaskNum.intValue());
		
		int numOfTask=0;
		for(int i = 0; i < groupedTaskNode.getChildNodes().getLength(); ++i) {
			if(groupedTaskNode.getChildNodes().item(i).getNodeName().equals("Task")) {
				++numOfTask;
			}
		}

		Element task = dom.createElement("Task");
		groupedTaskNode.appendChild(task);

		int index = groupedTaskNode.getChildNodes().getLength() - 1;
		Element taskId = dom.createElement("TaskID");
		taskId.appendChild(dom.createTextNode(new Integer(numOfTask).toString()));
		task.appendChild(taskId);

		Element taskName = dom.createElement("TaskName");
		taskName.appendChild(dom.createTextNode(taskNameString));
		task.appendChild(taskName);

		Element successFlag = dom.createElement("Flag");		
		Attr successFlagStatus = dom.createAttribute("status");
		successFlagStatus.setValue("1");
		successFlag.setAttributeNode(successFlagStatus);
		successFlag.appendChild(dom.createTextNode("Success"));
		task.appendChild(successFlag); // success

		Element failureFlag = dom.createElement("Flag");
		Attr failureFlagStatus = dom.createAttribute("status");
		failureFlagStatus.setValue("0");
		failureFlag.setAttributeNode(failureFlagStatus);
		failureFlag.appendChild(dom.createTextNode("Fatal"));
		task.appendChild(failureFlag); // fail

		Element dependsOn = dom.createElement("DependsOn");
		//if(dependentTaskNum.intValue() != -1) {
		if(taskType == TaskType.SEQUENTIAL && dependentTaskNum.intValue() != -1) {
			Attr attrId = dom.createAttribute("id");
			attrId.setValue(dependentTaskNum.toString());
			dependsOn.setAttributeNode(attrId);

			Attr attrStatus = dom.createAttribute("status");
			attrStatus.setValue("1");
			dependsOn.setAttributeNode(attrStatus);

			task.appendChild(dependsOn);			
		}
	}
	
	/**
	 * 
	 * The addFalg function adds a flag for status other than success or fatal
	 * 
	 * @param statusValue
	 *            is status value of a flag
	 */
	public void addFlag(Integer statusValue) {
		Element root = dom.getDocumentElement();
		Element flag = dom.createElement("Flag");
		root.appendChild(flag);
	}
}