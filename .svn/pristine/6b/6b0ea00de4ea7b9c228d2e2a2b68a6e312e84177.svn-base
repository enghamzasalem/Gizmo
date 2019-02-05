<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 
	The Task Selector web page. It shows the task scheduled for the user.
 -->
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>SCS Telepresence Robot</title>
</head>
<body>
	<table width="100%">
		<tr>
			<td width="100%"><h1>SCS Telepresence Robot</h1></td>
			<td><img src="Gizmo_logo_lg.png" align="right" alt="logo"/></td>
		</tr>
	</table>
	<hr />
	<h2 style="font-style: italic">Tasks List</h2>
	<s:url id="taskUrl" action="taskClient">
		<s:param name="taskName" value="taskReservation.taskName"/>
		<s:param name="taskDuration" value="taskReservation.taskDuration"/>
		<s:param name="taskPlan" value="taskReservation.taskPlan"/>
		<s:param name="action">start</s:param>
	</s:url>
	<s:a href="%{taskUrl}"><s:property value="taskReservation.taskName"/></s:a>
</body>
</html>