<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--
	Lookup Person capability web page. 
 -->
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<style type="text/css">
		.ui-timepicker-div .ui-widget-header{ margin-bottom: 8px; }
		.ui-timepicker-div dl{ text-align: left; }
		.ui-timepicker-div dl dt{ height: 25px; }
		.ui-timepicker-div dl dd{ margin: -25px 0 10px 65px; }
		.ui-timepicker-div td { font-size: 90%; }
	</style>
	
	<script type="text/javascript">
		$(document).ready(function(){
			
			//Here's what to do when the submit is clicked
			$('#submitBtn').click(
					function()
					{	
						
						//Send the form input
						$.ajax({
							url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=sendInput",
							data: { taskId: $("#findPersonTaskId").attr("value"), 
								   capabilityId:  $("#findPersonCapabilityId").attr("value"), 
								   personName:  $("#personName").attr("value"),
								   startTime:  $("#startTime").attr("value"),
								   endTime:  $("#endTime").attr("value")
								   },
							success: function(data) {
								//Get the output
								getJsonOutput();
							}	   
						});
					}
			);
			
		});

		function getJsonOutput() {
			
			//Get the output on stages. Ping the capability observer every second to get the updates.
			var outputInterval = setInterval(function(){ 
				$.ajax({
				url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=getOutput",
				data: { taskId: $("#findPersonTaskId").attr("value"), 
					   capabilityId:  $("#findPersonCapabilityId").attr("value")
					   },
			    dataType: 'json',	   
				success: function(data) {
					if(data.output.personName) {
						var personNameStr = data.output.personName;
						
						//If the person doesn't exist, stop asking for updates and display this message
						if(personNameStr == "Unknown")
						{
							alert('Person name is not valid. Please re-enter the data');
							clearInterval(outputInterval);
							return;
						}
						
						//Render the output
						$("#formDiv").empty();
						$("#formDiv").append("<label id='nameLbl'/>'s status on the calendar is <label id='statusLbl'/><br/>");
						$("#formDiv").append("Location: <label id='roomLbl'/><br/>");
						
						$("#nameLbl").text(data.output.personName);
						$("#nameLbl2").text(data.output.personName);
						$("#roomLbl").text(data.output.roomNumber);
						$("#statusLbl").text(data.output.availStatus);
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
	String uiElementId = (String) session.getAttribute("lookupPerson.jsp");
	String [] uiElementTokens = uiElementId.split("_x_");
	String taskId = uiElementTokens[0];
	String capabilityId = uiElementTokens[1];
	pageContext.setAttribute("taskId", taskId);
	pageContext.setAttribute("capabilityId", capabilityId);

	%>
	
	<input type="hidden" name="action" value="sendInput"/>
	<input type="hidden" id="findPersonTaskId" name="findPersonTaskId" value="<%= taskId %>"/>
	<input type="hidden" id="findPersonCapabilityId" name="findPersonCapabilityId" value="<%= capabilityId %>"/>
	<div id="mainDiv">
		<label style="font-style: italic;color:white;background: gray;">Look-up Person</label>		
		<div id="formDiv">
			<label>Name:</label> <input type="text" name="personName" id="personName"/> <br/>
			<label>Start Date/Time: </label> <input type="text" name="startTime" id="startTime"></input> <br/>
			<label>End Date/Time: </label> <input type="text" name="endTime" id="endTime"></input> <br/>
			<button id="submitBtn">Find!</button>
		</div>
	</div>
	
</body>
</html>