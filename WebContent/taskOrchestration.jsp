<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- 
	The Task Orchestration web page. It contains orchestration steps that allows the user
	to add one Task entry at a time. Uses a similar interaction protocol as the capabilities
	web pages and the taskClient web page, i.e. ajax-based HTTP reuqest/JSON-based return. Uses jQuery.
 -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript"
	src="http://ajax	.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js">
	
</script>
<script src="http://code.jquery.com/ui/1.8.21/jquery-ui.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(
			function() {
				var i = 1;
				var step = 1;
				$.getJSON("http://" + document.location.host
						+ "/gizmoapp/orchestration.action?action=init",
						function(result) {
							var mySelect = $('#primDD1');
							$.each(result.orchStep.primitivesList, function(val, text) {
								mySelect.append($(
										'<option></option>')
										.val(val).html(text.name));
							});
							var mySelect = $('#depDD1');
							$.each(result.orchStep.primitiveDependenciesList, function(val, text) {
								mySelect.append($(
										'<option></option>')
										.val(val).html(text.name));
							});
							$("#depDD1").attr("disabled", true);
							
						});
				
			$("#addSequence").click( function(){
					addTask("SEQUENTIAL", i, step);
					i = i+1;
					step = step + 1;
					
			});//addSequence click function
				
			$("#addParallel").click( function(){
				addTask("PARALLEL", i, 1);
				i = i+1;
				
		});		//addParallel click function
			
			$("#saveTask").click(function(){
				var taskName = $("#taskName").val() + ".xml";
				//Get selected primitive and Depends on
				var selPrimitive = $("#primDD" + i + " option:selected").text();
				if(i == 1){
					var selDependsOn = -1;
					var selDependsOnName = "";
				}					
				else{
					var selDependsOn= $("#depDD" + i + " option:selected").val();
					var selDependsOnName = $("#depDD" + i + " option:selected").text();
				}
					
				var selInputMap = $("#inputMap" + i).val();
				var selOutputMap = $("#outputMap" + i).val();
				
				//Disable the primitives and Depends on at this level
				$("#primDD" + i).attr("disabled", true);
				$("#depDD" + i).attr("disabled", true);
				$("#delButton" + i).attr("disabled", true);

				$.getJSON("http://" + document.location.host
						+ "/gizmoapp/orchestration.action?action=save",
								{fileName: taskName, primitiveName: selPrimitive, dependsOnID: selDependsOn, 
								dependsOnName: selDependsOnName, inputMap: selInputMap, outputMap: selOutputMap},
								function(result){									
									if (result.orchStep.saveStatus == "SUCCESS")
										alert("Task saved!");
									else
										alert("Please try a different task name!");
								});	//getJSON for save		
								
			});
		
			});//doc ready and its function		
			
			function addTask(typeOfTask, i, step) {
				
				//Get selected primitive and Depends on
				var selPrimitive = $("#primDD" + i + " option:selected").text();
				if(i == 1){
					var selDependsOn = -1;
					var selDependsOnName = "";
				}					
				else{
					var selDependsOn= $("#depDD" + i + " option:selected").val();
					var selDependsOnName = $("#depDD" + i + " option:selected").text();
				}					
				var selInputMap = $("#inputMap" + i).val();
				var selOutputMap = $("#outputMap" + i).val();
				//Disable the primitives and Depends on at this level
				$("#primDD" + i).attr("disabled", true);
				$("#depDD" + i).attr("disabled", true);
				$("#delButton" + i).attr("disabled", true);
				
				//Send data and get data for the next step
				$.getJSON("http://" + document.location.host
						+ "/gizmoapp/orchestration.action?action=addTask",
								{primitiveName: selPrimitive, dependsOnID: selDependsOn, taskType: typeOfTask, 
								dependsOnName: selDependsOnName, inputMap: selInputMap, outputMap: selOutputMap},
								
								function(result){										
							
									i = i + 1;
									
									var newInnerTableName = "step" + i;
									var newInnerTable = '<table id = "' + newInnerTableName + '"></table>';
										
									var newPrimDDName = "primDD" + i;
									var newPrimDD = '<select id = "' + newPrimDDName + '" style="width: 150px"></select>';
									var newDepDDName = "depDD" + i;
									var newDepDD = '<select id = "' + newDepDDName + '" style="width: 150px"></select>';	
									var newDelButtonName = "delButton" + i;
									var newDelButton = '<button id = "' + newDelButtonName + '" class="deletion" style="width: 50px" onclick="deleteStep('+i+')">Delete</button>';
									var inputOutputMap = '<td><label>Map Input </label><input id = "inputMap' + i + '"></td><td><label> to </label><input id = "outputMap' + i + '"></td>';
									
									if (typeOfTask == 'SEQUENTIAL'){
										step = step + 1;
										var newInnerRow = '<td>' + newPrimDD + '</td><td>' + newDepDD + '</td><td>' + newDelButton + '</td>';
										$('#tasks').append('<tr><td>' + newInnerTable + '</td></tr>');
									}
									else{
										var newInnerRow = '<td>' + newPrimDD + '</td><td>' + newDepDD + '</td><td>' + newDelButton + '</td>';										
										$('#tasks tr:last').append('<td>' + newInnerTable + '</td>');
									}
									
									$("#" + newInnerTableName).append('<tr>' + newInnerRow + '</tr>');
									$("#" + newInnerTableName).append('<tr>' + inputOutputMap + '</tr>');
									
									$.each(result.orchStep.primitivesList, function(val,text) {
										$('#'+ newPrimDDName).append($(
												'<option></option>')
												.val(val).html(text.name));//append
										}//function(val,text) within each
									);//each	
									
									$.each(result.orchStep.primitiveDependenciesList, function(val,text) {
										$('#'+ newDepDDName).append($(
												'<option></option>')
												.val(text.id).attr("value",text.id).html(text.name));//append
										}//function(val,text) within each
									);//each
									
								}//function(result) getJSON callback function 
				);	//addSequence getJSON method

		} //addTask click -> func
		
		function deleteStep(i){
			$('#primDD' + i).remove();
			$('#depDD' + i).remove();
			$('#delButton' + i).remove();
			$('#div' + i).remove();
			i = i - 1;
			
			$('#primDD' + i).attr("disabled", false);
			$('#depDD' + i).attr("disabled", false);
			$('#delButton' + i).attr("disabled", false);
			$('#div' + i).attr("disabled", false);
		}
</script>
<title>Task Orchestration</title>
</head>
<body>
	<h2>Compose a new task</h2>
	<table id=tasks>
	<tr><td>
	<table id="step11">
	<tr>
	<th> </th>
	<th>Primitives</th>
	<th> </th>
	<th>Depends On</th>
	<th> </th>
	
	</tr>
	<tr><td> </td>
		<td> <select id=primDD1 style="width: 150px"></select> </td>
		<td> </td>
		<td> <select id=depDD1 style="width: 150px"></select> </td>
		<td> <button id=delButton1 class="deletion" style="width: 50px">Delete</button> </td>		
	</tr>
	<tr>
		<td><label>Map input</label></td> 
		<td><input id = "inputMap1"></td>	
		<td><label> to </label></td> 
		<td><input id = "outputMap1"></td>
	</tr>
	</table></td>
	</tr>
	</table>
	<hr/>	

	<button id="addSequence">Add in sequence</button>
	<button id="addParallel">Add in parallel</button>
	
	Enter a name for the task: <input id="taskName">
	<button id="saveTask">Save Task</button>
	
	
</body>
</html>