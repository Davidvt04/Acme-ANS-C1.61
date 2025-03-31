<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Flight Details</title>
</head>
<body>
    <h1>Flight Details</h1>
    <acme:form readonly="false">
        <acme:input-textbox code="manager.flight.form.label.tag" path="tag" />
        <acme:input-checkbox code="manager.flight.form.label.requiresSelfTransfer" path="requiresSelfTransfer" />
        <acme:input-textbox code="manager.flight.form.label.cost" path="cost" />
        <acme:input-textbox code="manager.flight.form.label.description" path="description" />
        <acme:input-moment code="manager.flight.form.label.scheduledDeparture" path="scheduledDeparture" />
        <acme:input-moment code="manager.flight.form.label.scheduledArrival" path="scheduledArrival" />
        
        <!-- Publish button, only visible if the flight is still in draft mode -->
        <c:if test="${draftMode == true}">
            <acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish" />
        </c:if>
        
        <c:choose>
            <c:when test="${_command == 'create'}">
                <acme:submit code="manager.flight.form.button.create" action="/manager/flight/create" />
            </c:when>
            <c:when test="${(_command == 'show' || _command == 'update') && draftMode}">
                <acme:submit code="manager.flight.form.button.update" action="/manager/flight/update" />
                <acme:submit 
                    code="manager.flight.form.button.delete" 
                    action="/manager/flight/delete" />
            </c:when>
        </c:choose>
        
        <acme:button code="manager.flight.form.button.legs" action="/manager/leg/list?flightId=${id}" />
    </acme:form>
</body>
</html>
