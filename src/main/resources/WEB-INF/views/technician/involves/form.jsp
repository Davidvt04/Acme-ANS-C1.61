

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="technician.involves.form.label.task" 
			path="task" 
			readonly="${_command != 'create'}" 
			choices="${tasks}" />
	<acme:input-select code="technician.involves.form.label.maintenance-record" 
			path="maintenanceRecord" 
			readonly="${_command != 'create'}" 
			choices="${maintenanceRecords}" />
	<jstl:if test="${_command != 'create'}">
		<acme:input-textbox code="technician.involves.form.label.task-technician" path="taskTechnician" readonly="true" />
		<acme:input-textbox code="technician.involves.form.label.record-technician" path="maintenanceRecordTechnician" readonly="true" />
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete') && draftRecord == true}">
			<acme:submit code="technician.involves.form.button.delete"
				action="/technician/involves/delete" />
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.involves.form.button.create"
				action="/technician/involves/create" />
		</jstl:when>
	</jstl:choose>
</acme:form>