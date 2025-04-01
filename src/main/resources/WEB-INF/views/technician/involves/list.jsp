

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="technician.involves.list.label.ticker" path="taskTicker" width="40%"/>
	<acme:list-column code="technician.involves.list.label.type" path="taskType" width="40%"/>	
	<acme:list-column code="technician.involves.list.label.priority" path="taskPriority" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.involves.list.button.create" action="/technician/involves/create?masterId=${masterId}"/>
</jstl:if>