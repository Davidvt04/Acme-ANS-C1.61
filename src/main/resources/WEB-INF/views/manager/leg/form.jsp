<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Leg Details</title>
</head>
<body>
    <h1>Leg Details</h1>
    <p>Flight ID: <c:out value="${param.flightId}" /></p>
    <acme:form readonly="false">
        <!-- Hidden fields to ensure the required parameters are submitted -->
        <input type="hidden" name="_command" value="create" />
        <input type="hidden" name="flightId" value="${param.flightId}" />
        
        <!-- Leg basic details -->
        <acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber" />
        <acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture" />
        <acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival" />
        <acme:input-textbox code="manager.leg.form.label.durationInHours" path="durationInHours" />
        
        <!-- Publish button: visible only if draftMode is true and command is not 'create' -->
        <c:if test="${draftMode and _command != 'create'}">
            <acme:submit code="manager.leg.form.button.publish" action="/manager/leg/publish" />
        </c:if>
        
        <!-- Conditional submit: create vs. update/delete -->
        <c:choose>
            <c:when test="${_command == 'create'}">
                <acme:submit code="manager.leg.form.button.create" action="/manager/leg/create" />
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
