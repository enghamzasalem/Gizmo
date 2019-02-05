package edu.cmu.gizmo.management.taskmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import edu.cmu.gizmo.management.taskmanager.scripttask.DependsOn;
import edu.cmu.gizmo.management.taskmanager.scripttask.Flag;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTask;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTaskDependsOn;
import edu.cmu.gizmo.management.taskmanager.scripttask.GroupedTaskStatus;
import edu.cmu.gizmo.management.taskmanager.scripttask.Output;
import edu.cmu.gizmo.management.taskmanager.scripttask.Parameter;
import edu.cmu.gizmo.management.taskmanager.scripttask.PlannedTaskStatus;
import edu.cmu.gizmo.management.taskmanager.scripttask.PlannedTasks;
import edu.cmu.gizmo.management.taskmanager.scripttask.Task;

/**
 * TaskScriptParser contains the parsing function of a TaskScriptStrategy.
 * It is mainly used by TaskOrchestrator and not by TaskScriptStrategy at all.
 * It was started as a refactoring effort but due to the short amount of time, 
 * the same logic in ScriptTaskStrategy is copied over. Therefore, it essentailly
 * carries out the same funcitonality as ScriptTaskStrategy's parse function.
 *  
 * @author sjnicklee
 * @see TaskScriptStrategy
 *
 */
public class TaskScriptParser {
	/** The ID of a current task **/
	private Integer taskId = null;

	/** The name of a task script **/
	private String taskPlan = null;

	/** The javabean of a script task **/
	private PlannedTasks tasks = null;

	/** The status of a task **/
	private TaskStatus update = null;

	/** The document object of a task script xml file **/
	private Document doc = null;

	/**
	 * An Empty constructor 
	 */
	public TaskScriptParser() {
	}

	/**
	 * Sets the name of a task script
	 * @param taskPlan the name of a task script
	 */
	// Different from ScripStrategy
	public void setTaskPlan(String taskPlan) {
		this.taskPlan = taskPlan;
	}

	/**
	 * Returns document object (DOM) of a task script xml 
	 * @return DOM object
	 */
	public Document getDocument() {
		return doc;
	}
	
	/**
	 * Returns javabean of a task script that has all the references
	 * @return javabean of a task script
	 */
	public PlannedTasks getPlannedTasks() {
		return tasks;
	}

