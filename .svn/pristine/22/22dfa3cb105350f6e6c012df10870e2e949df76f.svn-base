<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--
	Go To Room capability web page. 
 -->

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js">
	</script>
	
	<script type="text/javascript">
		$(document).ready(
				function () {
					//Render the form elements
					$("#goToRoomDiv").empty();
					$("#goToRoomDiv").append("<label>Location: </label><input type='text' id='locationTx'></label> <br/>");
					$("#goToRoomDiv").append("<button id='goBtn' >Go!</button>");
					
					//Query the capability observer for any default room number that is routed.
					$.ajax({
					url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=getDefaultInput",
					data: { taskId: $("#goToRoomTaskId").attr("value"), 
						   capabilityId:  $("#goToRoomCapabilityId").attr("value")
						   },
				    dataType: 'json',	   
					success: function(data) {
						/* If there's any routed input, ask the user either to navigate to the 
							routed room number or too choose one*/
						var roomNumber = data.defaultInput.room;
						if(roomNumber) {
							var r=confirm("CoBot will navigate to " + roomNumber + ". Press OK to navigate now, or cancel to change the location.");
							if (r==true)
							  {
							  	sendInput(roomNumber)
							  }
							else
							  {
							 	return false;
							  }	
						}
						
					}	   
				});
				
				//Send the requested room number
				$("#goBtn").click(function() {
					var newLocation = $("#locationTx").attr("value");
					sendInput(newLocation);
				});
			}	
		);

		
		//A function to send the request room number entered in the form
		function sendInput(location) {
			$.ajax({
				url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=sendInput",
				data: { taskId: $("#goToRoomTaskId").attr("value"), 
					   capabilityId:  $("#goToRoomCapabilityId").attr("value"), 
					   room: location
					   },
				success: function(data) {
					//Start reading the output that contains the status of the execution
					getJsonOutput();
				}	   
			});
		}
		
		//A function to get the status of the execution.
		function getJsonOutput() {
			//Ping for the status every second
			setInterval(function(){ 
				$.ajax({
				url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=getOutput",
				data: { taskId: $("#goToRoomTaskId").attr("value"), 
					   capabilityId:  $("#goToRoomCapabilityId").attr("value")
					   },
			    dataType: 'json',	   
				success: function(data) {
					if (data.output.roomNumber)
					{
						//Display the output
						$("#goToRoomDiv").empty();
						$("#goToRoomDiv").append("<label>Going to room </label><label id='roomLbl'></label> <br/>");
						$("#goToRoomDiv").append("<label>Status: </label><label id='statusLbl'></label>");
						
						$("#roomLbl").text(data.output.roomNumber);
						$("#statusLbl").text(data.output.navStatus);	
					}
					
				}	   
			})}, 1000);
		}

	</script>
	
	<title>Lookup Person</title>
</head>
<body>

	<%
	//Code required to bind the JSP page to the capability at the back-end
	String uiElementId = (String) session.getAttribute("goToRoom.jsp");
	String [] uiElementTokens = uiElementId.split("_x_");
	String taskId = uiElementTokens[0];
	String capabilityId = uiElementTokens[1];
	pageContext.setAttribute("taskId", taskId);
	pageContext.setAttribute("capabilityId", capabilityId);

	
	%>
	
	<input type="hidden" name="action" value="sendInput"/>
	<input type="hidden" id="goToRoomTaskId" name="goToRoomTaskId" value="<%= taskId %>"/>
	<input type="hidden" id="goToRoomCapabilityId" name="goToRoomCapabilityId" value="<%= capabilityId %>"/>
	
		<label style="font-style: italic;color:white;background: gray;">Location Navigation</label>
	<div id="goToRoomDiv">		
		<label>Location: </label><input type="text" id="locationTx"></label> <br/>
		<button id="goBtn" >Go!</button>
	</div>
</body>
</html>