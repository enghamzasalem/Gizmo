<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!-- 
	The Task Client Action main view page. It is responsible of reflecting the task
	execution status on the user interface by managing the loading and the unloading
	of the capability web pages. Also, it keeps track of the overall task execution
	status, i.e. rejected or cancelled.
 -->

<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js">
	</script>
	<script src="http://code.jquery.com/ui/1.8.21/jquery-ui.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.21/themes/base/jquery-ui.css" type="text/css" media="all" />
	<script type="text/javascript" src="http://trentrichardson.com/examples/timepicker/jquery-ui-timepicker-addon.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		/*
			Ping the controller to get updates of the data model every second. The data model contains
			a list of capability observers, where each capability observer is mapped to a capability. 
			The responsibility of a capability observer is to hold the data associated with a capability
			and to hold the execution status of a capability.
			
		*/
		var outputInterval = setInterval(function(){$.getJSON("http://"+ document.location.host + "/gizmoapp/taskClient.action?action=updateUIStatus"
		  , 
	  function(data) {
			
			//Get the status of the overall task.
	  		var taskStatusCode = data.taskClientObserver.taskStatus;
	  		
			//The task is rejected
	  		if (taskStatusCode == 'REJECTED')
	  		{
	  			//show the status message in the page
	  			alert('Task execution rejected. Reason: ' + data.taskClientObserver.taskStatusMessage);
	  			clearInterval(outputInterval);
	  			return;
	  		}
	  		/* The task is accepted and is executed. Check the status of capabilities 
	  		to know what to load and unload */
		 	$.each(data.taskClientObserver.capabilityObservers,
				 function()
				 {
					 var taskIdParam = this.taskId;
					 var cpIdParam = this.capabilityId;
					 var cpElementId = this.taskId + '_x_' +this.capabilityId;
					 var jQueryCpElementId = '#' + cpElementId;
					 
					 //The capability is already loaded
					 if ( $(jQueryCpElementId).length > 0)
					 {
						 //Unload it!
						 if(this.status == 'ENDED')
						 {
							 $(jQueryCpElementId).remove();
						 }
						 //It's completed. Once you say continue, it will be marked completed and its status will change to ended
						 if(this.status == 'COMPLETED')
						 {
							 if($("#endBtn").length < 1) {
								 $(jQueryCpElementId).append("<br/><button id='endBtn'>Continue!</button>");
								 $("#endBtn").click(
										 function()
										 {
											 $.ajax({
													url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=completeCapability",
													data: { taskId: taskIdParam, 
														   capabilityId:  cpIdParam
														   }
												});
										 }
									);
								 }
							 }
							 
					 }
					 else //The capability is not there yet, so laod it!
					 {
						 if(this.status == 'STARTED')
						 {
							 //remove the loading text. Happens with the first capability
							 if( $("#loadingTxt").length > 0 )
							 {
								 $("#loadingTxt").remove();
							 }
							 //Add it to the user interface and assign its ID as a combination of the task id and the capability id
							 $("#cpPlayground").append("<div id='"+cpElementId+"' style='border-style: solid;border-color: gray;margin:10px;'/>");
							 $(jQueryCpElementId).attr('title', this.capabilityName);
							 $(jQueryCpElementId).load(this.capabilityUiDirectory);
						 }
						
					 }
					 
				 }
		   )
		   
		   //The task is cancelled. Has to be terminated!
		   if (taskStatusCode == 'CANCELLED')
	  		{
	  			//show status message in the page
	  			alert('Task execution cancelled. Reason: ' + data.taskClientObserver.taskStatusMessage);
	  			clearInterval(outputInterval);
	  			return;
	  		}
		 	
	  		//The task is completed. Tell the user to celebrate the victory!
		 	if (taskStatusCode == 'COMPLETED')
	  		{
	  			//show the status message in the page
	  			alert('The task has been successfuly completed!');
	  			clearInterval(outputInterval);
	  			return;
	  			//Maybe return?, but this will not show the dashboard
	  		}
		 
		    
	  })}, 1000);
	  });

	  function getCapabilityId(cpElementId)
	  {
		  var idsArray = cpElementId.split('_x_');
		  return idsArray[1];
	  }
	  
	  function getTaskId(cpElementId)
	  {
		  var idsArray = cpElementId.split('_x_');
		  return idsArray[0];
	  }
		  
	</script>
	
	<title>Task Execution Page</title>
</head>
<body>
	<table width="100%">
			<tr>
				<td width="100%"><h1>SCS Telepresence Robot</h1></td>
				<td><img src="Gizmo_logo_lg.png" align="right" alt="logo"/></td>
			</tr>
		</table>
		<hr />
	 
	<div id="cpPlayground">
		<p id="loadingTxt"><img id="cameraImage" src="loadingVid.gif" alt=""/></p>
	</div>
	
</body>
</html>