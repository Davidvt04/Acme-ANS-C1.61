<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.maintenance-record.list.label.moment" path="moment" width="30%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="20%"/>
	<acme:list-column code="technician.maintenance-record.list.label.next-inspection" path="nextInspectionDueTime" width="30%"/>
	<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>
