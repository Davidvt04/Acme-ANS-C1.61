<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="assistaceAgent.delay-dashboard.list.label.airline" path="airline"/>
	<acme:input-textbox code="assistaceAgent.delay-dashboard.list.label.departureAirport" path="departureAirport"/>
	<acme:input-textbox code="assistaceAgent.delay-dashboard.list.label.arrivalAirport" path="arrivalAirport"/>
	<acme:input-textbox code="assistaceAgent.delay-dashboard.list.label.status" path="status"/>
	<acme:input-moment code="assistaceAgent.delay-dashboard.list.label.departureScheduledDateTime" path="departureScheduledDateTime"/>
	<acme:input-moment code="assistaceAgent.delay-dashboard.list.label.arrivalScheduledDateTime" path="arrivalScheduledDateTime"/>
	<acme:input-moment code="assistaceAgent.delay-dashboard.list.label.departureActualDateTime" path="departureActualDateTime"/>
	<acme:input-moment code="assistaceAgent.delay-dashboard.list.label.arrivalActualDateTime" path="arrivalActualDateTime"/>
	
	
</acme:form>