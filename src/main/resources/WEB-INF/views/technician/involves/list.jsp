

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.involves.list.label.task-ticker" path="taskTicker" width="30%"/>
	<acme:list-column code="technician.involves.list.label.task-technician" path="taskTechnician" width="20%"/>	
	<acme:list-column code="technician.involves.list.label.record-ticker" path="maintenanceRecordTicker" width="30%"/>
	<acme:list-column code="technician.involves.list.label.record-technician" path="maintenanceRecordTechnician" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="technician.involves.list.button.create" action="/technician/involves/create"/>