	/**
	 * Parses a task script and creates a javabean with all the referecnes
	 * @return the status of an execution
	 */
	public boolean parse() {
		try {
			File file = new File(taskPlan);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(file);
			doc.getDocumentElement().normalize();

			ArrayList<TaskInputMap> totalTaskInputMapArrayList = 
					new ArrayList<TaskInputMap>(0);

			// -------
			// TASKS
			// -------
			NodeList tasksNodeList = doc.getElementsByTagName("Tasks");
			tasks = new PlannedTasks();
			for (int taskCounter = 0; taskCounter < tasksNodeList.getLength(); taskCounter++) {
				Node tasksNode = tasksNodeList.item(taskCounter);
				if (tasksNode.getNodeType() == Node.ELEMENT_NODE) {
					Element taskElement = (Element) tasksNode;

					// ------------
					// GROUPEDTASK
					// ------------
					NodeList groupedTaskNodeList = taskElement
							.getElementsByTagName("GroupedTask");
					for (int groupedTaskCounter = 0; groupedTaskCounter < groupedTaskNodeList
							.getLength(); ++groupedTaskCounter) {
						GroupedTask groupedTaskObj = new GroupedTask();
						ArrayList<GroupedTask> groupedTaskListObj =
								tasks.getGroupedTask();
						groupedTaskListObj.add(groupedTaskObj);
						Node groupedTaskNodeListNode = groupedTaskNodeList
								.item(groupedTaskCounter);
						if (groupedTaskNodeListNode.getNodeType() == Node.ELEMENT_NODE) {
							Element groupedTaskNodeListElement = (Element) groupedTaskNodeListNode;

							// ------------------
							// GROUPEDTASKSTATUS
							// ------------------
							GroupedTaskStatus groupedTaskStatus
							= new GroupedTaskStatus();
							groupedTaskObj
									.setGroupedTaskStatus(groupedTaskStatus);

							// ----------------------
							// GROUPEDTASKDEPENDSON
							// ----------------------
							NodeList groupedTaskDependsOnNodeList = groupedTaskNodeListElement
									.getElementsByTagName("GroupedTaskDependsOn");
							for (int groupedTaskDependsOnNodeListCounter = 0; groupedTaskDependsOnNodeListCounter < groupedTaskDependsOnNodeList
									.getLength(); ++groupedTaskDependsOnNodeListCounter) {
								Element groupedTaskDependsOnElement = (Element) groupedTaskDependsOnNodeList
										.item(groupedTaskDependsOnNodeListCounter);
								NodeList groupedTaskDependsOnList = groupedTaskDependsOnElement
										.getChildNodes();
								GroupedTaskDependsOn groupedTaskDependsOnObj = new GroupedTaskDependsOn();
								groupedTaskObj
										.setGroupedTaskDependsOn(groupedTaskDependsOnObj);
							}

							// --------------
							// GroupedTaskID
							// --------------
							NodeList groupedTaskIdList = groupedTaskNodeListElement
									.getElementsByTagName("GroupedTaskID");
							for (int groupedTaskIdListCounter = 0; groupedTaskIdListCounter < groupedTaskIdList
									.getLength(); ++groupedTaskIdListCounter) {
								Element groupedTaskIdElement = (Element) groupedTaskIdList
										.item(groupedTaskIdListCounter);
								NodeList groupedTaskIdNodeList = groupedTaskIdElement
										.getChildNodes();
																
								if(((Node) groupedTaskIdNodeList
										.item(groupedTaskIdListCounter))
										.getNodeValue() == null) {
									update = new TaskStatus(taskId,
									TaskStatus.TaskStatusValue.ERROR, "ERROR");
								}

								int groupedTaskIdInt = new Integer(
										((Node) groupedTaskIdNodeList
												.item(groupedTaskIdListCounter))
												.getNodeValue()).intValue();
								groupedTaskObj
										.setGroupedTaskId(groupedTaskIdInt);
							}

							// --------------
							// Task
							// --------------
							NodeList groupedTaskList = groupedTaskNodeListElement
									.getElementsByTagName("Task");
							ArrayList<Task> arrayListTaskObj = groupedTaskObj
									.getTasks();
							for (int groupedTaskListCounter = 0; 
									groupedTaskListCounter < groupedTaskList
									.getLength(); ++groupedTaskListCounter) {
								Task taskObj = new Task();
								arrayListTaskObj.add(taskObj);
								Element groupedTaskElement = 
										(Element) groupedTaskList
										.item(groupedTaskListCounter);

								PlannedTaskStatus taskStatus = new PlannedTaskStatus();
								taskObj.setTaskStatus(taskStatus);

								// --------------
								// DependsOn
								// --------------
								NodeList dependsOnNodeList = groupedTaskElement
										.getElementsByTagName("DependsOn");
								for (int dependsOnNodeListCounter = 0; dependsOnNodeListCounter < dependsOnNodeList
										.getLength(); ++dependsOnNodeListCounter) {
									Element dependsOnElement = (Element) dependsOnNodeList
											.item(dependsOnNodeListCounter);
									NodeList dependsOnList = dependsOnElement
											.getChildNodes();

									DependsOn dependsOn = new DependsOn();
									dependsOn
											.setId(new Integer(dependsOnElement
													.getAttribute("id"))
													.intValue());
									dependsOn
											.setStatus(new Integer(
													dependsOnElement
															.getAttribute("id"))
													.intValue());
									taskObj.setDependsOn(dependsOn);
								}

								// --------------
								// TaskID
								// --------------
								NodeList taskIdNodeList = groupedTaskElement
										.getElementsByTagName("TaskID");
								Element taskIdElement = (Element) taskIdNodeList
										.item(0);
								
								NodeList taskIdList = null;
								int taskIdInt = -1;
								if(taskIdElement == null) {


									update = new TaskStatus(taskId,
									TaskStatus.TaskStatusValue.ERROR, "ERROR");
								} else {
									taskIdList = taskIdElement.getChildNodes();

									taskIdInt = new Integer(
										((Node) taskIdList.item(0))
												.getNodeValue()).intValue();
									taskObj.setTaskId(taskIdInt);
								}
								// --------------
								// TaskName
								// --------------
								NodeList taskNameNodeList = groupedTaskElement
										.getElementsByTagName("TaskName");
								Element taskNameElement = (Element) taskNameNodeList
										.item(0);								
								if(taskNameElement == null) {
									update = new TaskStatus(taskId,
									TaskStatus.TaskStatusValue.ERROR, "ERROR");
								}
								else {
								
									NodeList taskNameList = taskNameElement
											.getChildNodes();

									taskObj.setTaskName(((Node) taskNameList
										.item(0)).getNodeValue());
								}

								// --------------
								// Output
								// --------------
								NodeList outputNodeList = groupedTaskElement
										.getElementsByTagName("Output");
								ArrayList<Output> arrayOutput = taskObj.getOutputArrayList();
								for (int outputNodeListCounter = 0; outputNodeListCounter < outputNodeList
										.getLength(); ++outputNodeListCounter) {
									// Node paramElement = (Element)
									// paramNodeList.item(0);
									Output output = new Output();

									Element outputNodeListElement = (Element) outputNodeList
											.item(outputNodeListCounter);
									NodeList outputNameNodeList = outputNodeListElement
											.getElementsByTagName("OutputName");
									Element outputNameElement = (Element) outputNameNodeList
											.item(0);
									NodeList outputNameList = outputNameElement
											.getChildNodes();

									output.setName(((Node) outputNameList
											.item(0))
											.getNodeValue());
									
									arrayOutput.add(output);
									taskObj.setOutputArrayList(arrayOutput);
								}

								// --------------
								// Parameter
								// --------------
								NodeList paramNodeList = groupedTaskElement
										.getElementsByTagName("Parameter");
								ArrayList<Parameter> arrayParameter = taskObj.getParameterArrayList();
								for (int paramNodeListCounter = 0; paramNodeListCounter < paramNodeList
										.getLength(); ++paramNodeListCounter) {
									// Node paramElement = (Element)
									// paramNodeList.item(0);
									Parameter parameter = new Parameter();

									Element paramNodeListElement = (Element) paramNodeList
											.item(paramNodeListCounter);
									NodeList paramNameNodeList = paramNodeListElement
											.getElementsByTagName("ParameterName");
									Element paraNameElement = (Element) paramNameNodeList
											.item(0);
									NodeList paramNameList = paraNameElement
											.getChildNodes();

									parameter.setName(((Node) paramNameList
											.item(0))
											.getNodeValue());
									
									arrayParameter.add(parameter);
									taskObj.setParameterArrayList(arrayParameter);
								}

								// --------------
								// Flag
								// --------------
								NodeList flagNodeList = groupedTaskElement
										.getElementsByTagName("Flag");
								ArrayList<Flag> flagArrayList = taskObj
										.getFlagArrayList();
								for (int flagNodeListCounter = 0; flagNodeListCounter < flagNodeList
										.getLength(); ++flagNodeListCounter) {
									Element flagElement = (Element) flagNodeList
											.item(flagNodeListCounter);
									NodeList flagList = flagElement
											.getChildNodes();
									Flag flag = new Flag();

									if(((Node) flagList.item(0))
											.getNodeValue() == null) {
										update = new TaskStatus(taskId,
										TaskStatus.TaskStatusValue.ERROR, "ERROR");
									} else {
										flag.setMsg(((Node) flagList.item(0))
											.getNodeValue());
										flag.setStatus(new Integer(flagElement
											.getAttribute("status")).intValue());
									
										flagArrayList.add(flag);
										taskObj.setFlagArrayList(flagArrayList);
									}
								}
								
								// --------------
								// TaskInputMap
								// --------------
								NodeList taskInputMapNodeList = groupedTaskElement
										.getElementsByTagName("TaskInputMap");
								ArrayList<TaskInputMap> taskInputMapArrayList = taskObj.getTaskInputMapArrayList();
								for (int taskInputMapNodeListCounter = 0; taskInputMapNodeListCounter < taskInputMapNodeList
										.getLength(); ++taskInputMapNodeListCounter) {
									TaskInputMap taskInputMap = new TaskInputMap();

									Element taskInputMapElement = (Element) taskInputMapNodeList
											.item(taskInputMapNodeListCounter);
									
									// SrcOutputName
									NodeList taskInputMapList = taskInputMapElement.getElementsByTagName("SrcOutputName");

									Element taskInputMapObjElement = (Element)taskInputMapList.item(0);
									NodeList taskInputMapObjList = taskInputMapObjElement.getChildNodes();
									String outputFrom = ((Node) taskInputMapObjList.item(0))
											.getNodeValue();

									// DstParameterName
									taskInputMapList = taskInputMapElement.getElementsByTagName("DstParameterName");

									taskInputMapObjElement = (Element)taskInputMapList.item(0);
									taskInputMapObjList = taskInputMapObjElement.getChildNodes();
									String inputTo = ((Node) taskInputMapObjList.item(0))
											.getNodeValue();
									
									// Add SrcOutputName & DstParameterName
									taskInputMap.addRoute(outputFrom, inputTo);
									
									// SrcId + DstId
									taskInputMapList = taskInputMapElement.getElementsByTagName("InputCapabilityId");

									taskInputMapObjElement = (Element)taskInputMapList.item(0);
									taskInputMapObjList = taskInputMapObjElement.getChildNodes();
									

									taskInputMap.setFromCapabilityId(new Integer(new Integer(((Node) taskInputMapObjList.item(0))
										.getNodeValue()).intValue() + taskIdInt));

									// Add SrdId + DstId
									
									// this is a hack to get this code to stop crashing because 
									// taskId is always null.
									if (taskId != null) {
										taskInputMap.setToCapabilityId(new Integer(taskIdInt + taskId.intValue()));
									}

									taskInputMapArrayList.add(taskInputMap);
									taskObj.setTaskInputMapArrayList(taskInputMapArrayList);
									totalTaskInputMapArrayList.addAll(taskInputMapArrayList);
								}								
							} // Task for loop
						} // if (groupedTaskNodeListNode.getNodeType() ==
							// Node.ELEMENT_NODE)
					} // GroupedTask for loop
				}
			}

			if(update == null) {
				Vector<TaskInputMap> v = new Vector<TaskInputMap>(0);

				for(int j=0;  j < totalTaskInputMapArrayList.size(); ++j) {
					TaskInputMap tim = totalTaskInputMapArrayList.get(j);
					v.add(tim);
				}
								
				update = new TaskStatus(taskId,
						TaskStatus.TaskStatusValue.READY, v);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			update = new TaskStatus(taskId,
					TaskStatus.TaskStatusValue.ERROR, "ERROR " + e.getMessage());
		} catch (SAXParseException e) {
			e.printStackTrace();
			update = new TaskStatus(taskId,
					TaskStatus.TaskStatusValue.ERROR, "ERROR " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			update = new TaskStatus(taskId,
					TaskStatus.TaskStatusValue.ERROR, "ERROR " + e.getMessage());
		}
		
		return false;
	}

}
