<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="technician.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.nearest-maintenance-record"/>
		</th>
		<td>
			<acme:print value="${nearestMaintenanceRecord}"/>
		</td>
	</tr>
	
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.estimated-duration-avg"/>
		</th>
		<td>
			<acme:print value="${averageNumberOfEstimatedDuration}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.estimated-duration-min"/>
		</th>
		<td>
			<acme:print value="${minimumNumberOfEstimatedDuration}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.estimated-duration-max"/>
		</th>
		<td>
			<acme:print value="${maximumNumberOfEstimatedDuration}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.estimated-duration-stddev"/>
		</th>
		<td>
			<acme:print value="${standardDeviationOfEstimatedDuration}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="technician.dashboard.form.title.estimated-cost-stats"/>
</h2>

<table class="table table-sm">
     <thead>
	     <tr>
	     	<th scope="row">
				<acme:print code="technician.dashboard.form.label.estimated-cost-currency"/>
			</th>
		   	<th scope="row">
				<acme:print code="technician.dashboard.form.label.estimated-cost-avg"/>
			</th>
			<th scope="row">
				<acme:print code="technician.dashboard.form.label.estimated-cost-min"/>
			</th>
			<th scope="row">
				<acme:print code="technician.dashboard.form.label.estimated-cost-max"/>
			</th>
			<th scope="row">
				<acme:print code="technician.dashboard.form.label.estimated-cost-stddev"/>
			</th>
		 </tr>
     </thead>
     <tbody>
        <jstl:forEach var="row" items="${averageMinMaxStdDevOfEstimatedCost}">
		  <tr>
		    <td><acme:print value="${row[0]}"/></td> <!-- Currency -->
		    <td><acme:print value="${row[1]}"/></td> <!-- Average -->
		    <td><acme:print value="${row[2]}"/></td> <!-- Min -->
		    <td><acme:print value="${row[3]}"/></td> <!-- Max -->
		    <td><acme:print value="${row[4]}"/></td> <!-- StdDev -->
		  </tr>
		</jstl:forEach>          
    </tbody>
</table>

<h2>
	<acme:print code="technician.dashboard.form.title.top-five-aircrafts-more-tasks"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.aircrafts-more-tasks-first"/>
		</th>
		<td>
			<acme:print value="${topFiveAircraftsWithMoreTasksFirst}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.aircrafts-more-tasks-second"/>
		</th>
		<td>
			<acme:print value="${topFiveAircraftsWithMoreTasksSecond}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.aircrafts-more-tasks-third"/>
		</th>
		<td>
			<acme:print value="${topFiveAircraftsWithMoreTasksThird}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.aircrafts-more-tasks-fourth"/>
		</th>
		<td>
			<acme:print value="${topFiveAircraftsWithMoreTasksFourth}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="technician.dashboard.form.label.aircrafts-more-tasks-fifth"/>
		</th>
		<td>
			<acme:print value="${topFiveAircraftsWithMoreTasksFifth}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="technician.dashboard.form.title.maintenance-records-per-status"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"PENDING", "IN_PROGRESS", "COMPLETED"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${numberOfPendingMaintenanceRecords}"/>, 
						<jstl:out value="${numberOfInProgressMaintenanceRecords}"/>, 
						<jstl:out value="${numberOfCompletedMaintenanceRecords}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>