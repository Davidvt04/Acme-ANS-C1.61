<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Leg List</title>
</head>
<body>
    <acme:list navigable="true" show="show">
        <acme:list-column path="flightNumber" code="manager.leg.list.label.flightNumber" />
        <acme:list-column path="originCity" code="manager.leg.list.label.departureCity" />
        <acme:list-column path="destinationCity" code="manager.leg.list.label.arrivalCity" />
        <acme:list-column path="scheduledDeparture" code="manager.leg.list.label.scheduledDeparture" />
        <acme:list-column path="scheduledArrival" code="manager.leg.list.label.scheduledArrival"/>
        <acme:list-column path="durationInHours" code="manager.leg.list.label.duration" />
        <acme:list-column path="status" code="manager.leg.list.label.status" />
        
    </acme:list>
    
    <c:if test="${flightDraftMode}">
		<acme:button code="manager.leg.list.button.create" action="/manager/leg/create?flightId=${param.flightId}" />
	</c:if>
</body>
</html>
