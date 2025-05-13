<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list >
	<acme:list-column code="assistaceAgent.delay-dashboard.list.label.airline" path="airline" width="20%"/>
	<acme:list-column code="assistaceAgent.delay-dashboard.list.label.departureAirport" path="departureAirport" width="20%"/>
	<acme:list-column code="assistaceAgent.delay-dashboard.list.label.arrivalAirport" path="arrivalAirport" width="20%"/>
	<acme:list-column code="assistaceAgent.delay-dashboard.list.label.status" path="status" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>