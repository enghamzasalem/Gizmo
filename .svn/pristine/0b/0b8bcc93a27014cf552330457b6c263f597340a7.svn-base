<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
	cobot3Dasboard capability web page. 
 -->

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js">
		</script>	
	<title>cobot3 dashboard</title>
	
	<script type="text/javascript">
		$(document).ready(function(){
			
			//Move camera events
			$("#upArr_camera").click( function() {
					sendMoveCommand('up', 'moveCamera');	
				}
			);
			$("#downArr_camera").click( function() {
					sendMoveCommand('down', 'moveCamera');	
				}
			);
			$("#rightArr_camera").click( function() {
					sendMoveCommand('right', 'moveCamera');	
				}
			);
			$("#leftArr_camera").click( function() {
					sendMoveCommand('left', 'moveCamera');	
				}
			);
			
			// Move cobot events
			$("#upArr_cobot").click( function() {
					sendMoveCommand('up', 'moveCobot');	
				}
			);
			$("#rightArr_cobot").click( function() {
					sendMoveCommand('right', 'moveCobot');	
				}
			);
			$("#leftArr_cobot").click( function() {
					sendMoveCommand('left', 'moveCobot');	
				}
			);
			
			
			
			//Capture the key presses to move cobot
			$(document).keydown(function(e){
			    if (e.keyCode == 37) { 
			    	sendMoveCommand('left', 'moveCobot');	
			    }
			    if (e.keyCode == 38) { 
			       sendMoveCommand('up', 'moveCobot');	
			    }
			    if (e.keyCode == 39) { 
			       sendMoveCommand('right', 'moveCobot');
			    }
			});
		});

		// Get the video image every 250 milli-seconds
		setInterval(function(){
			$("#cameraImage").attr("src", 
					"http://"+ document.location.host + "/gizmoapp/taskClient.action?action=getVideoOutput&taskId=0&capabilityId=0")
		}, 250);
		
		/* This function can be used to either to move the camera or to use CoBot
			depending of the value of the moveAction*/
		function sendMoveCommand(cameraDirection, moveAction)
		{
			$.ajax({
				url: "http://"+ document.location.host + "/gizmoapp/taskClient.action?action="+ moveAction,
				data: { taskId: $("#taskId").attr("value"), 
					   capabilityId:  $("#capabilityId").attr("value"), 
					   direction:  cameraDirection
					   },
				success: function(data) {
					
				}	   
			});
		}
	</script>
</head>
<body>


	<%
		//Code required to bind the JSP page to the capability at the back-end
		String uiElementId = (String) session.getAttribute("cobot3Dashboard.jsp");
		String [] uiElementTokens = uiElementId.split("_x_");
		String taskId = uiElementTokens[0];
		String capabilityId = uiElementTokens[1];
		pageContext.setAttribute("taskId", taskId);
		pageContext.setAttribute("capabilityId", capabilityId);
		
	%>
	
	<input type="hidden" id="taskId" name="taskId" value="<%= taskId %>"/>
	<input type="hidden" id="capabilityId" name="capabilityId" value="<%= capabilityId %>"/>
		
		<label style="font-style: italic;color:white;background: gray;">CoBot3 Dashboard</label>
	<div id="mainDiv" align="center">
		<img id="cameraImage" src="loadingVid.gif" alt="" align="middle"/>
		<table align="center">
			<tr>
				<td>
					<table align="center">
						<tr>
							<td></td>
							<td><img id="upArr_camera" src="up.png" alt=""/></td>
							<td></td>
						</tr>
						<tr>
							<td><img id="leftArr_camera" src="left.png" alt=""/></td>
							<td><img src="webcam.png" alt="CoBot Camera"/></td>
							<td><img id="rightArr_camera" src="right.png" alt=""/></td>
						</tr>
						<tr>
							<td></td>
							<td><img id="downArr_camera" src="down.png" alt=""/></td>
							<td></td>
						</tr>
					</table>
				</td>
				<td>
					<table align="center">
						<tr>
							<td></td>
							<td><img id="upArr_cobot" src="up.png" alt=""/></td>
							<td></td>
						</tr>
						<tr>
							<td><img id="leftArr_cobot" src="left.png" alt=""/></td>
							<td><img src="robot.png" alt="CoBot Steering"/></td>
							<td><img id="rightArr_cobot" src="right.png" alt=""/></td>
						</tr>
						<tr>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>	

</body>
</html>