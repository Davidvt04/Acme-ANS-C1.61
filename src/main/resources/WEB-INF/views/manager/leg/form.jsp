<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Leg Details</title>
</head>
<body>
    <acme:form readonly="false">
        <!-- Basic leg fields -->
        <acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber" />
        <acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture" />
        <acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival" />
        <acme:input-textbox code="manager.leg.form.label.durationInHours" path="durationInHours" />
        <acme:input-select code="manager.leg.form.label.status" path="status"  choices="${legStatuses}" />
        
        <!-- Departure Airport -->
        <acme:input-select 
            code="manager.leg.form.label.departureAirport" 
            path="departureAirport"
            choices="${departureAirports}" />

        <!-- Arrival Airport -->
        <acme:input-select 
            code="manager.leg.form.label.arrivalAirport" 
            path="arrivalAirport"
            choices="${arrivalAirports}" />
            
	  <acme:input-select 
	        code="manager.leg.form.label.aircraft" 
	        path="aircraft"
	        choices="${aircraftChoices}" />
	        
        <!-- Additional buttons and logic -->
        <c:if test="${draftMode and _command != 'create'}">
            <acme:submit code="manager.leg.form.button.publish" action="/manager/leg/publish" />
        </c:if>
        
        <c:choose>
            <c:when test="${_command == 'create'}">
                <acme:submit code="manager.leg.form.button.create" action="/manager/leg/create?flightId=${param.flightId}" />
            </c:when>
            <c:otherwise>
                <c:if test="${draftMode}">
                    <acme:submit code="manager.leg.form.button.update" action="/manager/leg/update" />
                    <acme:submit code="manager.leg.form.button.delete" action="/manager/leg/delete" />
                </c:if>
            </c:otherwise>
        </c:choose>
    </acme:form>
</body>
</html>
