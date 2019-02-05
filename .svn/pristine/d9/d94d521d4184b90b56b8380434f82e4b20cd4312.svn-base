<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--

	The Skype Communication capability web page 
 -->

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js">
	</script>
	<script type="text/javascript" src="http://download.skype.com/share/skypebuttons/js/skypeCheck.js">
	</script>
	<script type="text/javascript">
	$(document).ready(function(){
		
		//Post the form inputs, i.e. the call duration
		$('#submitBtn').click(
				function()
				{
					$.ajax({
						url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action=sendInput",
						data: { taskId: $("#skypeCommunicationTaskId").attr("value"), 
							   capabilityId:  $("#skypeCommunicationCapabilityId").attr("value"), 
							   time:  $("#callTime").attr("value")
							   },
						success: function(data) {
							//Load the skype button when getting a success
							loadSkypeButton();
						}	   
					});
				}
		);
		
	});

	function loadSkypeButton()
	{
		$("#formDiv").empty();
		$("#formDiv").append("<a href='skype:cobot3?call&video=true'><img src='http://download.skype.com/share/skypebuttons/buttons/call_blue_white_124x52.png' style='border: none;' width='124' height='52' alt='Skype Me!' /></a>");
	}
	
	</script>
	<title>Skype Communication</title>
</head>
<body>
	<%
	//Code required to bind the JSP page to the capability at the back-end
	String uiElementId = (String) session.getAttribute("skypeCommunication.jsp");
	String [] uiElementTokens = uiElementId.split("_x_");
	String taskId = uiElementTokens[0];
	String capabilityId = uiElementTokens[1];
	pageContext.setAttribute("taskId", taskId);
	pageContext.setAttribute("capabilityId", capabilityId);

	
	%>
	
	<input type="hidden" name="action" value="sendInput"/>
	<input type="hidden" id="skypeCommunicationTaskId" name="skypeCommunicationTaskId" value="<%= taskId %>"/>
	<input type="hidden" id="skypeCommunicationCapabilityId" name="skypeCommunicationCapabilityId" value="<%= capabilityId %>"/>
	
	<label style="font-style: italic;color:white;background: gray;">Skype Communication</label>
	<div id="formDiv">		
		<label>Expected Call Time:</label> <input type="text" name="callTime" id="callTime"/> <br/>
		<button id="submitBtn">Submit</button>
	</div>
</body>
</html>